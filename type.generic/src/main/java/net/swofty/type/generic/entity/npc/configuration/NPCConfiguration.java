package net.swofty.type.generic.entity.npc.configuration;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityPose;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

public interface NPCConfiguration {

    String[] holograms(HypixelPlayer player);

    Pos position(HypixelPlayer player);

    default Pos resolvedPosition(HypixelPlayer player) {
        Pos targetPosition = position(player);

        if (HypixelConst.getTypeLoader() == null
                || HypixelConst.getTypeLoader().getType() != ServerType.SKYBLOCK_HUB) {
            return targetPosition;
        }

        Instance instance = instance();
        if (instance == null || !instance.isChunkLoaded(targetPosition)) {
            return targetPosition;
        }

        if (isWalkable(instance, targetPosition.blockX(), targetPosition.blockY(), targetPosition.blockZ())) {
            return targetPosition;
        }

        int snappedY = findNearestWalkableY(instance, targetPosition.blockX(), targetPosition.blockY(), targetPosition.blockZ());
        if (snappedY == Integer.MIN_VALUE) {
            return targetPosition;
        }

        return targetPosition.withY(snappedY);
    }

    default boolean looking(HypixelPlayer player) {
        return false;
    }

    default boolean visible(HypixelPlayer player) {
        return true;
    }

    @Nullable
    default String chatName() {
        return null;
    }

    default Instance instance() {
        return HypixelConst.getInstanceContainer();
    }

    default EntityPose pose(HypixelPlayer player) {
        return EntityPose.STANDING;
    }

    default boolean shouldDisplayHolograms(HypixelPlayer player) {
        return true;
    }

    private static int findNearestWalkableY(Instance instance, int x, int y, int z) {
        final int maxDistance = 96;

        for (int offset = 1; offset <= maxDistance; offset++) {
            int below = y - offset;
            if (below >= 1 && isWalkable(instance, x, below, z)) {
                return below;
            }

            int above = y + offset;
            if (above <= 255 && isWalkable(instance, x, above, z)) {
                return above;
            }
        }

        return Integer.MIN_VALUE;
    }

    private static boolean isWalkable(Instance instance, int x, int y, int z) {
        if (y <= 0 || y >= 255) {
            return false;
        }

        Block below = instance.getBlock(x, y - 1, z);
        Block feet = instance.getBlock(x, y, z);
        Block head = instance.getBlock(x, y + 1, z);

        return below.isSolid()
                && !feet.isSolid()
                && !feet.isLiquid()
                && !head.isSolid()
                && !head.isLiquid();
    }
}
