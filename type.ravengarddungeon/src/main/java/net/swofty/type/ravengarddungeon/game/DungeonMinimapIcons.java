package net.swofty.type.ravengarddungeon.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.swofty.type.ravengardgeneric.hud.RavengardHudComposer;
import net.swofty.type.ravengardgeneric.hud.RavengardHudState;

import java.util.UUID;

public final class DungeonMinimapIcons {
    private static final double MAP_SCALE = 0.75;
    private static final double VISIBLE_RANGE_PIXELS = 124;
    private static final double BLOCKS_PER_TILE = 13 / MAP_SCALE;
    private static final String TILE_UNSEEN = "\uE131";
    private static final String TILE_SEEN = "\uE132";
    private static final String TILE_HERE = "\uE133";

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

        int currentRoom = dungeonInstance.roomIndexAt(state.getWorldX(), state.getWorldZ());
        if (currentRoom >= 0) {
            dungeonInstance.getVisitedRooms().add(currentRoom);
        }

        Component icons = Component.empty();
        for (DungeonInstanceRegistry.DungeonInstance.MapTile tile
                : dungeonInstance.getMapTiles(BLOCKS_PER_TILE)) {
            double offsetX = (tile.worldX() - state.getWorldX()) * MAP_SCALE;
            double offsetZ = (tile.worldZ() - state.getWorldZ()) * MAP_SCALE;
            if (Math.abs(offsetX) > VISIBLE_RANGE_PIXELS || Math.abs(offsetZ) > VISIBLE_RANGE_PIXELS) {
                continue;
            }
            String glyph = tile.roomIndex() == currentRoom ? TILE_HERE
                    : dungeonInstance.getVisitedRooms().contains(tile.roomIndex()) ? TILE_SEEN
                    : TILE_UNSEEN;
            int packedX = clamp9((int) Math.round((offsetX + 128) * 2));
            int packedZ = clamp9((int) Math.round((offsetZ + 128) * 2));
            icons = icons.append(Component.text(glyph)
                    .color(TextColor.color((packedX << 15) | (packedZ << 6))));
        }
        return icons;
    }

    private static int clamp9(int value) {
        return Math.clamp(value, 0, 0x1FF);
    }
}
