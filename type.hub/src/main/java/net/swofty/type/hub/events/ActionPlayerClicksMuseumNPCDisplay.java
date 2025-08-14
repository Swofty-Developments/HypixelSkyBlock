package net.swofty.type.hub.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.gui.inventories.museum.GUIYourMuseum;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClicksMuseumNPCDisplay implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true , isAsync = true)
    public void run(PlayerBlockInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        Pos displayPosition = new Pos(-23, 67, 80);

        if (event.getBlockPosition().distance(displayPosition) > 2) return;

        new GUIYourMuseum().open(player);
    }
}
