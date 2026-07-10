package net.swofty.type.skyblockgeneric.region.mining.configurations;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegenConfiguration;

import java.util.List;

public class GalateaForagingConfiguration extends SkyBlockRegenConfiguration {
    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        return MiningTask.never();
    }

    @Override
    public MiningTask handleStageTwo(MiningTask task, Pos brokenBlock) {
        return MiningTask.never();
    }

    @Override
    public List<Material> getMineableBlocks(Instance instance, Point point) {
        return List.of(
                Material.STRIPPED_SPRUCE_LOG,
                Material.STRIPPED_SPRUCE_WOOD,
                Material.MANGROVE_LOG,
                Material.MANGROVE_WOOD
        );
    }

    @Override
    public long getRegenerationTime() {
        return 5000;
    }
}
