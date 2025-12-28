package net.swofty.type.dwarvenmines.commission;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Commission reward calculator based on HOTM tier and daily bonus status.
 */
@Getter
@AllArgsConstructor
public class CommissionReward {
    private final int hotmXp;
    private final int miningXp;
    private final int mithrilPowder;
    private final boolean isDailyBonus;

    /**
     * Calculates rewards for completing a commission.
     *
     * @param hotmTier      the player's current HOTM tier (1-10)
     * @param isHotmMaxed   whether HOTM is maxed (tier 10)
     * @param isDailyBonus  whether this is one of the first 4 daily commissions
     * @return the commission reward
     */
    public static CommissionReward calculate(int hotmTier, boolean isHotmMaxed, boolean isDailyBonus) {
        int hotmXp;
        int miningXp;
        int mithrilPowder;

        // Base HOTM XP based on tier (when not maxed)
        if (!isHotmMaxed) {
            if (hotmTier == 1) {
                hotmXp = 100;
            } else if (hotmTier == 2) {
                hotmXp = 200;
            } else {
                hotmXp = 400;
            }
        } else {
            hotmXp = 0; // No HOTM XP when maxed
        }

        // Mining XP: random between 5,000 and 10,000
        miningXp = ThreadLocalRandom.current().nextInt(5_000, 10_001);

        // Mithril Powder: 100-200 when not maxed, 500 when maxed
        if (!isHotmMaxed) {
            mithrilPowder = ThreadLocalRandom.current().nextInt(100, 201);
        } else {
            mithrilPowder = 500;
        }

        // Daily bonus for first 4 commissions
        int dailyBonusHotmXp = 0;
        int dailyBonusPowder = 0;
        if (isDailyBonus) {
            if (!isHotmMaxed) {
                dailyBonusHotmXp = 900; // +900 HOTM XP daily bonus
            } else {
                dailyBonusPowder = 2000; // +2000 Mithril Powder when maxed
            }
        }

        return new CommissionReward(
                hotmXp + dailyBonusHotmXp,
                miningXp,
                mithrilPowder + dailyBonusPowder,
                isDailyBonus
        );
    }

    /**
     * Gets the formatted reward lines for display in the GUI.
     *
     * @param hotmTier     the player's current HOTM tier (1-10)
     * @param isHotmMaxed  whether HOTM is maxed
     * @param isDailyBonus whether this is a daily bonus commission
     * @return array of formatted reward strings
     */
    public static String[] getRewardLore(int hotmTier, boolean isHotmMaxed, boolean isDailyBonus) {
        java.util.List<String> lore = new java.util.ArrayList<>();

        if (!isHotmMaxed) {
            int baseHotmXp;
            if (hotmTier == 1) {
                baseHotmXp = 100;
            } else if (hotmTier == 2) {
                baseHotmXp = 200;
            } else {
                baseHotmXp = 400;
            }

            if (isDailyBonus) {
                lore.add("§7- §5+" + (baseHotmXp + 900) + " Heart of the Mountain XP §e(Daily Bonus)");
            } else {
                lore.add("§7- §5+" + baseHotmXp + " Heart of the Mountain XP");
            }
        }

        if (!isHotmMaxed) {
            if (isDailyBonus) {
                lore.add("§7- §2+100 Mithril Powder");
            } else {
                lore.add("§7- §2+100 Mithril Powder");
            }
        } else {
            lore.add("§7- §2+500 Mithril Powder");
        }

        lore.add("§7- §3+7.5k Mining XP");

        return lore.toArray(new String[0]);
    }
}

