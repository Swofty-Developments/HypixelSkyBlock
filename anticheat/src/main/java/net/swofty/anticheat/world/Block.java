package net.swofty.anticheat.world;

public class Block {
    private boolean isWater;
    private boolean isLava;
    private BoundingBox boundingBox;

    public Block(boolean isWater, boolean isLava, BoundingBox boundingBox) {
        this.isWater = isWater;
        this.isLava = isLava;
        this.boundingBox = boundingBox;
    }

    public boolean isWater() {
        return isWater;
    }

    public boolean isLava() {
        return isLava;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
