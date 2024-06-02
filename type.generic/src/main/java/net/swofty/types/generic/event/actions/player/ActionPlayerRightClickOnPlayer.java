package net.swofty.types.generic.event.actions.player;


import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.inventory.inventories.GUIViewPlayerProfile;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerRightClickOnPlayer implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof SkyBlockPlayer skyBlockPlayer) {
            new GUIViewPlayerProfile(skyBlockPlayer).open(player);
        }
    }
}
