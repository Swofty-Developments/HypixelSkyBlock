package net.swofty.type.village.events;

import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.gui.inventory.inventories.museum.GUIMuseumNonEmptyDisplay;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumDisplayEntityImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Player clicks on a museum display",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerClickMuseumDisplay extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerEntityInteractEvent event = (PlayerEntityInteractEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof MuseumDisplayEntityImpl museumDisplayEntity) {
            boolean isEmpty = museumDisplayEntity.isEmpty();

            if (isEmpty) {

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
