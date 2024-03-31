package net.swofty.types.generic.levels;

import lombok.Getter;
import net.swofty.types.generic.levels.unlocks.SkyBlockLevelStatisticUnlock;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public enum SkyBlockLevelRequirement {
    LEVEL_0(0, false, List.of()),
    LEVEL_1(100, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    )),
    LEVEL_2(200, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    )),
    LEVEL_3(300, true, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    )),
    LEVEL_4(400, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    )),
    LEVEL_5(500, false, List.of(
            new SkyBlockLevelStatisticUnlock(ItemStatistics.builder().with(
                    ItemStatistic.HEALTH, 5D
            ).build())
    )),
    ;

    private final int experience;
    private final boolean isMilestone;
    private final List<SkyBlockLevelUnlock> unlocks;

    SkyBlockLevelRequirement(int experience, boolean isMilestone, List<SkyBlockLevelUnlock> unlocks) {
        this.experience = experience;
        this.isMilestone = isMilestone;
        this.unlocks = unlocks;
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