package net.swofty.type.hub.events;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.SkyBlockEvent;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.gui.inventories.museum.GUIMuseumEmptyDisplay;
import net.swofty.type.generic.gui.inventories.museum.GUIMuseumNonEmptyDisplay;
import net.swofty.type.generic.museum.MuseumDisplayEntityImpl;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerClickMuseumDisplay implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (event.getTarget() instanceof MuseumDisplayEntityImpl museumDisplayEntity) {
            boolean isEmpty = museumDisplayEntity.isEmpty();

            if (isEmpty) {
                new GUIMuseumEmptyDisplay(
                        museumDisplayEntity.getDisplay(),
                        museumDisplayEntity.getPositionInMuseum()
                ).open(player);
            } else {
                new GUIMuseumNonEmptyDisplay(
                        museumDisplayEntity.getDisplay(),
                        museumDisplayEntity.getPositionInMuseum()
                ).open(player);
            }
        }
    }
}
