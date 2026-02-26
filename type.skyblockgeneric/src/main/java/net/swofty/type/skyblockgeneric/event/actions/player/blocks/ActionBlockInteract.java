package net.swofty.type.skyblockgeneric.event.actions.player.blocks;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockInteractable;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.BlockDataToolComponent;
import net.swofty.type.skyblockgeneric.item.components.RegionSelectorComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionBlockInteract implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void onInteract(PlayerBlockInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        ItemStack stack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(stack);

        if (item.hasComponent(RegionSelectorComponent.class)) {
            RegionSelectorComponent.SelectedRegion region = RegionSelectorComponent.getPlayerRegionSelection().get(player);
            if (region == null) {
                region = new RegionSelectorComponent.SelectedRegion(null, null);
            }
            region.setPos2(event.getBlockPosition());
            RegionSelectorComponent.getPlayerRegionSelection().put(player, region);
            player.sendMessage("§aPosition 2 set to §e" + event.getBlockPosition() + "§a.");
            event.setCancelled(true);
            return;
        }

        if (item.hasComponent(BlockDataToolComponent.class)) {
            player.sendMessage("§aBlock data for §e" + event.getBlockPosition() + "§a:");
            player.sendMessage("§e- Block ID: §f" + event.getBlock().registry().material());
            player.sendMessage("§e- Block State: §f" + event.getBlock().state());
            event.setCancelled(true);
            return;
        }

        if (!SkyBlockBlock.isSkyBlockBlock(event.getBlock())) return;

        SkyBlockBlock block = new SkyBlockBlock(event.getBlock());

        if (block.getGenericInstance() instanceof BlockInteractable interactable) {
            interactable.onInteract(event, block);
        }
    }
}
