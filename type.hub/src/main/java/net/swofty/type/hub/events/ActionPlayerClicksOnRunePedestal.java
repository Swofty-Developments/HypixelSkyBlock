package net.swofty.type.hub.events;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.hub.gui.GUIRunicPedestal;
import net.swofty.type.hub.runes.RuneEntityImpl;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClicksOnRunePedestal implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        if (!(event.getTarget() instanceof RuneEntityImpl)) return;

        new GUIRunicPedestal().open((SkyBlockPlayer) event.getPlayer());
    }
}
