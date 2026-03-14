package net.swofty.type.hub.events;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.hub.gui.GUIRunicPedestal;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClicksOnRunePedestal implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerBlockInteractEvent event) {
        if (!event.getBlockPosition().equals(new BlockVec(23, 64, -135))) return;

        new GUIRunicPedestal().open((SkyBlockPlayer) event.getPlayer());
    }
}
