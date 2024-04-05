package net.swofty.types.generic.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.PlaceEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Handles item placeable",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionItemPlace extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockPlaceEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event tempEvent) {
        PlayerBlockPlaceEvent event = (PlayerBlockPlaceEvent) tempEvent;
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.getGenericInstance() != null && item.getGenericInstance() instanceof PlaceEvent placeable) {
            if (!((CustomSkyBlockItem) item.getGenericInstance()).isPlaceable()) {
                event.setCancelled(true);
                return;
            }

            placeable.onPlace(event, player, item);
        }
    }
}
