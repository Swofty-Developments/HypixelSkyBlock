package net.swofty.type.ravengarddungeon.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import net.swofty.type.ravengardgeneric.hud.RavengardHudComposer;
import net.swofty.type.ravengardgeneric.hud.RavengardHudState;

import java.util.UUID;

public final class DungeonMinimapIcons {
    private static final int TILE = 13;
    private static final double VISIBLE_RANGE_PIXELS = 136;

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
        var placements = dungeonInstance.getGenerated().dungeon().getPlacements();
        for (int index = 0; index < placements.size(); index++) {
            if (!dungeonInstance.getVisitedRooms().contains(index)) {
                continue;
            }
            var placement = placements.get(index);
            DungeonMapGlyphs.RoomArt art = DungeonMapGlyphs.artFor(
                    placement.room().getId(), placement.rotation().getDegrees());
            if (art == null) {
                continue;
            }
            for (int row = 0; row < art.rows(); row++) {
                for (int col = 0; col < art.cols(); col++) {
                    Integer codepoint = art.tiles().get(row).get(col);
                    if (codepoint == null) {
                        continue;
                    }
                    double tileCenterX = placement.originX() + col * TILE + TILE / 2.0;
                    double tileCenterZ = placement.originZ() + row * TILE + TILE / 2.0;
                    double offsetX = tileCenterX - state.getWorldX();
                    double offsetZ = tileCenterZ - state.getWorldZ();
                    if (Math.abs(offsetX) > VISIBLE_RANGE_PIXELS
                            || Math.abs(offsetZ) > VISIBLE_RANGE_PIXELS) {
                        continue;
                    }
                    int packedX = clamp9((int) Math.round((offsetX + 128) * 2));
                    int packedZ = clamp9((int) Math.round((offsetZ + 128) * 2));
                    icons = icons.append(Component.text(Character.toString(codepoint))
                            .color(TextColor.color((packedX << 15) | (packedZ << 6)))
                            .shadowColor(ShadowColor.shadowColor(0)));
                }
            }
        }
        return icons;
    }

    private static int clamp9(int value) {
        return Math.clamp(value, 0, 0x1FF);
    }
}
