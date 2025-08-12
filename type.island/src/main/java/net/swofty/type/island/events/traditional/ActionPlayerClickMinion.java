package net.swofty.type.island.events.traditional;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.entity.MinionEntityImpl;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.SkyBlockEvent;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.gui.inventories.GUIMinion;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerClickMinion implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (event.getTarget() instanceof MinionEntityImpl minion) {
            new GUIMinion(minion.getIslandMinion()).open(player);
        }
    }
}
