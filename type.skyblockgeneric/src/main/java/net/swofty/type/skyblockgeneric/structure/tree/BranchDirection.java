package net.swofty.type.skyblockgeneric.structure.tree;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public enum BranchDirection {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0),
    NORTH_EAST(1, -1),
    NORTH_WEST(-1, -1),
    SOUTH_EAST(1, 1),
    SOUTH_WEST(-1, 1);

    private final int xOffset;
    private final int zOffset;

    BranchDirection(int xOffset, int zOffset) {
        this.xOffset = xOffset;
        this.zOffset = zOffset;
    }

    public static BranchDirection random() {
        BranchDirection[] values = values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    public static BranchDirection randomCardinal() {
        BranchDirection[] cardinals = {NORTH, SOUTH, EAST, WEST};
        return cardinals[ThreadLocalRandom.current().nextInt(cardinals.length)];
    }

    public boolean isOpposite(BranchDirection other) {
        return this.xOffset == -other.xOffset && this.zOffset == -other.zOffset;
    }
}
