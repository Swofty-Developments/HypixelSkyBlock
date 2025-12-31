package net.swofty.type.murdermysterylobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.minestom.server.entity.GameMode;
import net.swofty.type.murdermysterylobby.TypeMurderMysteryLobbyLoader;
import net.swofty.type.murdermysterylobby.hologram.LeaderboardHologramManager;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        // Spawn leaderboard holograms for this player
        LeaderboardHologramManager.spawnHologramsForPlayer(player);
        TypeMurderMysteryLobbyLoader.lobbyMap.sendMapData(player);
    }
}
