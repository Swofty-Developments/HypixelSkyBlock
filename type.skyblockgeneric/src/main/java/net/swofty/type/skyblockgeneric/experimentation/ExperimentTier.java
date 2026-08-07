package net.swofty.type.skyblockgeneric.experimentation;

import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.List;

public enum ExperimentTier {
    HIGH("High", 20, 3, 1_500, 100, 20, Material.LIME_DYE),
    GRAND("Grand", 25, 5, 2_500, 150, 18, Material.YELLOW_DYE),
    SUPREME("Supreme", 30, 7, 3_500, 200, 16, Material.ORANGE_DYE),
    TRANSCENDENT("Transcendent", 35, 8, 4_500, 250, 14, Material.RED_DYE),
    METAPHYSICAL("Metaphysical", 40, 10, 6_000, 300, 12, Material.PURPLE_DYE);

    private final String displayName;
    private final int requiredEnchantingLevel;
    private final int colorCount;
    private final int xpPerStep;
    private final int superPairsXpPerPair;
    private final int baseClicks;
    private final Material icon;

    ExperimentTier(String displayName, int requiredEnchantingLevel, int colorCount, int xpPerStep,
                   int superPairsXpPerPair, int baseClicks, Material icon) {
        this.displayName = displayName;
        this.requiredEnchantingLevel = requiredEnchantingLevel;
        this.colorCount = colorCount;
        this.xpPerStep = xpPerStep;
        this.superPairsXpPerPair = superPairsXpPerPair;
        this.baseClicks = baseClicks;
        this.icon = icon;
    }

    public String displayName() {
        return displayName;
    }

    public int requiredEnchantingLevel() {
        return requiredEnchantingLevel;
    }

    public int colorCount() {
        return colorCount;
    }

    public int xpPerStep() {
        return xpPerStep;
    }

    public int superPairsXpPerPair() {
        return superPairsXpPerPair;
    }

    public int baseClicks() {
        return baseClicks;
    }

    public Material icon() {
        return icon;
    }

    public boolean isUnlocked(SkyBlockPlayer player) {
        return player.getSkills().getCurrentLevel(SkillCategories.ENCHANTING) >= requiredEnchantingLevel;
    }

    public List<Integer> slotsForColor(int color) {
        if (color < 0 || color >= colorCount) throw new IllegalArgumentException("Invalid experiment color: " + color);

        if (colorCount <= 7) {
            int[] rows = switch (colorCount) {
                case 3 -> new int[]{12, 21, 30};
                case 5 -> new int[]{11, 20, 29};
                default -> new int[]{10, 19, 28};
            };
            return Arrays.stream(rows).map(row -> row + color).boxed().toList();
        }

        int columns = colorCount == 8 ? 4 : 5;
        int group = color / columns;
        int localColor = color % columns;
        int[] rows = colorCount == 8
                ? (group == 0 ? new int[]{11, 20} : new int[]{30, 39})
                : (group == 0 ? new int[]{11, 20} : new int[]{29, 38});
        return Arrays.stream(rows).map(row -> row + localColor).boxed().toList();
    }

    public static ExperimentTier fromName(String name) {
        return Arrays.stream(values())
                .filter(tier -> tier.name().equalsIgnoreCase(name) || tier.displayName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown experiment tier: " + name));
    }
}
