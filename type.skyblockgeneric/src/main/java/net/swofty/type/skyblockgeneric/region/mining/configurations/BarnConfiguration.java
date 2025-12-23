package net.swofty.type.skyblockgeneric.region.mining.configurations;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegenConfiguration;

import java.util.List;

public class BarnConfiguration extends SkyBlockRegenConfiguration {
    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        task.setIntermediaryBlock(Block.AIR);
        task.setReviveBlock(Block.AIR);

        return task;
    }

    @Override
    public MiningTask handleStageTwo(MiningTask task, Pos brokenBlock) {
        return MiningTask.never();
    }

    @Override
    public List<Material> getMineableBlocks(Instance instance, Point point) {
        return List.of(Material.WHEAT, Material.CARVED_PUMPKIN, Material.MELON, Material.POTATO, Material.CARROT);
    }

    @Override
    public long getRegenerationTime() {
        return 1;
    }
}
