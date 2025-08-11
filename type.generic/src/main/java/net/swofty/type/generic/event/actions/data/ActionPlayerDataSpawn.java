package net.swofty.type.generic.event.actions.data;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        if (HypixelConst.getTypeLoader().getType().isSkyBlock()) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        Logger.info("Loading basic Hypixel data for spawned player: " + event.getPlayer().getUsername() + "...");

        HypixelDataHandler handler = player.getDataHandler();

        // Run onLoad callbacks for basic Hypixel functionality
        handler.runOnLoad(player);

        player.sendMessage("§aWelcome to " + HypixelConst.getTypeLoader().getType().formatName() + "!");

        Logger.info("Successfully loaded basic Hypixel data for: " + player.getUsername());
    }
}