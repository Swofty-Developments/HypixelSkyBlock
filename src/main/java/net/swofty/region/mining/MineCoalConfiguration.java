package net.swofty.region.mining;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.region.SkyBlockMiningConfiguration;
import net.swofty.user.SkyBlockPlayer;

import java.util.List;

public class MineCoalConfiguration extends SkyBlockMiningConfiguration {

    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        task.setCurrentBlock(Block.COBBLESTONE);

        Block regenerationBlock = getRandomBlock(
                new RegenerationConfig(30, Block.COAL_ORE),
                new RegenerationConfig(70, Block.STONE)
        );

        task.setReviveBlock(regenerationBlock);

        return task;
    }

    @Override
    public MiningTask handleStageTwo(MiningTask task, Pos brokenBlock) {
        task.setCurrentBlock(Block.BEDROCK);
        task.setReviveBlock(Block.COBBLESTONE);

        return task;
    }

    @Override
    public List<Material> getMineableBlocks() {
        return List.of(Material.COAL_ORE, Material.COBBLESTONE, Material.STONE);
    }

    @Override
    public long getRegenerationTime() {
        return 3000;
    }
}
