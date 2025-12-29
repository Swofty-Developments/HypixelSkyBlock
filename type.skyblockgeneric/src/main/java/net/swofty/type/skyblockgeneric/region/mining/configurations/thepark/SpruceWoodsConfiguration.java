package net.swofty.type.skyblockgeneric.region.mining.configurations.thepark;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegenConfiguration;

import java.util.List;

public class SpruceWoodsConfiguration extends SkyBlockRegenConfiguration {
    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        Block atPosition = task.getInstance().getBlock(brokenBlock);

        task.setIntermediaryBlock(Block.AIR);
        task.setReviveBlock(atPosition);

        return task;
    }

    @Override
    public MiningTask handleStageTwo(MiningTask task, Pos brokenBlock) {
        return MiningTask.never();
    }

    @Override
    public List<Material> getMineableBlocks(Instance instance, Point point) {
        return List.of(Material.SPRUCE_LOG, Material.SPRUCE_LEAVES, Material.SPRUCE_WOOD);
    }

    @Override
    public long getRegenerationTime() {
        return 5000;
    }
}