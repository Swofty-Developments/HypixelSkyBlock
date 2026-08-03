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
    private static final String ROOM_ICON = "";
    private static final String CURRENT_ROOM_ICON = "";

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
            double centerX = placement.originX() + placement.getFootprintWidth() / 2.0;
            double centerZ = placement.originZ() + placement.getFootprintDepth() / 2.0;
            double offsetX = (centerX - state.getWorldX()) * MAP_SCALE;
            double offsetZ = (centerZ - state.getWorldZ()) * MAP_SCALE;
            if (Math.abs(offsetX) > VISIBLE_RANGE_PIXELS || Math.abs(offsetZ) > VISIBLE_RANGE_PIXELS) {
                continue;
            }
            boolean inside = state.getWorldX() >= placement.originX()
                    && state.getWorldX() < placement.originX() + placement.getFootprintWidth()
                    && state.getWorldZ() >= placement.originZ()
                    && state.getWorldZ() < placement.originZ() + placement.getFootprintDepth();
            int packedX = clamp9((int) Math.round((offsetX + 128) * 2));
            int packedZ = clamp9((int) Math.round((offsetZ + 128) * 2));
            icons = icons.append(Component.text(inside ? CURRENT_ROOM_ICON : ROOM_ICON)
                    .color(TextColor.color((packedX << 15) | (packedZ << 6))));
        }
        return icons;
    }

    private static int clamp9(int value) {
        return Math.clamp(value, 0, 0x1FF);
    }
}
