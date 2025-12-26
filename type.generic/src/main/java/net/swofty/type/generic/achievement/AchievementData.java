package net.swofty.type.generic.achievement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class AchievementData {
    private Map<String, AchievementProgress> achievements = new HashMap<>();
    private Map<String, String> trackedAchievements = new HashMap<>();

    public AchievementProgress getOrCreate(String achievementId) {
        return achievements.computeIfAbsent(achievementId, k -> new AchievementProgress());
    }

    public AchievementProgress get(String achievementId) {
        return achievements.get(achievementId);
    }

    public boolean isCompleted(String achievementId) {
        AchievementProgress progress = achievements.get(achievementId);
        if (progress == null) return false;

        AchievementDefinition def = AchievementRegistry.get(achievementId);
        if (def == null) return progress.isCompleted();

        if (def.getType() == AchievementType.TIERED) {
            return progress.getCurrentTier() >= 1;
        }
        return progress.isCompleted();
    }

    public boolean isFullyCompleted(String achievementId) {
        AchievementProgress progress = achievements.get(achievementId);
        if (progress == null) return false;

        AchievementDefinition def = AchievementRegistry.get(achievementId);
        if (def == null) return progress.isCompleted();

        if (def.getType() == AchievementType.TIERED) {
            return progress.getCurrentTier() >= def.getMaxTier();
        }
        return progress.isCompleted();
    }

    public int getCurrentTier(String achievementId) {
        AchievementProgress progress = achievements.get(achievementId);
        return progress != null ? progress.getCurrentTier() : 0;
    }

    public int getProgress(String achievementId) {
        AchievementProgress progress = achievements.get(achievementId);
        return progress != null ? progress.getCurrentProgress() : 0;
    }

    public int getTotalPoints() {
        int total = 0;
        for (Map.Entry<String, AchievementProgress> entry : achievements.entrySet()) {
            AchievementDefinition def = AchievementRegistry.get(entry.getKey());
            if (def == null) continue;

            AchievementProgress progress = entry.getValue();
            if (def.getType() == AchievementType.TIERED) {
                total += def.getPointsUpToTier(progress.getCurrentTier());
            } else if (progress.isCompleted()) {
                total += def.getPoints();
            }
        }
        return total;
    }

    public int getTotalPoints(AchievementCategory category) {
        int total = 0;
        for (AchievementDefinition def : AchievementRegistry.getByCategory(category)) {
            AchievementProgress progress = achievements.get(def.getId());
            if (progress == null) continue;

            if (def.getType() == AchievementType.TIERED) {
                total += def.getPointsUpToTier(progress.getCurrentTier());
            } else if (progress.isCompleted()) {
                total += def.getPoints();
            }
        }
        return total;
    }

    public int getPoints(AchievementCategory category, AchievementType type) {
        int total = 0;
        for (AchievementDefinition def : AchievementRegistry.getByCategory(category, type)) {
            AchievementProgress progress = achievements.get(def.getId());
            if (progress == null) continue;

            if (def.getType() == AchievementType.TIERED) {
                total += def.getPointsUpToTier(progress.getCurrentTier());
            } else if (progress.isCompleted()) {
                total += def.getPoints();
            }
        }
        return total;
    }

    public int getUnlockedCount(AchievementCategory category) {
        int count = 0;
        for (AchievementDefinition def : AchievementRegistry.getByCategory(category)) {
            AchievementProgress progress = achievements.get(def.getId());
            if (progress == null) continue;

            if (def.getType() == AchievementType.TIERED) {
                count += progress.getCurrentTier();
            } else if (progress.isCompleted()) {
                count++;
            }
        }
        return count;
    }

    public int getUnlockedCount(AchievementCategory category, AchievementType type) {
        int count = 0;
        for (AchievementDefinition def : AchievementRegistry.getByCategory(category, type)) {
            AchievementProgress progress = achievements.get(def.getId());
            if (progress == null) continue;

            if (def.getType() == AchievementType.TIERED) {
                count += progress.getCurrentTier();
            } else if (progress.isCompleted()) {
                count++;
            }
        }
        return count;
    }

    public double getCompletionPercentage(AchievementCategory category) {
        int total = AchievementRegistry.getTotalPoints(category);
        if (total == 0) return 0.0;
        return (double) getTotalPoints(category) / total * 100.0;
    }

    public int getTotalUnlockedCount() {
        int count = 0;
        for (AchievementCategory category : AchievementCategory.values()) {
            count += getUnlockedCount(category);
        }
        return count;
    }

    public void setTrackedAchievement(AchievementCategory category, String achievementId) {
        if (achievementId == null) {
            trackedAchievements.remove(category.getConfigKey());
        } else {
            trackedAchievements.put(category.getConfigKey(), achievementId);
        }
    }

    public String getTrackedAchievement(AchievementCategory category) {
        return trackedAchievements.get(category.getConfigKey());
    }

    public boolean isTracked(String achievementId) {
        AchievementDefinition def = AchievementRegistry.get(achievementId);
        if (def == null) return false;

        String tracked = trackedAchievements.get(def.getCategory().getConfigKey());
        return achievementId.equals(tracked);
    }

    public boolean toggleTracking(String achievementId) {
        AchievementDefinition def = AchievementRegistry.get(achievementId);
        if (def == null || def.getType() != AchievementType.TIERED) {
            return false;
        }

        if (isTracked(achievementId)) {
            setTrackedAchievement(def.getCategory(), null);
            return false;
        } else {
            setTrackedAchievement(def.getCategory(), achievementId);
            return true;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AchievementProgress {
        private int currentProgress = 0;
        private int currentTier = 0;
        private long unlockedTimestamp = 0;
        private boolean completed = false;

        public boolean addProgress(AchievementDefinition def, int amount) {
            currentProgress += amount;

            if (def.getType() == AchievementType.TIERED) {
                return checkTierProgress(def);
            } else {
                return checkChallengeProgress(def);
            }
        }

        private boolean checkTierProgress(AchievementDefinition def) {
            boolean newTierUnlocked = false;
            for (int tier = currentTier + 1; tier <= def.getMaxTier(); tier++) {
                if (currentProgress >= def.getGoalForTier(tier)) {
                    currentTier = tier;
                    unlockedTimestamp = System.currentTimeMillis();
                    newTierUnlocked = true;
                } else {
                    break;
                }
            }
            return newTierUnlocked;
        }

        private boolean checkChallengeProgress(AchievementDefinition def) {
            if (!completed && currentProgress >= def.getGoal()) {
                completed = true;
                unlockedTimestamp = System.currentTimeMillis();
                return true;
            }
            return false;
        }

        public void complete() {
            completed = true;
            unlockedTimestamp = System.currentTimeMillis();
        }

        public void resetProgress() {
            currentProgress = 0;
        }
    }
}
