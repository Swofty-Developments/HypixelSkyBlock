package net.swofty.type.hub.events;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.hub.gui.GUIRunicPedestal;
import net.swofty.type.hub.runes.RuneEntityImpl;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerClicksOnRunePedestal implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        if (!(event.getTarget() instanceof RuneEntityImpl)) return;

        new GUIRunicPedestal().open((SkyBlockPlayer) event.getPlayer());
    }
}
