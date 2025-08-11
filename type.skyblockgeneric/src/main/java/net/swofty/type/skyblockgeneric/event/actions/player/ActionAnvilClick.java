package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIAnvil;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionAnvilClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerBlockInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!event.getBlock().name().equals("minecraft:anvil")) return;

        new GUIAnvil().open(player);
    }
}
