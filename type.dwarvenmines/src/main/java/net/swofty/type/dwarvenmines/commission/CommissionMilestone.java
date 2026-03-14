package net.swofty.type.dwarvenmines.commission;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CommissionMilestone {
    TIER_1(1, 5, 100_000, 20, true, false, false, false, false, false),
    TIER_2(2, 25, 200_000, 30, false, false, false, false, false, false),
    TIER_3(3, 100, 400_000, 30, false, true, false, false, false, false),
    TIER_4(4, 250, 800_000, 50, false, false, true, false, false, false),
    TIER_5(5, 500, 1_200_000, 50, false, false, false, true, false, false),
    TIER_6(6, 750, 1_600_000, 75, false, false, false, false, true, false);

    private final int tier;
    private final int commissionsRequired;
    private final int miningXpReward;
    private final int skyBlockXpReward;
    private final boolean unlocksEmissaries;
    private final boolean unlocksExtraSlot;
    private final boolean unlocksDwarvenMinesScroll;
    private final boolean unlocksRoyalPigeon;
    private final boolean unlocksCrystalNucleusScroll;
    private final boolean hasSpecialReward;

    CommissionMilestone(int tier, int commissionsRequired, int miningXpReward, int skyBlockXpReward,
                        boolean unlocksEmissaries, boolean unlocksExtraSlot,
                        boolean unlocksDwarvenMinesScroll, boolean unlocksRoyalPigeon,
                        boolean unlocksCrystalNucleusScroll, boolean hasSpecialReward) {
        this.tier = tier;
        this.commissionsRequired = commissionsRequired;
        this.miningXpReward = miningXpReward;
        this.skyBlockXpReward = skyBlockXpReward;
        this.unlocksEmissaries = unlocksEmissaries;
        this.unlocksExtraSlot = unlocksExtraSlot;
        this.unlocksDwarvenMinesScroll = unlocksDwarvenMinesScroll;
        this.unlocksRoyalPigeon = unlocksRoyalPigeon;
        this.unlocksCrystalNucleusScroll = unlocksCrystalNucleusScroll;
        this.hasSpecialReward = hasSpecialReward;
    }

    public static CommissionMilestone getMilestoneForCompleted(int totalCompleted) {
        CommissionMilestone result = null;
        for (CommissionMilestone milestone : values()) {
            if (totalCompleted >= milestone.commissionsRequired) {
                result = milestone;
            }
        }
        return result;
    }

    public static CommissionMilestone getNextMilestone(int totalCompleted) {
        for (CommissionMilestone milestone : values()) {
            if (totalCompleted < milestone.commissionsRequired) {
                return milestone;
            }
        }
        return null;
    }

    public String[] getRewardDescriptions() {
        List<String> rewards = new ArrayList<>();

        if (unlocksEmissaries) {
            rewards.add("§7- §aEmissaries");
            rewards.add("§7- §6Royal Compass");
        }
        if (unlocksExtraSlot) {
            rewards.add("§7- §a+1 Commission Slot");
        }
        if (unlocksDwarvenMinesScroll) {
            rewards.add("§7- §5Travel Scroll to Dwarven Mines");
        }
        if (unlocksRoyalPigeon) {
            rewards.add("§7- §6Royal Pigeon");
        }
        if (unlocksCrystalNucleusScroll) {
            rewards.add("§7- §5Travel Scroll to Crystal Nucleus");
        }

        rewards.add("§7- §3+" + String.format("%,d", miningXpReward) + " Mining XP");
        rewards.add("§7- §b+" + skyBlockXpReward + " SkyBlock XP");

        return rewards.toArray(new String[0]);
    }
}

