package net.swofty.type.skywarslobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.entity.GameMode;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.hologram.LeaderboardHologramManager;
import net.swofty.type.skywarslobby.hologram.SoulWellHologramManager;
import net.swofty.type.skywarslobby.util.SkywarsLobbyMap;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        // Send map data to player
        SkywarsLobbyMap skywarsLobbyMap = new SkywarsLobbyMap();
        skywarsLobbyMap.sendMapData(player);

        // Spawn leaderboard holograms for this player
        LeaderboardHologramManager.spawnHologramsForPlayer(player);

        // Spawn Soul Well hologram for this player
        SoulWellHologramManager.spawnHologramForPlayer(player);
    }
}
