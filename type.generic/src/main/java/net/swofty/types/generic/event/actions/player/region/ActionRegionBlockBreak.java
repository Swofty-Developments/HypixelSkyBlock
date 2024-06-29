package net.swofty.types.generic.event.actions.player.region;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.item.ItemDropChanger;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockMiningConfiguration;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.region.mining.MineableBlock;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;

public class ActionRegionBlockBreak implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerBlockBreakEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (player.isBypassBuild()) return;

        event.setCancelled(true);
        SkyBlockRegion region = SkyBlockRegion.getRegionOfPosition(event.getBlockPosition());

        Block block = event.getBlock();
        Material material = Material.fromNamespaceId(block.name());
        Boolean shouldItemDrop = false;

        if (SkyBlockConst.isIslandServer()) {
            event.getInstance().setBlock(event.getBlockPosition(), Block.AIR);
            shouldItemDrop = true;
        } else if (region != null) {
            RegionType type = region.getType();
            SkyBlockMiningConfiguration mining = type.getMiningHandler();

            if (material != null && mining != null && !mining.getMineableBlocks(player.getInstance(), event.getBlockPosition()).contains(material)) {
                event.setCancelled(true);
                return;
            }

            if (mining != null && material != null) {
                mining.addToQueue(player, Pos.fromPoint(event.getBlockPosition()), (SharedInstance) player.getInstance());
                shouldItemDrop = true;
            }
        } else {
            event.setCancelled(true);
            return;
        }

        if (material != null && shouldItemDrop) {
            SkyBlockItem item;
            if (ItemDropChanger.get(material) != null) {
                item = ItemDropChanger.get(material).getItemSupplier().get();
            } else {
                item = new SkyBlockItem(material);
            }

            SkyBlockEventHandler.callSkyBlockEvent(new CustomBlockBreakEvent(
                    player, item.getMaterial(), event.getBlockPosition()
            ));

            MineableBlock mineableBlock;
            Integer dropAmount;
            
            try {
                mineableBlock = MineableBlock.get(block);
                Double fortune = player.getStatistics().allStatistics().getOverall(mineableBlock.getFortuneType());
                Double dropMultiplicator = (1 + (fortune*0.01));
                dropAmount = mineableBlock.getDrops().getAmount(dropMultiplicator);
            } catch (NullPointerException e) {
                dropAmount = 1;
            }

            /**
             * Handle block dropping
             */
            SkyBlockItem skyBlockItem = new SkyBlockItem(item.getItemStackBuilder().amount(dropAmount).build());
            if (player.getSkyBlockExperience().getLevel().asInt() >= 6) {
                player.addAndUpdateItem(skyBlockItem);
            } else {
                DroppedItemEntityImpl droppedItem = new DroppedItemEntityImpl(skyBlockItem, player);
                Pos pos = Pos.fromPoint(event.getBlockPosition());
                // Move the dropped item to the center of the block
                pos = pos.add(0.5, 0.5, 0.5);

                droppedItem.setInstance(player.getInstance(), pos);
                droppedItem.addViewer(player);
            }
        }
        shouldItemDrop = false;
    }
}

