package net.swofty.type.replayviewer.playback;

import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.AbsoluteBlockBatch;
import net.minestom.server.instance.block.Block;
import org.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

public final class MapDeserializer {

    private static final int CHUNK_SECTION_SIZE = 16;

    private MapDeserializer() {}

    /**
     * Loads map data into an instance.
     *
     * @param instance The instance to load the map into
     * @param compressedData The compressed map data
     * @throws IOException If deserialization fails
     */
    public static void loadMap(Instance instance, byte[] compressedData) throws IOException {
        // Decompress
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
        InflaterInputStream iis = new InflaterInputStream(bais);
        byte[] decompressed = iis.readAllBytes();
        iis.close();

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(decompressed));

        // Read header
        int centerChunkX = dis.readInt();
        int centerChunkZ = dis.readInt();
        int radius = dis.readInt();
        int minY = dis.readInt();
        int maxY = dis.readInt();
        int chunkCount = dis.readInt();

        // Read palette
        int paletteSize = dis.readInt();
        Map<Integer, Integer> paletteToStateId = new HashMap<>();
        for (int i = 0; i < paletteSize; i++) {
            int stateId = dis.readInt();
            int paletteId = dis.readInt();
            paletteToStateId.put(paletteId, stateId);
        }

        Logger.debug("Loading map: center=({}, {}), radius={}, chunks={}, palette={}",
            centerChunkX, centerChunkZ, radius, chunkCount, paletteSize);

        // Read and apply chunks
        AbsoluteBlockBatch batch = new AbsoluteBlockBatch();

        for (int c = 0; c < chunkCount; c++) {
            int chunkX = dis.readInt();
            int chunkZ = dis.readInt();
            int bitsPerBlock = dis.readByte() & 0xFF;

            // Read packed blocks
            int[] paletteIds = readPackedBlocks(dis, bitsPerBlock, minY, maxY);

            // Apply blocks
            int index = 0;
            int sectionsY = (maxY - minY) / CHUNK_SECTION_SIZE;

            for (int sectionY = 0; sectionY < sectionsY; sectionY++) {
                int baseY = minY + sectionY * CHUNK_SECTION_SIZE;

                for (int y = 0; y < CHUNK_SECTION_SIZE; y++) {
                    for (int z = 0; z < CHUNK_SECTION_SIZE; z++) {
                        for (int x = 0; x < CHUNK_SECTION_SIZE; x++) {
                            if (index >= paletteIds.length) break;

                            int paletteId = paletteIds[index++];
                            int stateId = paletteToStateId.getOrDefault(paletteId, 0);

                            if (stateId != 0) { // Skip air
                                Block block = Block.fromStateId(stateId);
                                if (block != null) {
                                    int worldX = chunkX * 16 + x;
                                    int worldY = baseY + y;
                                    int worldZ = chunkZ * 16 + z;
                                    batch.setBlock(worldX, worldY, worldZ, block);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Apply batch
        batch.apply(instance, null);

        Logger.info("Map loaded successfully: {} chunks", chunkCount);
    }

    private static int[] readPackedBlocks(DataInputStream dis, int bitsPerBlock, int minY, int maxY) throws IOException {
        int numLongs = dis.readInt();
        int sectionsY = (maxY - minY) / CHUNK_SECTION_SIZE;
        int blocksPerChunk = CHUNK_SECTION_SIZE * CHUNK_SECTION_SIZE * sectionsY * CHUNK_SECTION_SIZE;
        int[] blocks = new int[blocksPerChunk];

        int blocksPerLong = 64 / bitsPerBlock;
        long mask = (1L << bitsPerBlock) - 1;

        int blockIndex = 0;
        for (int i = 0; i < numLongs && blockIndex < blocks.length; i++) {
            long packed = dis.readLong();

            for (int j = 0; j < blocksPerLong && blockIndex < blocks.length; j++) {
                blocks[blockIndex++] = (int) ((packed >> (j * bitsPerBlock)) & mask);
            }
        }

        return blocks;
    }
}
