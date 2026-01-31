package net.swofty.commons.replay.storage;

import lombok.Getter;
import net.swofty.commons.replay.protocol.ReplayDataWriter;
import net.swofty.commons.replay.recordable.Recordable;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReplayRecordingBuffer {
	private static final int MAX_HEAP_RECORDABLES = 10000;
	private static final long FLUSH_INTERVAL_MS = 10000; // 10 seconds

	/**
	 * -- GETTER --
	 *  Gets the replay ID.
	 */
	@Getter
	private final UUID replayId;
	private final String gameId;
	private final Path tempFile;

	// In-memory buffer (up to 10 seconds of data)
	private final ConcurrentLinkedQueue<Recordable> heapBuffer = new ConcurrentLinkedQueue<>();
	private final AtomicInteger heapCount = new AtomicInteger(0);
	private final AtomicLong lastFlushTime = new AtomicLong(System.currentTimeMillis());

	// File buffer for overflow
	private DataOutputStream fileBuffer;
	private final AtomicLong bytesWritten = new AtomicLong(0);

	// Tracking
	private volatile int currentTick = 0;
	private volatile boolean closed = false;

	public ReplayRecordingBuffer(String gameId) throws IOException {
		this.replayId = UUID.randomUUID();
		this.gameId = gameId;
		this.tempFile = Files.createTempFile("replay_" + gameId + "_", ".tmp");
		this.fileBuffer = new DataOutputStream(new BufferedOutputStream(
			Files.newOutputStream(tempFile), 65536
		));
	}

	/**
	 * Adds a recordable to the buffer.
	 */
	public void record(Recordable recordable) {
		if (closed) return;

		recordable.setTick(currentTick);
		heapBuffer.offer(recordable);
		int count = heapCount.incrementAndGet();

		// Check if we need to flush
		if (count >= MAX_HEAP_RECORDABLES || shouldFlush()) {
			flush();
		}
	}

	/**
	 * Advances the current tick.
	 */
	public void tick() {
		currentTick++;
	}

	/**
	 * Sets the current tick explicitly.
	 */
	public void setTick(int tick) {
		this.currentTick = tick;
	}

	private boolean shouldFlush() {
		return System.currentTimeMillis() - lastFlushTime.get() > FLUSH_INTERVAL_MS;
	}

	/**
	 * Flushes heap buffer to disk.
	 */
	public synchronized void flush() {
		if (closed || heapBuffer.isEmpty()) return;

		List<Recordable> toFlush = new ArrayList<>();
		Recordable r;
		while ((r = heapBuffer.poll()) != null) {
			toFlush.add(r);
		}
		heapCount.set(0);
		lastFlushTime.set(System.currentTimeMillis());

		try {
			ReplayDataWriter writer = new ReplayDataWriter();
			for (Recordable recordable : toFlush) {
				// Write: tick (VarInt), type (byte), data
				writer.writeVarInt(recordable.getTick());
				writer.writeByte(recordable.getType().getId());
				recordable.write(writer);
			}
			byte[] data = writer.toByteArray();
			fileBuffer.writeInt(data.length);
			fileBuffer.write(data);
			bytesWritten.addAndGet(4 + data.length);
		} catch (IOException e) {
			throw new RuntimeException("Failed to flush replay buffer", e);
		}
	}

	/**
	 * Finishes recording and returns the path to the temp file.
	 */
	public Path finish() throws IOException {
		if (closed) return tempFile;
		closed = true;

		flush();
		fileBuffer.close();

		return tempFile;
	}

	public void cleanup() {
		try {
			Files.deleteIfExists(tempFile);
		} catch (IOException ignored) {
		}
	}
}
