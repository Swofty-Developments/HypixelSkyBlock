package net.swofty.types.generic.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.PlaceEvent;
import net.swofty.types.generic.item.impl.PlaceableCustomSkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionItemPlace implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerBlockPlaceEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.getGenericInstance() == null) return;

        Object instance = item.getGenericInstance();

        if (instance instanceof PlaceEvent placeEvent) {
            placeEvent.onPlace(event, player, item);
            return;
        }

        if (instance instanceof CustomSkyBlockItem && !((CustomSkyBlockItem) instance).isPlaceable()) {
            event.setCancelled(true);
        } else {
            PlaceableCustomSkyBlockItem placeableItem = (PlaceableCustomSkyBlockItem) instance;
            if (placeableItem.getAssociatedBlockType() != null) {
                event.setBlock(new SkyBlockBlock(placeableItem.getAssociatedBlockType()).toBlock());
            }
        }
    }
}
