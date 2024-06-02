package net.swofty.types.generic.structure;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class PasterService {
    public static void fill(Pos firstPosition, Pos secondPosition, Block block, Instance instance) {
        int minX = Math.min(firstPosition.blockX(), secondPosition.blockX());
        int minY = Math.min(firstPosition.blockY(), secondPosition.blockY());
        int minZ = Math.min(firstPosition.blockZ(), secondPosition.blockZ());

        int maxX = Math.max(firstPosition.blockX(), secondPosition.blockX());
        int maxY = Math.max(firstPosition.blockY(), secondPosition.blockY());
        int maxZ = Math.max(firstPosition.blockZ(), secondPosition.blockZ());

        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                    instance.setBlock(x, y, z, block);
    }

}
