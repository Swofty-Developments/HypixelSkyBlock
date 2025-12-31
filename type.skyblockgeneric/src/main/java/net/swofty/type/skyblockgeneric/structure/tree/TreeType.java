package net.swofty.type.skyblockgeneric.structure.tree;

import lombok.Getter;
import net.minestom.server.instance.block.Block;

@Getter
public enum TreeType {
    OAK(Block.OAK_LOG, Block.OAK_LEAVES,
            new TreeConfig(4, 7, 3, 5, 0.2, 0.3, 3, true)),
    BIRCH(Block.BIRCH_LOG, Block.BIRCH_LEAVES,
            new TreeConfig(5, 8, 2, 4, 0.1, 0.25, 2, false)),
    SPRUCE(Block.SPRUCE_LOG, Block.SPRUCE_LEAVES,
            new TreeConfig(6, 12, 2, 4, 0.15, 0.2, 3, false)),
    DARK_OAK(Block.DARK_OAK_LOG, Block.DARK_OAK_LEAVES,
            new TreeConfig(6, 9, 4, 7, 0.25, 0.4, 4, true)),
    JUNGLE(Block.JUNGLE_LOG, Block.JUNGLE_LEAVES,
            new TreeConfig(8, 16, 5, 8, 0.2, 0.35, 5, true)),
    ACACIA(Block.ACACIA_LOG, Block.ACACIA_LEAVES,
            new TreeConfig(5, 8, 4, 6, 0.4, 0.35, 3, true));

    private final Block logBlock;
    private final Block leavesBlock;
    private final TreeConfig defaultConfig;

    TreeType(Block logBlock, Block leavesBlock, TreeConfig defaultConfig) {
        this.logBlock = logBlock;
        this.leavesBlock = leavesBlock;
        this.defaultConfig = defaultConfig;
    }
}
