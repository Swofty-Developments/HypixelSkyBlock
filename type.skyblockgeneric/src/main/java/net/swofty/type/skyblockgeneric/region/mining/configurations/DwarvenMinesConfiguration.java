package net.swofty.type.skyblockgeneric.region.mining.configurations;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegenConfiguration;

import java.util.List;

public class DwarvenMinesConfiguration extends SkyBlockRegenConfiguration {

    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        task.setIntermediaryBlock(Block.BEDROCK);
        Block regenerationBlock;
        regenerationBlock = getRandomBlock(
                new RegenerationConfig(3, Block.POLISHED_DIORITE), // TODO: use chance from task.getPlayerWhoInitiated's HOTM
                new RegenerationConfig(97, task.getInitialMinedBlock()));
        task.setReviveBlock(regenerationBlock);
        return task;
    }

    @Override
    public MiningTask handleStageTwo(MiningTask task, Pos brokenBlock) {
        return task;
    }

    @Override
    public List<Material> getMineableBlocks(Instance instance, Point point) {
        return List.of(Material.CYAN_TERRACOTTA, Material.GRAY_WOOL, Material.LIGHT_BLUE_WOOL, Material.PRISMARINE, Material.DARK_PRISMARINE, Material.PRISMARINE_BRICKS, Material.POLISHED_DIORITE);
    }

    @Override
    public long getRegenerationTime() {
        return 3000;
    }
}
