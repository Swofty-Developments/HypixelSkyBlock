package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.inventories.GUIAnvil;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionAnvilClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerBlockInteractEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (!event.getBlock().name().equals("minecraft:anvil")) return;

        new GUIAnvil().open(player);
    }
}
