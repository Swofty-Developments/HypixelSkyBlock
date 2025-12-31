package net.swofty.type.skyblockgeneric.structure.tree;

public record TreeConfig(
        int minHeight,
        int maxHeight,
        int minWidth,
        int maxWidth,
        double slantChance,
        double branchChance,
        int leafRadius,
        boolean canBranchEarly
) {
    public TreeConfig withMinHeight(int minHeight) {
        return new TreeConfig(minHeight, maxHeight, minWidth, maxWidth, slantChance, branchChance, leafRadius, canBranchEarly);
    }

    public TreeConfig withMaxHeight(int maxHeight) {
        return new TreeConfig(minHeight, maxHeight, minWidth, maxWidth, slantChance, branchChance, leafRadius, canBranchEarly);
    }

    public TreeConfig withMinWidth(int minWidth) {
        return new TreeConfig(minHeight, maxHeight, minWidth, maxWidth, slantChance, branchChance, leafRadius, canBranchEarly);
    }

    public TreeConfig withMaxWidth(int maxWidth) {
        return new TreeConfig(minHeight, maxHeight, minWidth, maxWidth, slantChance, branchChance, leafRadius, canBranchEarly);
    }

    public TreeConfig withSlantChance(double slantChance) {
        return new TreeConfig(minHeight, maxHeight, minWidth, maxWidth, slantChance, branchChance, leafRadius, canBranchEarly);
    }

    public TreeConfig withBranchChance(double branchChance) {
        return new TreeConfig(minHeight, maxHeight, minWidth, maxWidth, slantChance, branchChance, leafRadius, canBranchEarly);
    }

    public TreeConfig withLeafRadius(int leafRadius) {
        return new TreeConfig(minHeight, maxHeight, minWidth, maxWidth, slantChance, branchChance, leafRadius, canBranchEarly);
    }

    public TreeConfig withCanBranchEarly(boolean canBranchEarly) {
        return new TreeConfig(minHeight, maxHeight, minWidth, maxWidth, slantChance, branchChance, leafRadius, canBranchEarly);
    }
}
