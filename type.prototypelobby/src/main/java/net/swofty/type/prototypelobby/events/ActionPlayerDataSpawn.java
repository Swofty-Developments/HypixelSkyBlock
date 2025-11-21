package net.swofty.type.prototypelobby.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.prototypelobby.PrototypeLobbyDataHandler;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        if (HypixelConst.getTypeLoader().getType().isSkyBlock()) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        Rank rank = player.getRank();
        if (rank == Rank.DEFAULT) return;

        for (HypixelPlayer onlinePlayer : HypixelGenericLoader.getLoadedPlayers()) {
            onlinePlayer.sendMessage(player.getFullDisplayName() + " §6joined the lobby!");
        }

        PrototypeLobbyDataHandler handler = PrototypeLobbyDataHandler.getUser(player.getUuid());
        handler.runOnLoad(player);
    }
}