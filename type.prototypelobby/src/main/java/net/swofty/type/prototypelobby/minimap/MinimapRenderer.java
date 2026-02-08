package net.swofty.type.prototypelobby.minimap;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.MapDataPacket;

import java.util.List;

public class MinimapRenderer {
    private static final int MAP_SIZE = 128;
    public static final int MINIMAP_MAP_ID = 9999;
    private static final byte MARKER_COLOR = (byte) 126;

    public MapDataPacket render(Instance instance, Pos center) {
        byte[] colors = new byte[MAP_SIZE * MAP_SIZE];

        int halfMap = MAP_SIZE / 2;
        int startX = center.blockX() - halfMap;
        int startZ = center.blockZ() - halfMap;

        for (int pz = 0; pz < MAP_SIZE; pz++) {
            for (int px = 0; px < MAP_SIZE; px++) {
                int worldX = startX + px;
                int worldZ = startZ + pz;

                if (!instance.isChunkLoaded(worldX >> 4, worldZ >> 4)) {
                    colors[pz * MAP_SIZE + px] = 0;
                    continue;
                }

                int surfaceY = findSurfaceY(instance, worldX, worldZ);
                Block block = instance.getBlock(worldX, surfaceY, worldZ);
                colors[pz * MAP_SIZE + px] = BlockColorMapping.getMapColor(block);
            }
        }

        colors[0] = MARKER_COLOR;

        return new MapDataPacket(
                MINIMAP_MAP_ID,
                (byte) 0,
                false,
                false,
                List.of(),
                new MapDataPacket.ColorContent(
                        (byte) MAP_SIZE,
                        (byte) MAP_SIZE,
                        (byte) 0,
                        (byte) 0,
                        colors
                )
        );
    }

    private int findSurfaceY(Instance instance, int x, int z) {
        int maxY = instance.getCachedDimensionType().maxY();
        int minY = instance.getCachedDimensionType().minY();

        for (int y = maxY; y >= minY; y--) {
            Block block = instance.getBlock(x, y, z);
            if (!block.isAir() && block.id() != Block.CAVE_AIR.id()) {
                return y;
            }
        }
        return minY;
    }
}
