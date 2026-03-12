package net.swofty.type.skyblockgeneric.garden;

import net.minestom.server.coordinate.Point;

public interface SkyBlockEditableWorldHandle {
    WorldBuildLimits getBuildLimits();

    default boolean isWithinBounds(Point point) {
        return getBuildLimits().contains(point);
    }

    default boolean canEdit(Point point) {
        return isWithinBounds(point);
    }

    default String getDeniedBuildMessage(Point point) {
        if (!isWithinBounds(point)) {
            return "§cYou can't build any further in this direction!";
        }
        return "§cYou can't edit that part yet!";
    }
}
