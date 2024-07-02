package net.swofty.anticheat.math;

public class ChunkUtils {
    public static int getChunkCoordinate(double xz) {
        return getChunkCoordinate((int) Math.floor(xz));
    }

    public static int getChunkCoordinate(int xz) {
        // Assume chunk/section size being 16 (4 bits)
        return xz >> 4;
    }
}
