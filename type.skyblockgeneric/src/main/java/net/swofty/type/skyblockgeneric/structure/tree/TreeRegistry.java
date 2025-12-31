package net.swofty.type.skyblockgeneric.structure.tree;

import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TreeRegistry {
    // Map of Instance (by identity) to list of registered trees
    private static final Map<Instance, List<RegisteredTree>> instanceTrees = new ConcurrentHashMap<>();

    /**
     * Register a tree after it has been built.
     */
    public static void registerTree(Instance instance, RegisteredTree tree) {
        instanceTrees.computeIfAbsent(instance, k -> Collections.synchronizedList(new ArrayList<>())).add(tree);
    }

    /**
     * Get all trees registered for an instance.
     */
    public static List<RegisteredTree> getTreesForInstance(Instance instance) {
        return instanceTrees.getOrDefault(instance, Collections.emptyList());
    }

    /**
     * Find trees where at least one block is within distance of a position.
     */
    public static List<RegisteredTree> getTreesInRange(Instance instance, int x, int y, int z, double distance) {
        List<RegisteredTree> result = new ArrayList<>();
        double distanceSquared = distance * distance;

        for (RegisteredTree tree : getTreesForInstance(instance)) {
            // Check if any block of the tree is within range
            for (BlockEntry block : tree.allBlocks()) {
                double dx = block.worldX() - x;
                double dy = block.worldY() - y;
                double dz = block.worldZ() - z;
                if (dx * dx + dy * dy + dz * dz <= distanceSquared) {
                    result.add(tree);
                    break; // Found at least one block in range
                }
            }
        }
        return result;
    }

    /**
     * Unregister a tree (used before regenerating).
     */
    public static void unregisterTree(Instance instance, RegisteredTree tree) {
        List<RegisteredTree> trees = instanceTrees.get(instance);
        if (trees != null) {
            trees.remove(tree);
        }
    }

    /**
     * Clear all trees for an instance.
     */
    public static void clearInstance(Instance instance) {
        instanceTrees.remove(instance);
    }

    /**
     * Represents a registered tree with all its metadata.
     */
    public record RegisteredTree(
            int baseX,
            int baseY,
            int baseZ,
            SpawnableTree spawnableTree,
            long seed,
            List<BlockEntry> allBlocks
    ) {}

    /**
     * Represents a single placed block with its expected type.
     */
    public record BlockEntry(
            int worldX,
            int worldY,
            int worldZ,
            Block expectedBlock
    ) {}
}
