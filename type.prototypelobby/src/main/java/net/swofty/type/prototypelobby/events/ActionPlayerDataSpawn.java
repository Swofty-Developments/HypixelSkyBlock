package net.swofty.type.prototypelobby.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

/**
 * Sends rank-based join messages when players spawn in the prototype lobby.
 * Note: Data loading is handled by the generic ActionPlayerDataSpawn via GameDataHandler.
 */
public class ActionPlayerDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        // Send rank join message for non-default ranks
        Rank rank = player.getRank();
        if (rank == Rank.DEFAULT) return;

        for (HypixelPlayer onlinePlayer : HypixelGenericLoader.getLoadedPlayers()) {
            onlinePlayer.sendMessage(player.getFullDisplayName() + " §6joined the lobby!");
        }
    }
}