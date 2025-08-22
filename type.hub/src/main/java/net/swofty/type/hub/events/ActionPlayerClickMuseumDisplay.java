package net.swofty.type.hub.events;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.gui.inventories.museum.GUIMuseumEmptyDisplay;
import net.swofty.type.skyblockgeneric.gui.inventories.museum.GUIMuseumNonEmptyDisplay;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplayEntityImpl;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickMuseumDisplay implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

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
