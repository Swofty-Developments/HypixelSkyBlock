package net.swofty.anticheat.world;

import lombok.Getter;

@Getter
public class Block {
    private final boolean isWater;
    private final boolean isLava;
    private final BoundingBox boundingBox;

    public Block(boolean isWater, boolean isLava, BoundingBox boundingBox) {
        this.isWater = isWater;
        this.isLava = isLava;
        this.boundingBox = boundingBox;
    }

}
