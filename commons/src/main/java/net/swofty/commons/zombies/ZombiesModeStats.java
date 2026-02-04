package net.swofty.commons.zombies;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ZombiesModeStats {

    private final Map<String, Long> roundsSurvived;
    private final Map<String, Long> wins;

    private final Map<String, Long> bestRound;
    private final Map<String, Long> kills;
    private final Map<String, Long> playersRevived;
    private final Map<String, Long> doorsOpened;
    private final Map<String, Long> windowsRepaired;
    private final Map<String, Long> timesKnockedDown;
    private final Map<String, Long> deaths;

    @Setter private long dailyResetTimestamp;
    @Setter private long weeklyResetTimestamp;
    @Setter private long monthlyResetTimestamp;

    public ZombiesModeStats() {
        this.roundsSurvived = new HashMap<>();
        this.wins = new HashMap<>();
        this.bestRound = new HashMap<>();
        this.kills = new HashMap<>();
        this.playersRevived = new HashMap<>();
        this.doorsOpened = new HashMap<>();
        this.windowsRepaired = new HashMap<>();
        this.timesKnockedDown = new HashMap<>();
        this.deaths = new HashMap<>();
        initializeResetTimestamps();
    }

    public ZombiesModeStats(Map<String, Long> roundsSurvived, Map<String, Long> wins,
                            Map<String, Long> bestRound, Map<String, Long> kills, Map<String, Long> deaths,
                            Map<String, Long> playersRevived, Map<String, Long> doorsOpened,
                            Map<String, Long> windowsRepaired, Map<String, Long> timesKnockedDown,
                           long dailyResetTimestamp, long weeklyResetTimestamp, long monthlyResetTimestamp) {
        this.roundsSurvived = new HashMap<>(roundsSurvived);
        this.wins = new HashMap<>(wins);
        this.bestRound = new HashMap<>(bestRound);
        this.kills = new HashMap<>(kills);
        this.deaths = new HashMap<>(deaths);
        this.playersRevived = new HashMap<>(playersRevived);
        this.doorsOpened = new HashMap<>(doorsOpened);
        this.windowsRepaired = new HashMap<>(windowsRepaired);
        this.timesKnockedDown = new HashMap<>(timesKnockedDown);
        this.dailyResetTimestamp = dailyResetTimestamp;
        this.weeklyResetTimestamp = weeklyResetTimestamp;
        this.monthlyResetTimestamp = monthlyResetTimestamp;
    }

    private void initializeResetTimestamps() {
        this.dailyResetTimestamp = computeNextDailyReset();
        this.weeklyResetTimestamp = computeNextWeeklyReset();
        this.monthlyResetTimestamp = computeNextMonthlyReset();
    }

    public static ZombiesModeStats empty() {
        return new ZombiesModeStats();
    }

    public void checkAndResetExpiredPeriods() {
        long now = System.currentTimeMillis();

        if (now >= dailyResetTimestamp) {
            resetPeriod(ZombiesLeaderboardPeriod.DAILY);
            dailyResetTimestamp = computeNextDailyReset();
        }

        if (now >= weeklyResetTimestamp) {
            resetPeriod(ZombiesLeaderboardPeriod.WEEKLY);
            weeklyResetTimestamp = computeNextWeeklyReset();
        }

        if (now >= monthlyResetTimestamp) {
            resetPeriod(ZombiesLeaderboardPeriod.MONTHLY);
            monthlyResetTimestamp = computeNextMonthlyReset();
        }
    }

    private static long computeNextDailyReset() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime nextReset = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return nextReset.toInstant().toEpochMilli();
    }

    private static long computeNextWeeklyReset() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime nextReset = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        return nextReset.toInstant().toEpochMilli();
    }

    private static long computeNextMonthlyReset() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime nextReset = now.with(TemporalAdjusters.firstDayOfNextMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        return nextReset.toInstant().toEpochMilli();
    }

    private String key(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return mode.getKey() + ":" + period.getKey();
    }

    // Getters for stats
    public long getRoundsSurvived(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return roundsSurvived.getOrDefault(key(mode, period), 0L);
    }

    public long getWins(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return wins.getOrDefault(key(mode, period), 0L);
    }

    public long getKills(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return kills.getOrDefault(key(mode, period), 0L);
    }

    public long getDeaths(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return deaths.getOrDefault(key(mode, period), 0L);
    }

    public long getPlayersRevived(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return playersRevived.getOrDefault(key(mode, period), 0L);
    }

    public long getDoorsOpened(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return doorsOpened.getOrDefault(key(mode, period), 0L);
    }

    public long getWindowsRepaired(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return windowsRepaired.getOrDefault(key(mode, period), 0L);
    }

    public long getTimesKnockedDown(ZombiesLeaderboardMode mode, ZombiesLeaderboardPeriod period) {
        return timesKnockedDown.getOrDefault(key(mode, period), 0L);
    }

    public long getBestRound(ZombiesLeaderboardMode mode) {
        return bestRound.getOrDefault(mode.getKey(), 0L);
    }

    // Record methods (for all periods)
    public void recordWin(ZombiesLeaderboardMode mode) {
        for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
            wins.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordKill(ZombiesLeaderboardMode mode) {
        for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
            kills.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordDeath(ZombiesLeaderboardMode mode) {
        for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
            deaths.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordRevive(ZombiesLeaderboardMode mode) {
        for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
            playersRevived.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordDoorOpened(ZombiesLeaderboardMode mode) {
        for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
            doorsOpened.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordWindowRepaired(ZombiesLeaderboardMode mode) {
        for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
            windowsRepaired.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordTimedKnockdown(ZombiesLeaderboardMode mode) {
        for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
            timesKnockedDown.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordRoundsSurvived(ZombiesLeaderboardMode mode, long rounds) {
        for (ZombiesLeaderboardPeriod period : ZombiesLeaderboardPeriod.values()) {
            roundsSurvived.merge(key(mode, period), rounds, Long::sum);
        }
    }

    public void updateBestRound(ZombiesLeaderboardMode mode, long round) {
        long current = getBestRound(mode);
        if (round > current) {
            bestRound.put(mode.getKey(), round);
        }
    }

    public void resetPeriod(ZombiesLeaderboardPeriod period) {
        for (ZombiesLeaderboardMode mode : ZombiesLeaderboardMode.values()) {
            String k = key(mode, period);
            roundsSurvived.remove(k);
            wins.remove(k);
            kills.remove(k);
            deaths.remove(k);
            playersRevived.remove(k);
            doorsOpened.remove(k);
            windowsRepaired.remove(k);
            timesKnockedDown.remove(k);
        }
    }

    public ZombiesModeStats copy() {
        return new ZombiesModeStats(roundsSurvived, wins, bestRound, kills, deaths,
                playersRevived, doorsOpened, windowsRepaired, timesKnockedDown,
                dailyResetTimestamp, weeklyResetTimestamp, monthlyResetTimestamp);
    }
}
