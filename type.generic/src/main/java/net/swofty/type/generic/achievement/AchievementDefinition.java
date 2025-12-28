package net.swofty.type.generic.achievement;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AchievementDefinition {
    private final String id;
    private final String name;
    private final String description;
    private final AchievementType type;
    private final AchievementCategory category;
    private final SeasonType season;
    private final String trigger;
    private final int goal;
    private final int points;
    private final List<AchievementTier> tiers;
    private final boolean perGame;
    private final boolean customCheck;

    public int getTotalPoints() {
        if (type == AchievementType.TIERED && tiers != null) {
            return tiers.stream().mapToInt(AchievementTier::getPoints).sum();
        }
        return points;
    }

    public int getGoalForTier(int tier) {
        if (type != AchievementType.TIERED || tiers == null) return goal;
        return tiers.stream()
                .filter(t -> t.getTier() == tier)
                .findFirst()
                .map(AchievementTier::getGoal)
                .orElse(0);
    }

    public int getPointsForTier(int tier) {
        if (type != AchievementType.TIERED || tiers == null) return points;
        return tiers.stream()
                .filter(t -> t.getTier() == tier)
                .findFirst()
                .map(AchievementTier::getPoints)
                .orElse(0);
    }

    public int getMaxTier() {
        if (type != AchievementType.TIERED || tiers == null) return 1;
        return tiers.stream().mapToInt(AchievementTier::getTier).max().orElse(1);
    }

    public int getPointsUpToTier(int tier) {
        if (type != AchievementType.TIERED || tiers == null) {
            return tier >= 1 ? points : 0;
        }
        return tiers.stream()
                .filter(t -> t.getTier() <= tier)
                .mapToInt(AchievementTier::getPoints)
                .sum();
    }

    public boolean isForSeason(SeasonType seasonType) {
        return type == AchievementType.SEASONAL && season == seasonType;
    }
}
