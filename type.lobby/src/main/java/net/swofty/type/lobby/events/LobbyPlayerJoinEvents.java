package net.swofty.type.lobby.events;

import lombok.SneakyThrows;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.LobbyTypeLoader;
import net.swofty.type.lobby.item.LobbyItem;
import net.swofty.type.lobby.visibility.PlayerVisibilityManager;
import org.tinylog.Logger;

import java.util.Map;

/**
 * Shared player join event handler for all lobby types.
 * Handles spawning, hotbar setup, and visibility rules.
 */
public class LobbyPlayerJoinEvents implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        event.setSpawningInstance(HypixelConst.getInstanceContainer());
        Logger.info("Player " + player.getUsername() + " joined the server from origin server " + player.getOriginServer());
        player.setRespawnPoint(HypixelConst.getTypeLoader()
                .getLoaderValues()
                .spawnPosition()
                .apply(player.getOriginServer())
        );

        // Set up hotbar items from lobby config
        if (HypixelConst.getTypeLoader() instanceof LobbyTypeLoader lobbyLoader) {
            Map<Integer, LobbyItem> hotbarItems = lobbyLoader.getHotbarItems();
            for (Map.Entry<Integer, LobbyItem> entry : hotbarItems.entrySet()) {
                player.getInventory().setItemStack(
                        entry.getKey(),
                        entry.getValue().getItemStack(player)
                );
            }
        }

        // Set up visibility based on player's toggle setting
        PlayerVisibilityManager.setupViewerRuleFromToggle(player);
        player.setGameMode(GameMode.SURVIVAL);
    }
}
