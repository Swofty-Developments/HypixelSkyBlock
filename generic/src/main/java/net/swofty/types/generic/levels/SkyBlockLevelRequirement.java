package net.swofty.types.generic.levels;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.types.generic.levels.unlocks.CustomLevelUnlock;
import net.swofty.types.generic.levels.unlocks.SkyBlockLevelStatisticUnlock;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum SkyBlockLevelRequirement {
    LEVEL_0(0, false, List.of(), "§7", null, null),
    LEVEL_1(100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_2(200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_3(300, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new CustomLevelUnlock(CustomLevelAward.ACCESS_TO_COMMUNITY_SHOP)
    ), "§7", null, null),
    LEVEL_4(400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    LEVEL_5(500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build()),
            new CustomLevelUnlock(CustomLevelAward.ACCESS_TO_GARDEN),
            new CustomLevelUnlock(CustomLevelAward.ACCESS_TO_WARDROBE)
    ), "§7", null, null),
    LEVEL_6(600, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    ), "§7", null, null),
    ;

    private final int experience;
    private final boolean isMilestone;
    private final List<SkyBlockLevelUnlock> unlocks;
    private final String prefix;
    private final String prefixDisplay;
    private final Material prefixItem;

    SkyBlockLevelRequirement(int experience, boolean isMilestone, List<SkyBlockLevelUnlock> unlocks, String prefix, String prefixDisplay, Material prefixItem) {
        this.experience = experience;
        this.isMilestone = isMilestone;
        this.unlocks = unlocks;
        this.prefix = prefix;
        this.prefixDisplay = prefixDisplay;
        this.prefixItem = prefixItem;
    }

    public int getCumulativeExperience() {
        int cumulative = 0;
        for (SkyBlockLevelRequirement requirement : values()) {
            cumulative += requirement.experience;
            if (requirement == this) {
                return cumulative;
            }
        }
        return 0;
    }

    public SkyBlockLevelRequirement getNextMilestoneLevel() {
        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.isMilestone && requirement.ordinal() > ordinal()) {
                return requirement;
            }
        }
        return null;
    }

    public List<SkyBlockLevelStatisticUnlock> getStatisticUnlocks() {
        return unlocks.stream()
                .filter(unlock -> unlock instanceof SkyBlockLevelStatisticUnlock)
                .map(unlock -> (SkyBlockLevelStatisticUnlock) unlock)
                .toList();
    }

    public int asInt() {
        return ordinal();
    }

    public String getColor() {
        return "§7";
    }

    public @Nullable SkyBlockLevelRequirement getNextLevel() {
        return ordinal() + 1 < values().length ? values()[ordinal() + 1] : null;
    }

    @Override
    public String toString() {
        return String.valueOf(ordinal());
    }

    public Map<SkyBlockLevelRequirement, String> getPreviousPrefixChanges() {
        HashMap<SkyBlockLevelRequirement, String> toReturn = new HashMap<>();

        SkyBlockLevelRequirement last = LEVEL_0;
        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.ordinal() < ordinal() && !requirement.prefix.equals(last.prefix)) {
                toReturn.put(requirement, requirement.prefix);
            }
            last = requirement;
        }

        return toReturn;
    }

    public Map.Entry<SkyBlockLevelRequirement, String> getNextPrefixChange() {
        for (SkyBlockLevelRequirement requirement : values()) {
            if (requirement.ordinal() > ordinal() && !requirement.prefix.equals(prefix)) {
                return Map.entry(requirement, requirement.prefix);
            }
        }
        return null;
    }

    public static Map<SkyBlockLevelRequirement, String> getAllPrefixChanges() {
        HashMap<SkyBlockLevelRequirement, String> toReturn = new HashMap<>();

        SkyBlockLevelRequirement last = LEVEL_0;
        for (SkyBlockLevelRequirement requirement : values()) {
            if (!requirement.prefix.equals(last.prefix)) {
                toReturn.put(requirement, requirement.prefix);
            }
            last = requirement;
        }

        return toReturn;
    }

    public static SkyBlockLevelRequirement getFromTotalXP(double xp) {
        SkyBlockLevelRequirement toReturn = LEVEL_0;
        for (SkyBlockLevelRequirement requirement : values()) {
            if (xp < requirement.experience) {
                return toReturn;
            } else {
                toReturn = requirement;
            }
        }
        return toReturn;
    }
}