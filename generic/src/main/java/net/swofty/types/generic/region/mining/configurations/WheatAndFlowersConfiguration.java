package net.swofty.types.generic.region.mining.configurations;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.mongodb.CrystalDatabase;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.region.SkyBlockMiningConfiguration;
import net.swofty.types.generic.utility.groups.Groups;

import java.util.ArrayList;
import java.util.Collections;
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
    public List<Material> getMineableBlocks(Instance instance, Point point) {
        ArrayList<Material> materials = new ArrayList<>(Collections.singletonList(Material.WHEAT));

        if (CrystalDatabase.getFromAround(SkyBlockConst.getTypeLoader().getType(), Pos.fromPoint(point), 15)
                .stream().anyMatch(crystal -> crystal.itemType == ItemType.FLOWER_CRYSTAL)) {
            materials.addAll(new ArrayList<>(Groups.FLOWERS));
        }

        return materials;
    }

    @Override
    public long getRegenerationTime() {
        return 1;
    }
}
