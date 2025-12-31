package net.swofty.type.murdermysteryconfigurator.events;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        // Send welcome message
        player.sendMessage(Component.text("§6§l=== Murder Mystery Configurator ==="));
        player.sendMessage(Component.text("§eUse §b/choosemap <mapId> §eto load a map"));
        player.sendMessage(Component.text("§eUse §b/mmsetup §eto configure the map"));
        player.sendMessage(Component.text("§7Anvil worlds will be automatically converted to Polar format"));
    }
}
