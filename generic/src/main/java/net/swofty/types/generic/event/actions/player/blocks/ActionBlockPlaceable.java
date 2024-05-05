package net.swofty.types.generic.event.actions.player.blocks;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.block.impl.BlockPlaceable;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.PlaceableCustomSkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionBlockPlaceable implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void onPlace(PlayerBlockPlaceEvent event){
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.getGenericInstance() == null) return;

        Object instance = item.getGenericInstance();

        if (instance instanceof PlaceableCustomSkyBlockItem placeable){
            if (placeable.getAssociatedBlockType() == null) return;
            SkyBlockBlock skyBlockBlock = new SkyBlockBlock(placeable.getAssociatedBlockType());
            if (skyBlockBlock.getGenericInstance() instanceof BlockPlaceable blockPlaceable){
                blockPlaceable.onPlace(event , skyBlockBlock);
            }
        }
    }
}
