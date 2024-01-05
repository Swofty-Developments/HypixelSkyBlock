package net.swofty.region.mining.configurations;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.region.SkyBlockMiningConfiguration;

import java.util.List;

public class MineWheatConfiguration extends SkyBlockMiningConfiguration {
    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        task.setIntermediaryBlock(Block.AIR);
        task.setReviveBlock(Block.WHEAT);

        return task;
    }

    @Override
    public MiningTask handleStageTwo(MiningTask task, Pos brokenBlock) {
        return MiningTask.never();
    }

    @Override
    public List<Material> getMineableBlocks() {
        return List.of(Material.WHEAT);
    }

    @Override
    public long getRegenerationTime() {
        return 3000;
    }
}
