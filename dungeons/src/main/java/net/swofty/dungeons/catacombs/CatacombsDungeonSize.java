package net.swofty.dungeons.catacombs;

public enum CatacombsDungeonSize {
    TINY(4, 4),
    SMALL(5, 5),
    MEDIUM_TALL(5, 6),
    MEDIUM(6, 6),
    LARGE(6, 6);

    private final int width;
    private final int height;

    CatacombsDungeonSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
