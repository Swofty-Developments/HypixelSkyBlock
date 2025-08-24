package net.swofty.type.island.events.traditional;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.entity.MinionEntityImpl;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIMinion;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickMinion implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof MinionEntityImpl minion) {
            new GUIMinion(minion.getIslandMinion()).open(player);
        }
    }
}
