package net.swofty.type.lobby.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.LobbyTypeLoader;
import net.swofty.type.lobby.item.LobbyItem;
import net.swofty.type.lobby.visibility.PlayerVisibilityManager;

import java.util.Map;

public class LobbyPlayerSpawnEvents implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (HypixelConst.getTypeLoader() instanceof LobbyTypeLoader lobbyLoader) {
            Map<Integer, LobbyItem> hotbarItems = lobbyLoader.getHotbarItems();
            for (Map.Entry<Integer, LobbyItem> entry : hotbarItems.entrySet()) {
                player.getInventory().setItemStack(
                    entry.getKey(),
                    entry.getValue().getItemStack(player)
                );
            }
        }

        PlayerVisibilityManager.setupViewerRuleFromToggle(player);
        player.setGameMode(GameMode.SURVIVAL);
    }
}