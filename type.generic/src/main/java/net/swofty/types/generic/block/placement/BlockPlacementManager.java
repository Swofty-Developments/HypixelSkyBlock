package net.swofty.types.generic.block.placement;

import net.swofty.types.generic.block.placement.rules.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockManager;

public class BlockPlacementManager {
    private static final BlockManager blockManager = MinecraftServer.getBlockManager();

    public static void register() {
        for (Block block : Block.values()) {
            register(block);
        }
    }

    public static void register(Block block) {
        MultiplePlacementRules placementRules = new MultiplePlacementRules(block);
        String blockName = block.name().toLowerCase();

        if (block.properties().containsKey("facing"))
            placementRules.addBlockPlacement(new FacingPlacement(block));

        if (block.properties().containsKey("axis"))
            placementRules.addBlockPlacement(new AxisPlacement(block));

        if (block.properties().containsKey("waterlogged"))
            placementRules.addBlockPlacement(new DisableWaterLogPlacement(block));

        if (block.properties().containsKey("rotation"))
            placementRules.addBlockPlacement(new RotationPlacement(block));

        if (DoublePlantPlacement.isDoublePlant(block))
            placementRules.addBlockPlacement(new DoublePlantPlacement(block));

        if (StairsPlacement.isStairs(block))
            placementRules.addBlockPlacement(new StairsPlacement(block));

        if (CardinalPlacement.isCardinalBlock(block))
            placementRules.addBlockPlacement(new CardinalPlacement(block));

        if (block == Block.GRINDSTONE)
            placementRules.addBlockPlacement(GrindstonePlacement::new);

        if (block == Block.REDSTONE_WIRE)
            placementRules.addBlockPlacement(RedstonePlacement::new);

        if (block == Block.BAMBOO)
            placementRules.addBlockPlacement(BambooPlacement::new);

        if (block == Block.BELL)
            placementRules.addBlockPlacement(BellPlacement::new);

        if (block == Block.TWISTING_VINES)
            placementRules.addBlockPlacement(TwistingVines::new);

        if (block == Block.WEEPING_VINES)
            placementRules.addBlockPlacement(WeepingVines::new);

        if (block == Block.SNOW)
            placementRules.addBlockPlacement(SnowLayerPlacement::new);

        if (block == Block.TURTLE_EGG)
            placementRules.addBlockPlacement(TurtleEggPlacement::new);

        if (block == Block.PLAYER_HEAD)
            placementRules.addBlockPlacement(new HeadPlacement(block, Block.PLAYER_WALL_HEAD));

        if (block == Block.ZOMBIE_HEAD)
            placementRules.addBlockPlacement(new HeadPlacement(block, Block.ZOMBIE_WALL_HEAD));

        if (block == Block.CREEPER_HEAD)
            placementRules.addBlockPlacement(new HeadPlacement(block, Block.CREEPER_WALL_HEAD));

        if (block == Block.DRAGON_HEAD)
            placementRules.addBlockPlacement(new HeadPlacement(block, Block.DRAGON_WALL_HEAD));

        if (block == Block.SKELETON_SKULL)
            placementRules.addBlockPlacement(new HeadPlacement(block, Block.SKELETON_WALL_SKULL));

        if (block == Block.LANTERN || block == Block.SOUL_LANTERN)
            placementRules.addBlockPlacement(new LanternPlacement(block));

        if (block == Block.TORCH)
            placementRules.addBlockPlacement(new TorchPlacement(block, Block.WALL_TORCH));

        if (block == Block.REDSTONE_TORCH)
            placementRules.addBlockPlacement(new TorchPlacement(block, Block.REDSTONE_WALL_TORCH));

        if (block == Block.SOUL_TORCH)
            placementRules.addBlockPlacement(new TorchPlacement(block, Block.SOUL_WALL_TORCH));

        if (block == Block.CHEST || block == Block.TRAPPED_CHEST)
            placementRules.addBlockPlacement(new ChestBlockPlacement(block));

        if (blockName.contains("wall") && !blockName.contains("skull") && !blockName.contains("torch") && !blockName.contains("head") && !blockName.contains("wall_sign"))
            placementRules.addBlockPlacement(new WallPlacement(block));

        if (blockName.contains("slab"))
            placementRules.addBlockPlacement(new SlabPlacement(block));

        if (blockName.contains("anvil"))
            placementRules.addBlockPlacement(new AnvilPlacement(block));

        if (blockName.contains("bed") && block != Block.BEDROCK)
            placementRules.addBlockPlacement(new BedPlacement(block));

        if (blockName.contains("trapdoor"))
            placementRules.addBlockPlacement(new TrapdoorPlacement(block));

        if (blockName.contains("door") && !blockName.contains("trapdoor"))
            placementRules.addBlockPlacement(new DoorPlacement(block));

        if (blockName.contains("button") || block == Block.LEVER)
            placementRules.addBlockPlacement(new ButtonAndLeverPlacement(block));

        if (blockName.contains("fence_gate"))
            placementRules.addBlockPlacement(new FenceGatePlacement(block));

        if (placementRules.getPlacementRules().isEmpty())
            placementRules.addBlockPlacement(new UnknownPlacement(block));

        blockManager.registerBlockPlacementRule(placementRules);
    }
}
