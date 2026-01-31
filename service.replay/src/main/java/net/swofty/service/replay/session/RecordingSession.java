package net.swofty.service.replay.session;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.ServerType;
import net.swofty.commons.replay.ReplayMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

@Getter
public class RecordingSession {
	private final UUID replayId;
	private final String gameId;
	private final ServerType serverType;
	private final String gameTypeName;
	private final String mapName;
	private final String mapHash;
	private final long startTime;
	private final double mapCenterX;
	private final double mapCenterZ;
	private final Map<UUID, String> players;
	private final Map<String, List<UUID>> teams;
	private final Map<String, ReplayMetadata.TeamInfo> teamInfo;

	@Setter
	private long endTime;
	@Setter
	private int durationTicks;
	@Setter
	private String winnerId;
	@Setter
	private String winnerType;

	// Batches ordered by index
	private final ConcurrentSkipListMap<Integer, DataBatch> batches = new ConcurrentSkipListMap<>();
	private volatile long lastDataTime;
	private volatile int highestTick = 0;

	public RecordingSession(
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
		this.replayId = replayId;
		this.gameId = gameId;
		this.serverType = serverType;
		this.gameTypeName = gameTypeName;
		this.mapName = mapName;
		this.mapHash = mapHash;
		this.startTime = startTime;
		this.mapCenterX = mapCenterX;
		this.mapCenterZ = mapCenterZ;
		this.players = new HashMap<>(players);
		this.teams = new HashMap<>(teams);
		this.teamInfo = new HashMap<>(teamInfo);
		this.lastDataTime = System.currentTimeMillis();
	}

	/**
	 * Adds a batch of data to this session.
	 */
	public void addBatch(int index, int startTick, int endTick, int recordableCount, byte[] data) {
		batches.put(index, new DataBatch(index, startTick, endTick, recordableCount, data));
		lastDataTime = System.currentTimeMillis();
		if (endTick > highestTick) {
			highestTick = endTick;
		}
	}

	/**
	 * Gets all batches in order.
	 */
	public List<DataBatch> getBatches() {
		return new ArrayList<>(batches.values());
	}

	/**
	 * Gets total bytes received.
	 */
	public long getTotalBytesReceived() {
		return batches.values().stream()
			.mapToLong(b -> b.data.length)
			.sum();
	}

	/**
	 * Gets total recordable count.
	 */
	public int getTotalRecordableCount() {
		return batches.values().stream()
			.mapToInt(DataBatch::recordableCount)
			.sum();
	}

	public record DataBatch(
		int index,
		int startTick,
		int endTick,
		int recordableCount,
		byte[] data
	) {
	}
}
