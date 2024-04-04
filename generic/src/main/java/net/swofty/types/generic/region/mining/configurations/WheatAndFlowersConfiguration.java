package net.swofty.types.generic.region.mining.configurations;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.region.SkyBlockMiningConfiguration;
import net.swofty.types.generic.utility.groups.Groups;

import java.util.ArrayList;
import java.util.List;

public class WheatAndFlowersConfiguration extends SkyBlockMiningConfiguration {
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
    public List<Material> getMineableBlocks() {
        ArrayList<Material> materials = new ArrayList<>(Groups.FLOWERS);
        materials.add(Material.WHEAT);

        return materials;
    }

    @Override
    public long getRegenerationTime() {
        return 1;
    }
}
