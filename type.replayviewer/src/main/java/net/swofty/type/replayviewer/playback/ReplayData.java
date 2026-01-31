package net.swofty.type.replayviewer.playback;

import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.recordable.Recordable;
import net.swofty.commons.replay.recordable.RecordableType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.InflaterInputStream;

public class ReplayData {
    // Tick -> List of recordables at that tick
    private final Map<Integer, List<Recordable>> recordablesByTick = new TreeMap<>();

    // All recordables in order
    private final List<Recordable> allRecordables = new ArrayList<>();

    // Block change history for reverting
    private final TreeMap<Integer, List<BlockChangeEntry>> blockChanges = new TreeMap<>();

    public void loadFromChunks(List<byte[]> compressedChunks) throws IOException {
        for (byte[] compressedData : compressedChunks) {
            byte[] decompressed = decompress(compressedData);
            loadRecordables(decompressed);
        }
    }

    private byte[] decompress(byte[] compressed) throws IOException {
        try (InflaterInputStream iis = new InflaterInputStream(new ByteArrayInputStream(compressed))) {
            return iis.readAllBytes();
        }
    }

    private void loadRecordables(byte[] data) throws IOException {
        ReplayDataReader reader = new ReplayDataReader(data);

        while (reader.available() > 0) {
            int tick = reader.readVarInt();
            int typeId = reader.readUnsignedByte();

            RecordableType type = RecordableType.byId(typeId);
            if (type == null) {
                throw new IOException("Unknown recordable type: " + typeId);
            }

            Recordable recordable = type.createAndRead(reader);
            recordable.setTick(tick);

            allRecordables.add(recordable);
            recordablesByTick.computeIfAbsent(tick, k -> new ArrayList<>()).add(recordable);

            // Track block changes for reverting
            if (type == RecordableType.BLOCK_CHANGE) {
                var bc = (net.swofty.commons.replay.recordable.RecordableBlockChange) recordable;
                blockChanges.computeIfAbsent(tick, k -> new ArrayList<>())
                        .add(new BlockChangeEntry(bc.getX(), bc.getY(), bc.getZ(),
                                bc.getPreviousBlockStateId(), bc.getBlockStateId()));
            }
        }
    }

    /**
     * Gets all recordables at a specific tick.
     */
    public List<Recordable> getRecordablesAt(int tick) {
        return recordablesByTick.getOrDefault(tick, Collections.emptyList());
    }

    /**
     * Gets recordables between two ticks (inclusive).
     */
    public List<Recordable> getRecordablesBetween(int startTick, int endTick) {
        List<Recordable> result = new ArrayList<>();
        for (int tick = startTick; tick <= endTick; tick++) {
            result.addAll(getRecordablesAt(tick));
        }
        return result;
    }

    /**
     * Gets block changes between two ticks.
     */
    public List<BlockChangeEntry> getBlockChangesBetween(int startTick, int endTick) {
        List<BlockChangeEntry> result = new ArrayList<>();
        blockChanges.subMap(startTick, true, endTick, true)
                .values()
                .forEach(result::addAll);
        return result;
    }

    /**
     * Gets all ticks that have recordables.
     */
    public Set<Integer> getAllTicks() {
        return recordablesByTick.keySet();
    }

    /**
     * Gets the highest tick number.
     */
    public int getMaxTick() {
        if (recordablesByTick.isEmpty()) return 0;
        return ((TreeMap<Integer, ?>) recordablesByTick).lastKey();
    }

    /**
     * Block change entry for reverting.
     */
    public record BlockChangeEntry(int x, int y, int z, int previousStateId, int newStateId) {}
}
