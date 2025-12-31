package net.swofty.type.skyblockgeneric.structure.tree;

public record TreeBranch(
        int x,
        int y,
        int z,
        BranchDirection direction,
        int remainingLength,
        boolean isMainTrunk
) {
    public TreeBranch advance() {
        int newX = x;
        int newZ = z;
        if (direction != null) {
            newX += direction.getXOffset();
            newZ += direction.getZOffset();
        }
        return new TreeBranch(newX, y + 1, newZ, direction, remainingLength - 1, isMainTrunk);
    }

    public TreeBranch advanceWithDirection(BranchDirection newDirection) {
        return new TreeBranch(x, y, z, newDirection, remainingLength, isMainTrunk);
    }

    public TreeBranch fork(BranchDirection newDirection, int length) {
        return new TreeBranch(
                x + newDirection.getXOffset(),
                y,
                z + newDirection.getZOffset(),
                newDirection,
                length,
                false
        );
    }
}
