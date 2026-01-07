package net.swofty.type.dwarvenmines.commission;

import lombok.Getter;

@Getter
public enum HOTMTier {
    TIER_1(1, 0, 0, 1, 0, 35),
    TIER_2(2, 3_000, 3_000, 2, 0, 45),
    TIER_3(3, 9_000, 12_000, 2, 1, 60),
    TIER_4(4, 25_000, 37_000, 2, 1, 75),
    TIER_5(5, 60_000, 97_000, 2, 1, 90),
    TIER_6(6, 100_000, 197_000, 2, 1, 110),
    TIER_7(7, 150_000, 347_000, 3, 1, 130),
    TIER_8(8, 210_000, 557_000, 2, 0, 180),
    TIER_9(9, 290_000, 847_000, 2, 0, 210),
    TIER_10(10, 400_000, 1_247_000, 2, 0, 240);

    private final int tier;
    private final int xpRequired;
    private final int cumulativeXp;
    private final int tokenReward;
    private final int forgeSlotReward;
    private final int skyBlockXpReward;

    HOTMTier(int tier, int xpRequired, int cumulativeXp, int tokenReward, int forgeSlotReward, int skyBlockXpReward) {
        this.tier = tier;
        this.xpRequired = xpRequired;
        this.cumulativeXp = cumulativeXp;
        this.tokenReward = tokenReward;
        this.forgeSlotReward = forgeSlotReward;
        this.skyBlockXpReward = skyBlockXpReward;
    }

    public static HOTMTier getTierForXp(long totalXp) {
        HOTMTier result = TIER_1;
        for (HOTMTier tier : values()) {
            if (totalXp >= tier.cumulativeXp) {
                result = tier;
            }
        }
        return result;
    }

    public static HOTMTier getNextTier(long totalXp) {
        for (HOTMTier tier : values()) {
            if (totalXp < tier.cumulativeXp) {
                return tier;
            }
        }
        return null; // Maxed
    }

    public static boolean isMaxed(long totalXp) {
        return totalXp >= TIER_10.cumulativeXp;
    }

    public int getTotalForgeSlots() {
        int slots = 0;
        for (HOTMTier t : values()) {
            if (t.tier <= this.tier) {
                slots += t.forgeSlotReward;
            }
        }
        return slots;
    }

    public int getTotalTokens() {
        int tokens = 0;
        for (HOTMTier t : values()) {
            if (t.tier <= this.tier) {
                tokens += t.tokenReward;
            }
        }
        return tokens;
    }
}

