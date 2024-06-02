package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.inventory.inventories.GUIAnvil;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionAnvilClick implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerBlockInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!event.getBlock().name().equals("minecraft:anvil")) return;

        new GUIAnvil().open(player);
    }
}
