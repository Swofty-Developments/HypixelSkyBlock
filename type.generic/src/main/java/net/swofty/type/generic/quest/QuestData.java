package net.swofty.type.generic.quest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@NoArgsConstructor
public class QuestData {
    private Map<String, QuestProgress> activeQuests = new HashMap<>();
    private Map<String, Long> completedQuests = new HashMap<>();

    @Setter
    private long dailyResetTimestamp = 0;
    @Setter
    private long weeklyResetTimestamp = 0;

    @Setter
    private int dailyChallengesCompleted = 0;

    private Set<String> todaysChallengeGames = new HashSet<>();

    public static final int MAX_DAILY_CHALLENGES = 15;

    public void checkAndResetExpired() {
        long now = System.currentTimeMillis();

        if (dailyResetTimestamp == 0 || now >= dailyResetTimestamp) {
            resetDaily();
            dailyResetTimestamp = calculateNextMidnightUTC();
        }

        if (weeklyResetTimestamp == 0 || now >= weeklyResetTimestamp) {
            resetWeekly();
            weeklyResetTimestamp = calculateNextThursdayMidnightUTC();
        }
    }

    private void resetDaily() {
        activeQuests.entrySet().removeIf(entry -> {
            QuestDefinition def = QuestRegistry.get(entry.getKey());
            return def != null && def.getType() == QuestType.DAILY;
        });
        completedQuests.entrySet().removeIf(entry -> {
            QuestDefinition def = QuestRegistry.get(entry.getKey());
            return def != null && def.getType() == QuestType.DAILY;
        });

        dailyChallengesCompleted = 0;
        todaysChallengeGames.clear();
    }

    private void resetWeekly() {
        activeQuests.entrySet().removeIf(entry -> {
            QuestDefinition def = QuestRegistry.get(entry.getKey());
            return def != null && def.getType() == QuestType.WEEKLY;
        });
        completedQuests.entrySet().removeIf(entry -> {
            QuestDefinition def = QuestRegistry.get(entry.getKey());
            return def != null && def.getType() == QuestType.WEEKLY;
        });
    }

    public static long calculateNextMidnightUTC() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(ZoneOffset.UTC);
        return nextMidnight.toInstant().toEpochMilli();
    }

    public static long calculateNextThursdayMidnightUTC() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        LocalDate today = now.toLocalDate();

        int daysUntilThursday = (DayOfWeek.THURSDAY.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
        if (daysUntilThursday == 0) {
            daysUntilThursday = 7;
        }

        LocalDate nextThursday = today.plusDays(daysUntilThursday);
        ZonedDateTime nextThursdayMidnight = nextThursday.atStartOfDay(ZoneOffset.UTC);
        return nextThursdayMidnight.toInstant().toEpochMilli();
    }

    public void startQuest(String questId) {
        if (!activeQuests.containsKey(questId) && !completedQuests.containsKey(questId)) {
            activeQuests.put(questId, new QuestProgress(questId, 0, System.currentTimeMillis()));
        }
    }

    public boolean addProgress(String questId, int amount) {
        QuestProgress progress = activeQuests.get(questId);
        if (progress == null) return false;

        QuestDefinition def = QuestRegistry.get(questId);
        if (def == null) return false;

        progress.setCurrentProgress(progress.getCurrentProgress() + amount);

        if (progress.getCurrentProgress() >= def.getGoal()) {
            completeQuest(questId);
            return true;
        }
        return false;
    }

    public void completeQuest(String questId) {
        activeQuests.remove(questId);
        completedQuests.put(questId, System.currentTimeMillis());
    }

    public boolean isCompleted(String questId) {
        return completedQuests.containsKey(questId);
    }

    public boolean isActive(String questId) {
        return activeQuests.containsKey(questId);
    }

    public int getProgress(String questId) {
        QuestProgress progress = activeQuests.get(questId);
        return progress != null ? progress.getCurrentProgress() : 0;
    }

    public boolean canCompleteChallenge() {
        return dailyChallengesCompleted < MAX_DAILY_CHALLENGES;
    }

    public boolean hasCompletedChallengeInGame(String gameId) {
        return todaysChallengeGames.contains(gameId);
    }

    public void markChallengeCompleted(String gameId) {
        todaysChallengeGames.add(gameId);
        dailyChallengesCompleted++;
    }

    public int getRemainingChallenges() {
        return MAX_DAILY_CHALLENGES - dailyChallengesCompleted;
    }

    public long getTimeUntilDailyReset() {
        return Math.max(0, dailyResetTimestamp - System.currentTimeMillis());
    }

    public long getTimeUntilWeeklyReset() {
        return Math.max(0, weeklyResetTimestamp - System.currentTimeMillis());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestProgress {
        private String questId;
        private int currentProgress;
        private long startedTimestamp;
    }
}
