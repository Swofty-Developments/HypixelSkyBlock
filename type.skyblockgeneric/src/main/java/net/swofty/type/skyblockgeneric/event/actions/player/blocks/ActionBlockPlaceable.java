package net.swofty.type.skyblockgeneric.event.actions.player.blocks;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockPlaceable;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PlaceableComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionBlockPlaceable implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void onPlace(PlayerBlockPlaceEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.toConfigurableItem() == null) return;

        if (item.hasComponent(PlaceableComponent.class)) {
            PlaceableComponent placeable = item.getComponent(PlaceableComponent.class);
            if (placeable.getBlockType() == null) return;
            SkyBlockBlock skyBlockBlock = new SkyBlockBlock(placeable.getBlockType());
            if (skyBlockBlock.getGenericInstance() instanceof BlockPlaceable blockPlaceable) {
                blockPlaceable.onPlace(event, skyBlockBlock);
            }
        }
    }
}
