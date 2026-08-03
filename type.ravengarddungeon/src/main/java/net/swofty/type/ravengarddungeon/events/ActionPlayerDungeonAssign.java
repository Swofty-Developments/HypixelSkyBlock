package net.swofty.type.ravengarddungeon.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.redis.service.GameInformationHandler;
import net.swofty.type.ravengarddungeon.game.DungeonInstanceRegistry;
import net.swofty.type.ravengarddungeon.user.RavengardDungeonPlayer;

import java.util.UUID;

public class ActionPlayerDungeonAssign implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onSpawn(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn() || !(event.getPlayer() instanceof RavengardDungeonPlayer player)) {
            return;
        }
        String assignedGameId = GameInformationHandler.game.remove(player.getUuid());
        if (assignedGameId == null) {
            return;
        }
        DungeonInstanceRegistry.DungeonInstance instance;
        try {
            instance = DungeonInstanceRegistry.get(UUID.fromString(assignedGameId));
        } catch (IllegalArgumentException exception) {
            return;
        }
        if (instance == null) {
            player.sendMessage("§cYour dungeon instance no longer exists.");
            return;
        }
        instance.markPlayerJoined(player.getUuid());
        player.sendMessage("§7Preparing your dungeon (seed §f" + instance.getSeed() + "§7)...");
        boolean aerial = instance.getMode().equals("ADMIN");
        instance.whenReady().thenRun(() -> player.scheduler().scheduleNextTick(() -> {
            Pos spawn;
            if (aerial) {
                double[] center = boundsCenter(instance);
                spawn = new Pos(center[0], 140, center[1], 0, 90);
                player.setGameMode(net.minestom.server.entity.GameMode.CREATIVE);
            } else {
                spawn = instance.getGenerated().spawn().withY(67);
            }
            player.setInstance(instance.getInstance(), spawn);
            player.sendMessage("§aEntered dungeon §f" + instance.getGameId().toString().substring(0, 8)
                    + "§a (" + instance.getGenerated().dungeon().getRoomCount() + " rooms, mode "
                    + instance.getMode() + ").");
        }));
    }

    private static double[] boundsCenter(DungeonInstanceRegistry.DungeonInstance instance) {
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
        for (var placement : instance.getGenerated().dungeon().getPlacements()) {
            minX = Math.min(minX, placement.originX());
            maxX = Math.max(maxX, placement.originX() + placement.getFootprintWidth());
            minZ = Math.min(minZ, placement.originZ());
            maxZ = Math.max(maxZ, placement.originZ() + placement.getFootprintDepth());
        }
        return new double[]{(minX + maxX) / 2.0, (minZ + maxZ) / 2.0};
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void onDisconnect(PlayerDisconnectEvent event) {
        UUID uuid = event.getPlayer().getUuid();
        for (DungeonInstanceRegistry.DungeonInstance instance : DungeonInstanceRegistry.all()) {
            instance.markPlayerLeft(uuid);
        }
    }
}
