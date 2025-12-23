package net.swofty.type.skyblockgeneric.region.mining.configurations.deepmines;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegenConfiguration;

import java.util.List;

public class ObsidianSanctuaryConfiguration extends SkyBlockRegenConfiguration {

    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        task.setIntermediaryBlock(Block.COBBLESTONE);

        Block regenerationBlock;

        regenerationBlock = getRandomBlock(
                new RegenerationConfig(10, Block.DIAMOND_ORE),
                new RegenerationConfig(10, Block.DIAMOND_BLOCK),
                new RegenerationConfig(10, Block.OBSIDIAN),
                new RegenerationConfig(70, Block.STONE));

        task.setReviveBlock(regenerationBlock);

        return task;
    }

    @Override
    public MiningTask handleStageTwo(MiningTask task, Pos brokenBlock) {
        task.setIntermediaryBlock(Block.BEDROCK);
        task.setReviveBlock(Block.COBBLESTONE);

        return task;
    }

    @Override
    public List<Material> getMineableBlocks(Instance instance, Point point) {
        return List.of(Material.DIAMOND_ORE, Material.COBBLESTONE, Material.STONE, Material.DIAMOND_BLOCK, Material.OBSIDIAN);
    }

    @Override
    public long getRegenerationTime() {
        return 3000;
    }
}
