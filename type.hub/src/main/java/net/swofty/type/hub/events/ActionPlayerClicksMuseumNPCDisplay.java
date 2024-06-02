package net.swofty.type.hub.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.inventory.inventories.museum.GUIYourMuseum;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerClicksMuseumNPCDisplay implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true , isAsync = true)
    public void run(PlayerBlockInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        Pos displayPosition = new Pos(-23, 67, 80);

        if (event.getBlockPosition().distance(displayPosition) > 2) return;

        new GUIYourMuseum().open(player);
    }
}
