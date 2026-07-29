package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.instance.block.Block;

public enum LuckyBlockTier {
    NORMAL("§eNormal Lucky Block", Block.YELLOW_STAINED_GLASS, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzlkMWVmNGMxNTNiZGU4YTdiMDhmOTJjMGM0Yjc5ZmRjMmZjYjU3ZjgzYTMxYTgyMjljNjJhYzc1ZjFmMGEzMSJ9fX0="),
    PROMISING("§1Promising Lucky Block", Block.LIGHT_BLUE_STAINED_GLASS, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY2ZDZjMTllNGY1MDUxODgyODUxYTRhZWFkNzlmMGYxZjM4YWE2ODk3MTliNmIzMzAzMTdlYTJiOGIwZTUwMCJ9fX0="),
    FORTUNATE("Fortunate Lucky Block", Block.LIME_STAINED_GLASS, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjExODU2MTVkNWNjN2M3MDBlYWJjYjdkYjA5N2VkNzIxZDU4OWZkZmVlZjlmMDMzMzM2YzI2Yzk4OGU0YmU0NiJ9fX0="),
    OFFENSIVE("§5Offensive Lucky Block", Block.BLACK_STAINED_GLASS, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzcxOTMwMjc4ZjAyOWM1Nzc4Yzg1YzA0NzVhMzdmYWM4YWNkMzg1MDc0MmFhZTZhMzU0MmFjZGU0NDg3ZDYzIn19fQ=="),
    MIRACLE("§cMiracle Lucky Block", Block.RED_STAINED_GLASS, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzEyNjExNjU2M2U5MDRjZGU3ZjUyYWUwZmI1ZTA3NjZlNjBhYmY0NzU3OTU3ZGU5ZGQzYjA2ZWRmMWY4YmQ4ZSJ9fX0=");

    private final String displayName;
    private final Block placeBlock;
    private final String headTexture;

    LuckyBlockTier(String displayName, Block placeBlock, String headTexture) {
        this.displayName = displayName;
        this.placeBlock = placeBlock;
        this.headTexture = headTexture;
    }

    public String displayName() {
        return displayName;
    }

    public Block placeBlock() {
        return placeBlock;
    }

    public String headTexture() {
        return headTexture;
    }
}
