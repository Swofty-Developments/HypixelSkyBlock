package net.swofty.type.hub.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.SkyBlockEvent;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.gui.inventories.museum.GUIYourMuseum;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerClicksMuseumNPCDisplay implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true , isAsync = true)
    public void run(PlayerBlockInteractEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        Pos displayPosition = new Pos(-23, 67, 80);

        if (event.getBlockPosition().distance(displayPosition) > 2) return;

        new GUIYourMuseum().open(player);
    }
}
