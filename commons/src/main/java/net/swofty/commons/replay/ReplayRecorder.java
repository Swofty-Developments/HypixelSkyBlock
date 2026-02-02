package net.swofty.commons.replay;

import lombok.Getter;
import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.objects.replay.ReplayDataBatchProtocolObject;
import net.swofty.commons.protocol.objects.replay.ReplayEndProtocolObject;
import net.swofty.commons.protocol.objects.replay.ReplayMapUploadProtocolObject;
import net.swofty.commons.protocol.objects.replay.ReplayStartProtocolObject;
import net.swofty.commons.replay.protocol.ReplayDataWriter;
import net.swofty.commons.replay.recordable.Recordable;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * remember:
 * 1. Create a ReplayRecorder when game starts
 * 2. Call tick() every server tick
 * 3. Call record() to add recordables
 * 4. Call finish() when game ends
 */
public class ReplayRecorder {
	private static final int BATCH_SIZE = 1000;
	private static final int BATCH_INTERVAL_TICKS = 200; // 10 seconds at 20 TPS

	@Getter
	private final UUID replayId;
	private final String gameId;
	private final ServerType serverType;
	private final Consumer<Object> serviceSender;

	private final ConcurrentLinkedQueue<Recordable> buffer = new ConcurrentLinkedQueue<>();
	private final AtomicInteger batchIndex = new AtomicInteger(0);

	@Getter
	private volatile int currentTick = 0;
	private volatile int lastBatchTick = 0;
	private volatile boolean recording = false;
	private volatile boolean finished = false;

	// Map center for coordinate optimization
	private double mapCenterX = 0;
	private double mapCenterZ = 0;

	/**
	 * Creates a new replay recorder.
	 *
	 * @param gameId        The game ID
	 * @param serverType    The server type
	 * @param serviceSender Function to send protocol messages to replay service
	 */
	public ReplayRecorder(String gameId, ServerType serverType, Consumer<Object> serviceSender) {
		this.replayId = UUID.randomUUID();
		this.gameId = gameId;
		this.serverType = serverType;
		this.serviceSender = serviceSender;
	}

	/**
	 * Starts the recording session.
	 */
	public void start(
		String gameTypeName,
		String mapName,
		String mapHash,
		Map<UUID, String> players,
		Map<String, List<UUID>> teams,
		Map<String, ReplayStartProtocolObject.TeamInfo> teamInfo
	) {
		recording = true;
		currentTick = 0;
		lastBatchTick = 0;

		var startMessage = new ReplayStartProtocolObject.StartMessage(
			replayId,
			gameId,
			serverType,
			gameTypeName,
			mapName,
			mapHash,
			System.currentTimeMillis(),
			mapCenterX,
			mapCenterZ,
			players,
			teams,
			teamInfo
		);

		serviceSender.accept(startMessage);
		Logger.info("Started replay recording {} for game {}", replayId, gameId);
	}

	public void setMapCenter(double x, double z) {
		this.mapCenterX = x;
		this.mapCenterZ = z;
	}

	public void tick() {
		if (!recording || finished) return;

		currentTick++;

		// Check if we should send a batch
		if (shouldSendBatch()) {
			sendBatch();
		}
	}

	public void record(Recordable recordable) {
		if (!recording || finished) return;

		recordable.setTick(currentTick);
		buffer.offer(recordable);

		if (buffer.size() >= BATCH_SIZE) {
			sendBatch();
		}
	}

	public void recordAll(Collection<? extends Recordable> recordables) {
		if (!recording || finished) return;

		for (Recordable recordable : recordables) {
			recordable.setTick(currentTick);
			buffer.offer(recordable);
		}

		if (buffer.size() >= BATCH_SIZE) {
			sendBatch();
		}
	}

	private boolean shouldSendBatch() {
		return !buffer.isEmpty() && (currentTick - lastBatchTick >= BATCH_INTERVAL_TICKS);
	}

	private void sendBatch() {
		if (buffer.isEmpty()) return;

		List<Recordable> toSend = new ArrayList<>();
		Recordable r;
		while ((r = buffer.poll()) != null) {
			toSend.add(r);
		}

		if (toSend.isEmpty()) return;

		int startTick = toSend.getFirst().getTick();
		int endTick = toSend.getLast().getTick();

		try {
			byte[] data = serializeRecordables(toSend);

			var batchMessage = new ReplayDataBatchProtocolObject.BatchMessage(
				replayId,
				batchIndex.getAndIncrement(),
				startTick,
				endTick,
				toSend.size(),
				data
			);

			serviceSender.accept(batchMessage);
			lastBatchTick = currentTick;

			Logger.debug("Sent batch {} with {} recordables ({} bytes)",
				batchMessage.batchIndex(), toSend.size(), data.length);

		} catch (IOException e) {
			Logger.error(e, "Failed to serialize recordables");
		}
	}

	private byte[] serializeRecordables(List<Recordable> recordables) throws IOException {
		ReplayDataWriter writer = new ReplayDataWriter();
		for (Recordable recordable : recordables) {
			writer.writeVarInt(recordable.getTick());
			writer.writeByte(recordable.getType().getId());
			recordable.write(writer);
		}
		return writer.toByteArray();
	}

	public void finish() {
		if (finished) return;
		finished = true;
		recording = false;

		sendBatch();

		var endMessage = new ReplayEndProtocolObject.EndMessage(
			replayId,
			System.currentTimeMillis(),
			currentTick
		);

		serviceSender.accept(endMessage);
		Logger.info("Finished replay recording {} ({} ticks)", replayId, currentTick);
	}

	public void uploadMapIfNeeded(String mapHash, String mapName, byte[] compressedData) {
		var uploadMessage = new ReplayMapUploadProtocolObject.MapUploadMessage(
			mapHash,
			mapName,
			compressedData
		);
		serviceSender.accept(uploadMessage);
	}

	/**
	 * @return The start tick of the current buffer
	 */
	public int getStartTick() {
		return lastBatchTick;
	}

	/**
	 * @return The number of recordables in the buffer
	 */
	public int getRecordableCount() {
		return buffer.size();
	}

	/**
	 * Flushes and returns the current batch of data without sending it.
	 * Returns null if buffer is empty.
	 */
	public byte[] flushBatch() {
		if (buffer.isEmpty()) return null;

		List<Recordable> toFlush = new ArrayList<>();
		Recordable r;
		while ((r = buffer.poll()) != null) {
			toFlush.add(r);
		}

		if (toFlush.isEmpty()) return null;

		try {
			lastBatchTick = currentTick;
			return serializeRecordables(toFlush);
		} catch (IOException e) {
			Logger.error(e, "Failed to serialize recordables for flush");
			return null;
		}
	}

	/**
	 * @return Whether recording is active
	 */
	public boolean isRecording() {
		return recording && !finished;
	}
}
