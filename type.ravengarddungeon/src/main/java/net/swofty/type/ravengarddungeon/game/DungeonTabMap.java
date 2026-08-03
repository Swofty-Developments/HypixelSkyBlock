package net.swofty.type.ravengarddungeon.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.swofty.type.ravengardgeneric.hud.RavengardHudComposer;
import net.swofty.type.ravengardgeneric.hud.RavengardHudState;

import java.util.List;
import java.util.UUID;

public final class DungeonTabMap {
    private static final double MAP_SCALE = 0.75;
    private static final double BLOCKS_PER_TILE = 13 / MAP_SCALE;
    private static final int MAP_CENTER = 256;
    private static final String TILE_UNSEEN = "";
    private static final String TILE_SEEN = "";
    private static final String TILE_HERE = "";
    private static final String PLAYER_MARKER = "";
    private static final String TEAMMATE_MARKER = "";
    private static final double YAW_PER_STEP = 360.0 / 64.0;

    private DungeonTabMap() {
    }

    public static void register() {
        RavengardHudComposer.dungeonTabMap = DungeonTabMap::render;
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

        List<DungeonInstanceRegistry.DungeonInstance.MapTile> tiles =
                dungeonInstance.getMapTiles(BLOCKS_PER_TILE);
        if (tiles.isEmpty()) {
            return null;
        }
        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
        for (DungeonInstanceRegistry.DungeonInstance.MapTile tile : tiles) {
            minX = Math.min(minX, tile.worldX());
            maxX = Math.max(maxX, tile.worldX());
            minZ = Math.min(minZ, tile.worldZ());
            maxZ = Math.max(maxZ, tile.worldZ());
        }
        double centerX = (minX + maxX) / 2, centerZ = (minZ + maxZ) / 2;

        int currentRoom = dungeonInstance.roomIndexAt(state.getWorldX(), state.getWorldZ());
        Component header = Component.empty();
        for (DungeonInstanceRegistry.DungeonInstance.MapTile tile : tiles) {
            String glyph = tile.roomIndex() == currentRoom ? TILE_HERE
                    : dungeonInstance.getVisitedRooms().contains(tile.roomIndex()) ? TILE_SEEN
                    : TILE_UNSEEN;
            header = header.append(Component.text(glyph)
                    .color(TextColor.color(packed(tile.worldX(), tile.worldZ(), centerX, centerZ, 0)))
                    .shadowColor(ShadowColor.shadowColor(0)));
        }
        header = header.append(Component.newline());

        for (UUID memberId : dungeonInstance.getPlayers()) {
            Player member = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(memberId);
            if (member == null) {
                continue;
            }
            boolean self = memberId.equals(uuid);
            float yaw = member.getPosition().yaw();
            while (yaw < 0) {
                yaw += 360f;
            }
            int rotation = ((int) Math.round(yaw / YAW_PER_STEP)) & 0x3F;
            header = header.append(Component.text(self ? PLAYER_MARKER : TEAMMATE_MARKER)
                    .color(TextColor.color(packed(member.getPosition().x(), member.getPosition().z(),
                            centerX, centerZ, rotation)))
                    .shadowColor(ShadowColor.shadowColor(0)));
        }
        return header.append(Component.newline());
    }

    private static int packed(double worldX, double worldZ, double centerX, double centerZ, int rotation) {
        int mapX = clamp9((int) Math.round(MAP_CENTER + (worldX - centerX) * MAP_SCALE));
        int mapZ = clamp9((int) Math.round(MAP_CENTER + (worldZ - centerZ) * MAP_SCALE));
        return (mapX << 15) | (mapZ << 6) | rotation;
    }

    private static int clamp9(int value) {
        return Math.clamp(value, 0, 0x1FF);
    }
}
