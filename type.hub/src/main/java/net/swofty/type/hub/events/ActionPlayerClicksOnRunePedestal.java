package net.swofty.type.hub.events;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.hub.gui.GUIRunicPedestal;
import net.swofty.type.hub.runes.RuneEntityImpl;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.SkyBlockEvent;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerClicksOnRunePedestal implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        if (!(event.getTarget() instanceof RuneEntityImpl)) return;

        new GUIRunicPedestal().open((HypixelPlayer) event.getPlayer());
    }
}
