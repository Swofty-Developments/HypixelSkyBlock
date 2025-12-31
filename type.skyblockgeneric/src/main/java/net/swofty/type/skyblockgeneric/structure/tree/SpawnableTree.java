package net.swofty.type.skyblockgeneric.structure.tree;

import lombok.Getter;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.swofty.type.skyblockgeneric.structure.SkyBlockStructure;

import java.util.ArrayList;

@Getter
public enum SpawnableTree {
    // Oak variants
    SMALL_OAK(TreeType.OAK, new TreeConfig(4, 5, 2, 3, 0.1, 0.2, 2, false)),
    MEDIUM_OAK(TreeType.OAK, new TreeConfig(5, 7, 3, 5, 0.2, 0.3, 3, true)),
    LARGE_OAK(TreeType.OAK, new TreeConfig(8, 12, 5, 7, 0.25, 0.4, 4, true)),
    GIANT_OAK(TreeType.OAK, new TreeConfig(12, 16, 7, 10, 0.3, 0.45, 5, true)),

    // Birch variants
    SMALL_BIRCH(TreeType.BIRCH, new TreeConfig(5, 6, 2, 3, 0.05, 0.15, 2, false)),
    MEDIUM_BIRCH(TreeType.BIRCH, new TreeConfig(6, 8, 2, 4, 0.1, 0.25, 2, false)),
    TALL_BIRCH(TreeType.BIRCH, new TreeConfig(8, 12, 3, 4, 0.1, 0.2, 3, false)),

    // Spruce variants
    SMALL_SPRUCE(TreeType.SPRUCE, new TreeConfig(5, 7, 2, 3, 0.1, 0.15, 2, false)),
    MEDIUM_SPRUCE(TreeType.SPRUCE, new TreeConfig(7, 10, 3, 4, 0.15, 0.2, 3, false)),
    TALL_SPRUCE(TreeType.SPRUCE, new TreeConfig(10, 15, 3, 5, 0.15, 0.25, 4, false)),
    GIANT_SPRUCE(TreeType.SPRUCE, new TreeConfig(15, 25, 4, 6, 0.2, 0.3, 5, false)),

    // Dark Oak variants
    SMALL_DARK_OAK(TreeType.DARK_OAK, new TreeConfig(5, 7, 3, 5, 0.2, 0.35, 3, true)),
    MEDIUM_DARK_OAK(TreeType.DARK_OAK, new TreeConfig(6, 9, 4, 7, 0.25, 0.4, 4, true)),
    LARGE_DARK_OAK(TreeType.DARK_OAK, new TreeConfig(8, 12, 6, 9, 0.3, 0.45, 5, true)),

    // Jungle variants
    SMALL_JUNGLE(TreeType.JUNGLE, new TreeConfig(6, 8, 3, 5, 0.15, 0.3, 3, true)),
    MEDIUM_JUNGLE(TreeType.JUNGLE, new TreeConfig(8, 12, 5, 7, 0.2, 0.35, 4, true)),
    LARGE_JUNGLE(TreeType.JUNGLE, new TreeConfig(12, 18, 6, 9, 0.25, 0.4, 5, true)),
    GIANT_JUNGLE(TreeType.JUNGLE, new TreeConfig(18, 30, 8, 12, 0.3, 0.45, 6, true)),

    // Acacia variants
    SMALL_ACACIA(TreeType.ACACIA, new TreeConfig(4, 6, 3, 5, 0.35, 0.3, 3, true)),
    MEDIUM_ACACIA(TreeType.ACACIA, new TreeConfig(5, 8, 4, 6, 0.4, 0.35, 3, true)),
    LARGE_ACACIA(TreeType.ACACIA, new TreeConfig(7, 10, 5, 8, 0.45, 0.4, 4, true)),

    // Special/themed variants
    BUSH(TreeType.OAK, new TreeConfig(2, 3, 2, 3, 0.0, 0.1, 2, false)),
    SHRUB(TreeType.JUNGLE, new TreeConfig(2, 3, 2, 4, 0.0, 0.15, 2, true)),
    WINDSWEPT_OAK(TreeType.OAK, new TreeConfig(5, 8, 4, 6, 0.6, 0.35, 3, true)),
    WINDSWEPT_ACACIA(TreeType.ACACIA, new TreeConfig(5, 8, 5, 8, 0.7, 0.4, 3, true));

    private final TreeType treeType;
    private final TreeConfig config;

    SpawnableTree(TreeType treeType, TreeConfig config) {
        this.treeType = treeType;
        this.config = config;
    }

    public Block getLogBlock() {
        return treeType.getLogBlock();
    }

    public Block getLeavesBlock() {
        return treeType.getLeavesBlock();
    }

    /**
     * Create a SkyBlockTree at the given position using this preset.
     */
    public SkyBlockTree createAt(int x, int y, int z) {
        return new SkyBlockTree(0, x, y, z, treeType, config);
    }

    /**
     * Create a SkyBlockTree with a specific seed for reproducibility.
     */
    public SkyBlockTree createAt(int x, int y, int z, long seed) {
        return new SkyBlockTree(0, x, y, z, treeType, config, seed);
    }

    /**
     * Create a SkyBlockTree with rotation.
     */
    public SkyBlockTree createAt(int rotation, int x, int y, int z) {
        return new SkyBlockTree(rotation, x, y, z, treeType, config);
    }

    /**
     * Create a SkyBlockTree, build it, and register it in the TreeRegistry.
     * This allows the tree to be tracked for later respawning.
     */
    public SkyBlockTree createAndRegister(int x, int y, int z, long seed, SharedInstance instance) {
        SkyBlockTree tree = new SkyBlockTree(0, x, y, z, treeType, config, seed);
        tree.build(instance);

        // Convert local coordinates to world coordinates and build block list
        var blocks = new ArrayList<TreeRegistry.BlockEntry>();

        for (SkyBlockTree.LogPosition log : tree.getPlacedLogs()) {
            int worldX = tree.rotateValue(x, log.x(), SkyBlockStructure.CoordinateType.X);
            int worldY = y + log.y();
            int worldZ = tree.rotateValue(z, log.z(), SkyBlockStructure.CoordinateType.Z);
            blocks.add(new TreeRegistry.BlockEntry(worldX, worldY, worldZ, treeType.getLogBlock()));
        }

        for (SkyBlockTree.LogPosition leaf : tree.getPlacedLeaves()) {
            int worldX = tree.rotateValue(x, leaf.x(), SkyBlockStructure.CoordinateType.X);
            int worldY = y + leaf.y();
            int worldZ = tree.rotateValue(z, leaf.z(), SkyBlockStructure.CoordinateType.Z);
            blocks.add(new TreeRegistry.BlockEntry(worldX, worldY, worldZ, treeType.getLeavesBlock()));
        }

        TreeRegistry.RegisteredTree registeredTree = new TreeRegistry.RegisteredTree(
                x, y, z, this, seed, blocks
        );
        TreeRegistry.registerTree(instance, registeredTree);

        return tree;
    }
}
