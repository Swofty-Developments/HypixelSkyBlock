package net.swofty.type.skyblockgeneric.region.mining.configurations.deepmines;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegenConfiguration;

import java.util.List;

public class GunpowderMinesConfiguration extends SkyBlockRegenConfiguration {

    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        task.setIntermediaryBlock(Block.COBBLESTONE);

        Block regenerationBlock;

        regenerationBlock = getRandomBlock(
                new RegenerationConfig(10, Block.COAL_ORE),
                new RegenerationConfig(10, Block.IRON_ORE),
                new RegenerationConfig(10, Block.GOLD_ORE),
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
        return List.of(Material.COAL_ORE, Material.COBBLESTONE, Material.STONE, Material.IRON_ORE, Material.GOLD_ORE);
    }

    @Override
    public long getRegenerationTime() {
        return 3000;
    }
}
