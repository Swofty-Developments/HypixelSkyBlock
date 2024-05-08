package net.swofty.type.hub.events;

import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.inventory.inventories.museum.GUIMuseumEmptyDisplay;
import net.swofty.types.generic.gui.inventory.inventories.museum.GUIMuseumNonEmptyDisplay;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumDisplayEntityImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerClickMuseumDisplay implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof MuseumDisplayEntityImpl museumDisplayEntity) {
            boolean isEmpty = museumDisplayEntity.isEmpty();

            if (isEmpty) {
                new GUIMuseumEmptyDisplay(museumDisplayEntity.getDisplay(),
                        museumDisplayEntity.getPositionInMuseum()).open(player);
            } else {
                ItemDisplayMeta itemDisplayMeta = (ItemDisplayMeta) museumDisplayEntity.getEntityMeta();
                SkyBlockItem item = new SkyBlockItem(itemDisplayMeta.getItemStack());
                new GUIMuseumNonEmptyDisplay(item,
                        museumDisplayEntity.getDisplay(),
                        museumDisplayEntity.getPositionInMuseum()).open(player);
            }
        }
    }
}
