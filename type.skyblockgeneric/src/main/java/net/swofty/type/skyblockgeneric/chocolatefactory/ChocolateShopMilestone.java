package net.swofty.type.skyblockgeneric.chocolatefactory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.item.Material;

/**
 * Represents chocolate shop milestones.
 * Each milestone unlocks a special rabbit when spending a certain amount of chocolate in the shop.
 */
@Getter
@AllArgsConstructor
public enum ChocolateShopMilestone {
    // Common milestones (White)
    MILESTONE_1(1, 1_000L, "Alpha Rabbit", "f", 1, 0.002,
            "a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2", Material.WHITE_STAINED_GLASS_PANE),
    MILESTONE_2(2, 5_000L, "Beta Rabbit", "f", 1, 0.002,
            "b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2", Material.WHITE_STAINED_GLASS_PANE),
    MILESTONE_3(3, 25_000L, "Gamma Rabbit", "f", 1, 0.002,
            "c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2", Material.WHITE_STAINED_GLASS_PANE),
    MILESTONE_4(4, 100_000L, "Delta Rabbit", "f", 1, 0.002,
            "d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2", Material.WHITE_STAINED_GLASS_PANE),
    MILESTONE_5(5, 500_000L, "Epsilon Rabbit", "f", 1, 0.002,
            "e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2", Material.WHITE_STAINED_GLASS_PANE),

    // Uncommon milestones (Lime/Green)
    MILESTONE_6(6, 1_000_000L, "Zeta Rabbit", "a", 2, 0.003,
            "f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2", Material.LIME_STAINED_GLASS_PANE),
    MILESTONE_7(7, 2_500_000L, "Eta Rabbit", "a", 2, 0.003,
            "a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3", Material.LIME_STAINED_GLASS_PANE),
    MILESTONE_8(8, 5_000_000L, "Theta Rabbit", "a", 2, 0.003,
            "b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3", Material.LIME_STAINED_GLASS_PANE),
    MILESTONE_9(9, 10_000_000L, "Iota Rabbit", "a", 2, 0.003,
            "c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3", Material.LIME_STAINED_GLASS_PANE),
    MILESTONE_10(10, 25_000_000L, "Kappa Rabbit", "a", 2, 0.003,
            "d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3", Material.LIME_STAINED_GLASS_PANE),

    // Rare milestones (Blue)
    MILESTONE_11(11, 50_000_000L, "Lambda Rabbit", "9", 4, 0.004,
            "e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3", Material.BLUE_STAINED_GLASS_PANE),
    MILESTONE_12(12, 100_000_000L, "Mu Rabbit", "9", 4, 0.004,
            "f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3", Material.BLUE_STAINED_GLASS_PANE),
    MILESTONE_13(13, 250_000_000L, "Nu Rabbit", "9", 4, 0.004,
            "a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4", Material.BLUE_STAINED_GLASS_PANE),
    MILESTONE_14(14, 500_000_000L, "Xi Rabbit", "9", 4, 0.004,
            "b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4", Material.BLUE_STAINED_GLASS_PANE),
    MILESTONE_15(15, 1_000_000_000L, "Omicron Rabbit", "9", 4, 0.004,
            "c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4", Material.BLUE_STAINED_GLASS_PANE),

    // Epic milestones (Purple)
    MILESTONE_16(16, 2_500_000_000L, "Pi Rabbit", "5", 10, 0.005,
            "d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4", Material.PURPLE_STAINED_GLASS_PANE),
    MILESTONE_17(17, 5_000_000_000L, "Rho Rabbit", "5", 10, 0.005,
            "e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4", Material.PURPLE_STAINED_GLASS_PANE),
    MILESTONE_18(18, 10_000_000_000L, "Sigma Rabbit", "5", 10, 0.005,
            "f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4", Material.PURPLE_STAINED_GLASS_PANE),
    MILESTONE_19(19, 25_000_000_000L, "Tau Rabbit", "5", 10, 0.005,
            "a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5", Material.PURPLE_STAINED_GLASS_PANE),
    MILESTONE_20(20, 50_000_000_000L, "Upsilon Rabbit", "5", 10, 0.005,
            "b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5", Material.PURPLE_STAINED_GLASS_PANE),

    // Legendary milestones (Orange)
    MILESTONE_21(21, 100_000_000_000L, "Phi Rabbit", "6", 0, 0.02,
            "c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5", Material.ORANGE_STAINED_GLASS_PANE),
    MILESTONE_22(22, 150_000_000_000L, "Chi Rabbit", "6", 0, 0.02,
            "d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5", Material.ORANGE_STAINED_GLASS_PANE),
    MILESTONE_23(23, 200_000_000_000L, "Psi Rabbit", "6", 0, 0.02,
            "e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5", Material.ORANGE_STAINED_GLASS_PANE),
    MILESTONE_24(24, 300_000_000_000L, "Omega Rabbit", "6", 0, 0.02,
            "f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5", Material.ORANGE_STAINED_GLASS_PANE);

    private final int number;
    private final long requiredSpent;
    private final String rabbitName;
    private final String colorCode;
    private final int chocolateBonus;
    private final double multiplierBonus;
    private final String textureId;
    private final Material glassPaneMaterial;

    public String getRomanNumeral() {
        return toRoman(number);
    }

    public String getFormattedRequirement() {
        return ChocolateFactoryHelper.formatChocolate(requiredSpent);
    }

    public boolean isUnlocked(long totalSpent) {
        return totalSpent >= requiredSpent;
    }

    public double getProgress(long totalSpent) {
        if (totalSpent >= requiredSpent) return 100.0;
        return (totalSpent / (double) requiredSpent) * 100.0;
    }

    public static ChocolateShopMilestone fromNumber(int number) {
        for (ChocolateShopMilestone milestone : values()) {
            if (milestone.number == number) {
                return milestone;
            }
        }
        return null;
    }

    private static String toRoman(int number) {
        if (number <= 0) return "I";
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[number / 1000] +
                hundreds[(number % 1000) / 100] +
                tens[(number % 100) / 10] +
                ones[number % 10];
    }
}
