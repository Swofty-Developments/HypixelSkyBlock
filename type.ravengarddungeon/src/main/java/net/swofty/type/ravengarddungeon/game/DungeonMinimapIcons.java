package net.swofty.type.ravengarddungeon.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.swofty.dungeons.ravengard.RavengardDungeon.RoomPlacement;
import net.swofty.type.ravengardgeneric.hud.RavengardHudComposer;
import net.swofty.type.ravengardgeneric.hud.RavengardHudState;

import java.util.UUID;

public final class DungeonMinimapIcons {
    private static final double MAP_SCALE = 0.75;
    private static final double VISIBLE_RANGE_PIXELS = 124;
    private static final double TILE_SIZE_PIXELS = 13;
    private static final String ROOM_TILE = "";

    private DungeonMinimapIcons() {
    }

    public static void register() {
        RavengardHudComposer.dungeonMinimapIcons = DungeonMinimapIcons::render;
    }

    private static Component render(RavengardHudState state) {
        UUID uuid = state.getPlayerUuid();
        if (uuid == null) {
            return null;
        }
        DungeonInstanceRegistry.DungeonInstance dungeonInstance = null;
        for (DungeonInstanceRegistry.DungeonInstance candidate : DungeonInstanceRegistry.all()) {
            if (candidate.getPlayers().contains(uuid)) {
                dungeonInstance = candidate;
                break;
            }
        }
        if (dungeonInstance == null) {
            return null;
        }

        Component icons = Component.empty();
        for (RoomPlacement placement : dungeonInstance.getGenerated().dungeon().getPlacements()) {
            double leftPixels = (placement.originX() - state.getWorldX()) * MAP_SCALE;
            double topPixels = (placement.originZ() - state.getWorldZ()) * MAP_SCALE;
            double widthPixels = placement.getFootprintWidth() * MAP_SCALE;
            double depthPixels = placement.getFootprintDepth() * MAP_SCALE;
            if (leftPixels > VISIBLE_RANGE_PIXELS || leftPixels + widthPixels < -VISIBLE_RANGE_PIXELS
                    || topPixels > VISIBLE_RANGE_PIXELS || topPixels + depthPixels < -VISIBLE_RANGE_PIXELS) {
                continue;
            }
            int columns = Math.max(1, (int) Math.ceil(widthPixels / TILE_SIZE_PIXELS));
            int rowCount = Math.max(1, (int) Math.ceil(depthPixels / TILE_SIZE_PIXELS));
            for (int column = 0; column < columns; column++) {
                for (int row = 0; row < rowCount; row++) {
                    double centerX = leftPixels + (column + 0.5) * (widthPixels / columns);
                    double centerZ = topPixels + (row + 0.5) * (depthPixels / rowCount);
                    if (Math.abs(centerX) > VISIBLE_RANGE_PIXELS
                            || Math.abs(centerZ) > VISIBLE_RANGE_PIXELS) {
                        continue;
                    }
                    int packedX = clamp9((int) Math.round((centerX + 128) * 2));
                    int packedZ = clamp9((int) Math.round((centerZ + 128) * 2));
                    icons = icons.append(Component.text(ROOM_TILE)
                            .color(TextColor.color((packedX << 15) | (packedZ << 6))));
                }
            }
        }
        return icons;
    }

    private static int clamp9(int value) {
        return Math.clamp(value, 0, 0x1FF);
    }
}
