package net.swofty.type.skyblockgeneric.hunting;

import net.swofty.commons.skyblock.item.Rarity;

public enum AttributeRarity {
    COMMON(new int[]{1, 4, 9, 15, 22, 30, 40, 54, 72, 96}, 0),
    UNCOMMON(new int[]{1, 3, 6, 10, 15, 21, 28, 36, 48, 64}, 5),
    RARE(new int[]{1, 3, 6, 9, 13, 17, 22, 28, 36, 48}, 10),
    EPIC(new int[]{1, 2, 4, 6, 9, 12, 16, 20, 25, 32}, 15),
    LEGENDARY(new int[]{1, 2, 3, 5, 7, 9, 12, 15, 19, 24}, 20);

    private final int[] cumulativeShards;
    private final int huntingRequirement;

    AttributeRarity(int[] cumulativeShards, int huntingRequirement) {
        this.cumulativeShards = cumulativeShards;
        this.huntingRequirement = huntingRequirement;
    }

    public int levelFor(int syphoned) {
        int level = 0;
        for (int requirement : cumulativeShards) {
            if (syphoned < requirement) break;
            level++;
        }
        return level;
    }

    public int cumulativeForLevel(int level) {
        if (level <= 0) return 0;
        return cumulativeShards[Math.min(10, level) - 1];
    }

    public int nextRequirement(int syphoned) {
        int level = levelFor(syphoned);
        return level >= 10 ? cumulativeShards[9] : cumulativeShards[level];
    }

    public int huntingRequirement() {
        return huntingRequirement;
    }

    public Rarity itemRarity() {
        return Rarity.valueOf(name());
    }
}
