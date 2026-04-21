package net.swofty.type.bedwarsgame.replay;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public final class MapSerializer {

    private static final int CHUNK_SECTION_SIZE = 16;
    private static final int MIN_Y = -64;
    private static final int MAX_Y = 320;

    private MapSerializer() {}

    /**
     * Serializes a region of the instance around a center point.
     *
     * @param instance The instance to serialize
     * @param centerChunkX Center chunk X coordinate
     * @param centerChunkZ Center chunk Z coordinate
     * @param radius Number of chunks in each direction
     * @return SerializedMap containing compressed data and metadata
     */
    public static SerializedMap serializeRegion(Instance instance, int centerChunkX, int centerChunkZ, int radius) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Write header
        dos.writeInt(centerChunkX);
        dos.writeInt(centerChunkZ);
        dos.writeInt(radius);
        dos.writeInt(MIN_Y);
        dos.writeInt(MAX_Y);

        // Collect all chunks
        List<ChunkData> chunks = new ArrayList<>();

        for (int cx = centerChunkX - radius; cx <= centerChunkX + radius; cx++) {
            for (int cz = centerChunkZ - radius; cz <= centerChunkZ + radius; cz++) {
                Chunk chunk = instance.getChunk(cx, cz);
                if (chunk != null) {
                    chunks.add(serializeChunk(chunk, cx, cz));
                }
            }
        }

        dos.writeInt(chunks.size());

        // Build global palette
        Map<Integer, Integer> palette = new HashMap<>();
        palette.put(0, 0); // Air is always 0
        int nextPaletteId = 1;

        for (ChunkData chunkData : chunks) {
            for (int stateId : chunkData.blockStates) {
                if (stateId != 0 && !palette.containsKey(stateId)) {
                    palette.put(stateId, nextPaletteId++);
                }
            }
        }

        // Write palette
        dos.writeInt(palette.size());
        for (var entry : palette.entrySet()) {
            dos.writeInt(entry.getKey()); // stateId
            dos.writeInt(entry.getValue()); // paletteId
        }

        // Write chunks
        for (ChunkData chunkData : chunks) {
            dos.writeInt(chunkData.chunkX);
            dos.writeInt(chunkData.chunkZ);

            // Encode blocks using palette
            int[] paletteIds = new int[chunkData.blockStates.length];
            for (int i = 0; i < chunkData.blockStates.length; i++) {
                paletteIds[i] = palette.getOrDefault(chunkData.blockStates[i], 0);
            }

            // Write using variable bit width
            int bitsPerBlock = Math.max(1, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            dos.writeByte(bitsPerBlock);

            // Pack blocks
            writePackedBlocks(dos, paletteIds, bitsPerBlock);
        }

        dos.flush();
        byte[] uncompressed = baos.toByteArray();

        // Compress
        byte[] compressed = compress(uncompressed);

        // Generate hash
        String hash = generateHash(compressed);

        Logger.debug("Serialized map: {} chunks, {} palette entries, {} -> {} bytes",
            chunks.size(), palette.size(), uncompressed.length, compressed.length);

        return new SerializedMap(hash, compressed, uncompressed.length, compressed.length);
    }

    private static ChunkData serializeChunk(Chunk chunk, int chunkX, int chunkZ) {
        int sectionsY = (MAX_Y - MIN_Y) / CHUNK_SECTION_SIZE;
        int[] blockStates = new int[CHUNK_SECTION_SIZE * CHUNK_SECTION_SIZE * sectionsY * CHUNK_SECTION_SIZE];

        int index = 0;
        for (int sectionY = 0; sectionY < sectionsY; sectionY++) {
            int baseY = MIN_Y + sectionY * CHUNK_SECTION_SIZE;

            for (int y = 0; y < CHUNK_SECTION_SIZE; y++) {
                for (int z = 0; z < CHUNK_SECTION_SIZE; z++) {
                    for (int x = 0; x < CHUNK_SECTION_SIZE; x++) {
                        Block block = chunk.getBlock(x, baseY + y, z);
                        blockStates[index++] = block.stateId();
                    }
                }
            }
        }

        return new ChunkData(chunkX, chunkZ, blockStates);
    }

    private static void writePackedBlocks(DataOutputStream dos, int[] blocks, int bitsPerBlock) throws IOException {
        int blocksPerLong = 64 / bitsPerBlock;
        int numLongs = (blocks.length + blocksPerLong - 1) / blocksPerLong;

        dos.writeInt(numLongs);

        long currentLong = 0;
        int bitPos = 0;
        int blockIndex = 0;

        for (int i = 0; i < numLongs; i++) {
            currentLong = 0;
            bitPos = 0;

            for (int j = 0; j < blocksPerLong && blockIndex < blocks.length; j++) {
                currentLong |= ((long) blocks[blockIndex++] & ((1L << bitsPerBlock) - 1)) << bitPos;
                bitPos += bitsPerBlock;
            }

            dos.writeLong(currentLong);
        }
    }

    private static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        try (DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater)) {
            dos.write(data);
        }
        return baos.toByteArray();
    }

    private static String generateHash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data);
            return HexFormat.of().formatHex(hash).substring(0, 16); // First 16 chars
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public record SerializedMap(String hash, byte[] compressedData, int uncompressedSize, int compressedSize) {}

    private record ChunkData(int chunkX, int chunkZ, int[] blockStates) {}
}
