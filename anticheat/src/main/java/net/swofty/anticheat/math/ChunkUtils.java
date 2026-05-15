package net.swofty.anticheat.math;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChunkUtils {

    public static int getChunkCoordinate(double xz) {
        return getChunkCoordinate((int) Math.floor(xz));
    }

    public static int getChunkCoordinate(int xz) {
        // Assume chunk/section size being 16 (4 bits)
        return xz >> 4;
    }
}
