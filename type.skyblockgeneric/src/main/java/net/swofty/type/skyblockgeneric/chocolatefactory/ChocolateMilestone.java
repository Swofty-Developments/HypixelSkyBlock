package net.swofty.type.skyblockgeneric.chocolatefactory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents chocolate factory milestones.
 * Each milestone unlocks a special rabbit when reaching a certain all-time chocolate threshold.
 */
@Getter
@AllArgsConstructor
public enum ChocolateMilestone {
    // Common milestones (White)
    MILESTONE_1(1, 1_000L, "Almond Amaretto Rabbit", "f", 1, 0.002,
            "b1b93d4e792bbf1522915b4c5f0444659fa3614c45a2b2922e61a888e61c714e", Material.WHITE_STAINED_GLASS_PANE),
    MILESTONE_2(2, 10_000L, "Buttercream Blossom Rabbit", "f", 1, 0.002,
            "137f6bd5e7c4e8fb22c88eb09537079bd3d4c144cf5a376ce6f2030110bd1680", Material.WHITE_STAINED_GLASS_PANE),
    MILESTONE_3(3, 100_000L, "Cocoa Comet Rabbit", "f", 1, 0.002,
            "6b38fa0a62113539b18fc31d1e04be34d47b7d500ef790b53f83d81c479ed29d", Material.WHITE_STAINED_GLASS_PANE),
    MILESTONE_4(4, 1_000_000L, "Dulce Drizzle Rabbit", "f", 1, 0.002,
            "e9eb72f7ed9e8c37a4c9ed9e8b9afc77f6ca4e01f2a76ebf9a8e8f7b2c3d4e5f", Material.WHITE_STAINED_GLASS_PANE),
    MILESTONE_5(5, 2_500_000L, "Espresso Eclair Rabbit", "f", 1, 0.002,
            "a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2", Material.WHITE_STAINED_GLASS_PANE),

    // Uncommon milestones (Lime/Green)
    MILESTONE_6(6, 5_000_000L, "Fudge Fountain Rabbit", "a", 2, 0.003,
            "c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2", Material.LIME_STAINED_GLASS_PANE),
    MILESTONE_7(7, 10_000_000L, "Ginger Glaze Rabbit", "a", 2, 0.003,
            "d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2", Material.LIME_STAINED_GLASS_PANE),
    MILESTONE_8(8, 25_000_000L, "Honey Hazelnut Rabbit", "a", 2, 0.003,
            "e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2", Material.LIME_STAINED_GLASS_PANE),
    MILESTONE_9(9, 50_000_000L, "Icing Ivy Rabbit", "a", 2, 0.003,
            "f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2", Material.LIME_STAINED_GLASS_PANE),
    MILESTONE_10(10, 100_000_000L, "Jasmine Jello Rabbit", "a", 2, 0.003,
            "a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3", Material.LIME_STAINED_GLASS_PANE),

    // Rare milestones (Blue)
    MILESTONE_11(11, 250_000_000L, "Kiwi Kiss Rabbit", "9", 4, 0.004,
            "b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3", Material.BLUE_STAINED_GLASS_PANE),
    MILESTONE_12(12, 500_000_000L, "Lavender Lemon Rabbit", "9", 4, 0.004,
            "c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3", Material.BLUE_STAINED_GLASS_PANE),
    MILESTONE_13(13, 1_000_000_000L, "Maple Mirage Rabbit", "9", 4, 0.004,
            "d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3", Material.BLUE_STAINED_GLASS_PANE),
    MILESTONE_14(14, 2_500_000_000L, "Nougat Nebula Rabbit", "9", 4, 0.004,
            "e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3", Material.BLUE_STAINED_GLASS_PANE),
    MILESTONE_15(15, 5_000_000_000L, "Orange Obsidian Rabbit", "9", 4, 0.004,
            "f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3", Material.BLUE_STAINED_GLASS_PANE),

    // Epic milestones (Purple)
    MILESTONE_16(16, 10_000_000_000L, "Peppermint Pearl Rabbit", "5", 10, 0.005,
            "a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4", Material.PURPLE_STAINED_GLASS_PANE),
    MILESTONE_17(17, 25_000_000_000L, "Quince Quark Rabbit", "5", 10, 0.005,
            "b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4", Material.PURPLE_STAINED_GLASS_PANE),
    MILESTONE_18(18, 50_000_000_000L, "Raspberry Ripple Rabbit", "5", 10, 0.005,
            "c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4", Material.PURPLE_STAINED_GLASS_PANE),
    MILESTONE_19(19, 100_000_000_000L, "Saffron Swirl Rabbit", "5", 10, 0.005,
            "d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4", Material.PURPLE_STAINED_GLASS_PANE),
    MILESTONE_20(20, 150_000_000_000L, "Toffee Tulip Rabbit", "5", 10, 0.005,
            "e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6e7f8a9b0c1d2e3f4", Material.PURPLE_STAINED_GLASS_PANE),

    // Legendary milestones (Orange) - These only give multiplier, no flat chocolate
    MILESTONE_21(21, 200_000_000_000L, "Ube Unicorn Rabbit", "6", 0, 0.02,
            "f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4", Material.ORANGE_STAINED_GLASS_PANE),
    MILESTONE_22(22, 300_000_000_000L, "Vanilla Vortex Rabbit", "6", 0, 0.02,
            "a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5", Material.ORANGE_STAINED_GLASS_PANE),
    MILESTONE_23(23, 400_000_000_000L, "Walnut Whirl Rabbit", "6", 0, 0.02,
            "b4c5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5", Material.ORANGE_STAINED_GLASS_PANE),
    MILESTONE_24(24, 500_000_000_000L, "Xocolatl Xenon Rabbit", "6", 0, 0.02,
            "c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b3c4d5", Material.ORANGE_STAINED_GLASS_PANE);

    private final int number;
    private final long requiredChocolate;
    private final String rabbitName;
    private final String colorCode;
    private final int chocolateBonus;
    private final double multiplierBonus;
    private final String textureId;
    private final Material glassPaneMaterial;

    private static final Map<Integer, ChocolateMilestone> MILESTONES_BY_NUMBER = createMilestonesByNumber();

    public String getRomanNumeral() {
        return StringUtility.getAsRomanNumeral(number);
    }

    public String getFormattedRequirement() {
        return ChocolateFactoryHelper.formatChocolate(requiredChocolate);
    }

    public boolean isUnlocked(long allTimeChocolate) {
        return allTimeChocolate >= requiredChocolate;
    }

    public double getProgress(long allTimeChocolate) {
        if (allTimeChocolate >= requiredChocolate) return 100.0;
        return (allTimeChocolate / (double) requiredChocolate) * 100.0;
    }

    public static ChocolateMilestone fromNumber(int number) {
        return MILESTONES_BY_NUMBER.get(number);
    }

    private static Map<Integer, ChocolateMilestone> createMilestonesByNumber() {
        Map<Integer, ChocolateMilestone> milestonesByNumber = new HashMap<>();
        for (ChocolateMilestone milestone : values()) {
            milestonesByNumber.put(milestone.number, milestone);
        }
        return milestonesByNumber;
    }
}
