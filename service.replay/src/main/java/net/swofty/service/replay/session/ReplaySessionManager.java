package net.swofty.service.replay.session;

import net.swofty.commons.ServerType;
import net.swofty.commons.replay.ReplayMetadata;
import net.swofty.service.replay.storage.ReplayDatabase;
import org.bson.Document;
import org.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import java.util.zip.Deflater;

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

	/**
	 * Starts a new recording session.
	 */
	public RecordingSession startSession(
		UUID replayId,
		String gameId,
		ServerType serverType,
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
			replayId, gameId, serverType, gameTypeName, mapName, mapHash,
			startTime, mapCenterX, mapCenterZ, players, teams, teamInfo
		);

		activeSessions.put(replayId, session);
		Logger.info("Started replay recording session {} for game {}", replayId, gameId);

		return session;
	}

	/**
	 * Gets an active recording session.
	 */
	public RecordingSession getSession(UUID replayId) {
		return activeSessions.get(replayId);
	}

	/**
	 * Receives a batch of replay data.
	 */
	public void receiveBatch(UUID replayId, int batchIndex, int startTick, int endTick, int recordableCount, byte[] data) {
		RecordingSession session = activeSessions.get(replayId);
		if (session == null) {
			Logger.warn("Received data for unknown session: {}", replayId);
			return;
		}

		session.addBatch(batchIndex, startTick, endTick, recordableCount, data);
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

	private EndResult finalizeSession(RecordingSession session) throws IOException {
		Logger.info("Finalizing replay session {}", session.getReplayId());

		long totalBytes = 0;
		long compressedBytes = 0;

		// Process and store each batch
		List<RecordingSession.DataBatch> batches = session.getBatches();
		for (RecordingSession.DataBatch batch : batches) {
			byte[] compressed = compressData(batch.data());
			database.saveReplayDataChunk(
				session.getReplayId(),
				batch.index(),
				compressed,
				batch.startTick(),
				batch.endTick()
			);
			totalBytes += batch.data().length;
			compressedBytes += compressed.length;
		}

		// Store metadata
		Document metadata = createMetadataDocument(session, compressedBytes);
		database.saveReplayMetadata(metadata);

		Logger.info("Replay {} finalized: {} bytes -> {} bytes ({:.1f}% reduction)",
			session.getReplayId(), totalBytes, compressedBytes,
			(1 - (double) compressedBytes / totalBytes) * 100);

		return new EndResult(true, totalBytes, compressedBytes);
	}

	private Document createMetadataDocument(RecordingSession session, long dataSize) {
		Document doc = new Document()
			.append("replayId", session.getReplayId().toString())
			.append("gameId", session.getGameId())
			.append("serverType", session.getServerType().name())
			.append("gameTypeName", session.getGameTypeName())
			.append("mapName", session.getMapName())
			.append("mapHash", session.getMapHash())
			.append("version", net.swofty.commons.replay.ReplayVersion.CURRENT_VERSION)
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

	private byte[] compressData(byte[] data) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
		deflater.setInput(data);
		deflater.finish();

		byte[] buffer = new byte[4096];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			out.write(buffer, 0, count);
		}
		deflater.end();

		return out.toByteArray();
	}

	/**
	 * Starts the cleanup task for stale sessions.
	 */
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
