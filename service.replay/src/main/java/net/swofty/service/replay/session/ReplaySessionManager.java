package net.swofty.service.replay.session;

import net.swofty.commons.ServerType;
import net.swofty.service.replay.storage.ReplayDatabase;
import net.swofty.type.game.replay.ReplayMetadata;
import net.swofty.type.game.replay.ReplayVersion;
import org.bson.Document;
import org.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReplaySessionManager {
	private final ReplayDatabase database;
	private final Map<UUID, RecordingSession> activeSessions = new ConcurrentHashMap<>();
	private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
	private final ExecutorService compressionExecutor = Executors.newFixedThreadPool(4);

	// Session timeout - if no data received for this long, session is considered stale
	private static final long SESSION_TIMEOUT_MS = 5 * 60 * 1000; // 5 minutes

	public ReplaySessionManager(ReplayDatabase database) {
		this.database = database;
	}

	public RecordingSession startSession(
		UUID replayId,
		String gameId,
		ServerType serverType,
		String serverId,
		String gameTypeName,
		String mapName,
		String mapHash,
		long startTime,
		double mapCenterX,
		double mapCenterZ,
		Map<UUID, String> players,
		Map<String, List<UUID>> teams,
		Map<String, ReplayMetadata.TeamInfo> teamInfo
	) {
		RecordingSession session = new RecordingSession(
			replayId, gameId, serverType, serverId, gameTypeName, mapName, mapHash,
			startTime, mapCenterX, mapCenterZ, players, teams, teamInfo
		);

		activeSessions.put(replayId, session);
		Logger.info("Started replay recording session {} for game {}", replayId, gameId);

		return session;
	}

	public RecordingSession getSession(UUID replayId) {
		return activeSessions.get(replayId);
	}

	public void receiveBatch(UUID replayId, int batchIndex, int startTick, int endTick, int recordableCount, byte[] compressedData) {
		RecordingSession session = activeSessions.get(replayId);
		if (session == null) {
			Logger.warn("Received data for unknown session: {}", replayId);
			return;
		}

		session.addBatch(batchIndex, startTick, endTick, recordableCount, compressedData);
	}

	public CompletableFuture<EndResult> endSession(UUID replayId, long endTime, int durationTicks) {
		RecordingSession session = activeSessions.remove(replayId);
		if (session == null) {
			return CompletableFuture.completedFuture(new EndResult(false, 0, 0));
		}

		session.setEndTime(endTime);
		session.setDurationTicks(durationTicks);

		return CompletableFuture.supplyAsync(() -> {
			try {
				return finalizeSession(session);
			} catch (Exception e) {
				Logger.error(e, "Failed to finalize session {}", replayId);
				return new EndResult(false, 0, 0);
			}
		}, compressionExecutor);
	}

	private EndResult finalizeSession(RecordingSession session) {
		Logger.info("Finalizing replay session {}", session.getReplayId());

		long compressedBytes = 0;

		// Process and store each batch
		List<RecordingSession.DataBatch> batches = session.getOrderedBatches();
		for (RecordingSession.DataBatch batch : batches) {
			database.saveReplayDataChunk(
				session.getReplayId(),
				batch.index(),
				batch.compressedData(),
				batch.startTick(),
				batch.endTick()
			);
			compressedBytes += batch.compressedData().length;
		}

		// Store metadata
		Document metadata = createMetadataDocument(session, compressedBytes);
		database.saveReplayMetadata(metadata);

		Logger.info("Replay {} finalized: {} compressed bytes",
			session.getReplayId(), compressedBytes);

		return new EndResult(true, compressedBytes, compressedBytes);
	}

	private Document createMetadataDocument(RecordingSession session, long dataSize) {
		Document doc = new Document()
			.append("replayId", session.getReplayId().toString())
			.append("gameId", session.getGameId())
			.append("serverType", session.getServerType().name())
			.append("serverId", session.getServerId())
			.append("gameTypeName", session.getGameTypeName())
			.append("mapName", session.getMapName())
			.append("mapHash", session.getMapHash())
			.append("version", ReplayVersion.CURRENT_VERSION)
			.append("startTime", session.getStartTime())
			.append("endTime", session.getEndTime())
			.append("durationTicks", session.getDurationTicks())
			.append("mapCenterX", session.getMapCenterX())
			.append("mapCenterZ", session.getMapCenterZ())
			.append("dataSize", dataSize);

		// Players
		List<String> playerIds = new ArrayList<>();
		Document playerNames = new Document();
		session.getPlayers().forEach((uuid, name) -> {
			playerIds.add(uuid.toString());
			playerNames.append(uuid.toString(), name);
		});
		doc.append("players", playerIds);
		doc.append("playerNames", playerNames);

		// Teams
		Document teams = new Document();
		session.getTeams().forEach((teamId, uuids) -> {
			List<String> uuidStrings = uuids.stream().map(UUID::toString).toList();
			teams.append(teamId, uuidStrings);
		});
		doc.append("teams", teams);

		// Team info
		Document teamInfo = new Document();
		session.getTeamInfo().forEach((teamId, info) -> {
			teamInfo.append(teamId, new Document()
				.append("name", info.name())
				.append("colorCode", info.colorCode())
				.append("color", info.color())
			);
		});
		doc.append("teamInfo", teamInfo);

		// Winner
		if (session.getWinnerId() != null) {
			doc.append("winnerId", session.getWinnerId());
			doc.append("winnerType", session.getWinnerType());
		}

		return doc;
	}

	public void startCleanupTask() {
		cleanupExecutor.scheduleAtFixedRate(() -> {
			long now = System.currentTimeMillis();
			activeSessions.entrySet().removeIf(entry -> {
				if (now - entry.getValue().getLastDataTime() > SESSION_TIMEOUT_MS) {
					Logger.warn("Removing stale replay session: {}", entry.getKey());
					return true;
				}
				return false;
			});
		}, 1, 1, TimeUnit.MINUTES);
	}

	public int getActiveSessionCount() {
		return activeSessions.size();
	}

	public record EndResult(boolean success, long totalBytes, long compressedBytes) {
	}
}
