package net.swofty.commons.murdermystery;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MurderMysteryModeStats {
    // General stats
    private final Map<String, Long> wins;
    private final Map<String, Long> kills;
    private final Map<String, Long> gamesPlayed;

    // Kill types
    private final Map<String, Long> bowKills;
    private final Map<String, Long> knifeKills;
    private final Map<String, Long> thrownKnifeKills;
    private final Map<String, Long> trapKills;

    // Role-specific stats
    private final Map<String, Long> detectiveWins;
    private final Map<String, Long> murdererWins;
    private final Map<String, Long> killsAsHero;
    private final Map<String, Long> killsAsMurderer;

    // Infection mode stats
    private final Map<String, Long> survivorWins;
    private final Map<String, Long> alphaWins;
    private final Map<String, Long> killsAsInfected;
    private final Map<String, Long> killsAsSurvivor;
    private final Map<String, Long> timeSurvived;

    // Time records (stored as millis, lower is better)
    private final Map<String, Long> quickestDetectiveWin;
    private final Map<String, Long> quickestMurdererWin;

    // Tokens earned
    private final Map<String, Long> tokens;

    @Setter private long weeklyResetTimestamp;
    @Setter private long monthlyResetTimestamp;

    public MurderMysteryModeStats() {
        this.wins = new HashMap<>();
        this.kills = new HashMap<>();
        this.gamesPlayed = new HashMap<>();
        this.bowKills = new HashMap<>();
        this.knifeKills = new HashMap<>();
        this.thrownKnifeKills = new HashMap<>();
        this.trapKills = new HashMap<>();
        this.detectiveWins = new HashMap<>();
        this.murdererWins = new HashMap<>();
        this.killsAsHero = new HashMap<>();
        this.killsAsMurderer = new HashMap<>();
        this.survivorWins = new HashMap<>();
        this.alphaWins = new HashMap<>();
        this.killsAsInfected = new HashMap<>();
        this.killsAsSurvivor = new HashMap<>();
        this.timeSurvived = new HashMap<>();
        this.quickestDetectiveWin = new HashMap<>();
        this.quickestMurdererWin = new HashMap<>();
        this.tokens = new HashMap<>();
        initializeResetTimestamps();
    }

    public MurderMysteryModeStats(
            Map<String, Long> wins, Map<String, Long> kills, Map<String, Long> gamesPlayed,
            Map<String, Long> bowKills, Map<String, Long> knifeKills, Map<String, Long> thrownKnifeKills,
            Map<String, Long> trapKills, Map<String, Long> detectiveWins, Map<String, Long> murdererWins,
            Map<String, Long> killsAsHero, Map<String, Long> killsAsMurderer,
            Map<String, Long> survivorWins, Map<String, Long> alphaWins,
            Map<String, Long> killsAsInfected, Map<String, Long> killsAsSurvivor, Map<String, Long> timeSurvived,
            Map<String, Long> quickestDetectiveWin, Map<String, Long> quickestMurdererWin,
            Map<String, Long> tokens,
            long weeklyResetTimestamp, long monthlyResetTimestamp) {
        this.wins = new HashMap<>(wins);
        this.kills = new HashMap<>(kills);
        this.gamesPlayed = new HashMap<>(gamesPlayed);
        this.bowKills = new HashMap<>(bowKills);
        this.knifeKills = new HashMap<>(knifeKills);
        this.thrownKnifeKills = new HashMap<>(thrownKnifeKills);
        this.trapKills = new HashMap<>(trapKills);
        this.detectiveWins = new HashMap<>(detectiveWins);
        this.murdererWins = new HashMap<>(murdererWins);
        this.killsAsHero = new HashMap<>(killsAsHero);
        this.killsAsMurderer = new HashMap<>(killsAsMurderer);
        this.survivorWins = new HashMap<>(survivorWins);
        this.alphaWins = new HashMap<>(alphaWins);
        this.killsAsInfected = new HashMap<>(killsAsInfected);
        this.killsAsSurvivor = new HashMap<>(killsAsSurvivor);
        this.timeSurvived = new HashMap<>(timeSurvived);
        this.quickestDetectiveWin = new HashMap<>(quickestDetectiveWin);
        this.quickestMurdererWin = new HashMap<>(quickestMurdererWin);
        this.tokens = new HashMap<>(tokens);
        this.weeklyResetTimestamp = weeklyResetTimestamp;
        this.monthlyResetTimestamp = monthlyResetTimestamp;
    }

    private void initializeResetTimestamps() {
        this.weeklyResetTimestamp = computeNextWeeklyReset();
        this.monthlyResetTimestamp = computeNextMonthlyReset();
    }

    public static MurderMysteryModeStats empty() {
        return new MurderMysteryModeStats();
    }

    public void checkAndResetExpiredPeriods() {
        long now = System.currentTimeMillis();

        if (now >= weeklyResetTimestamp) {
            resetPeriod(MurderMysteryLeaderboardPeriod.WEEKLY);
            weeklyResetTimestamp = computeNextWeeklyReset();
        }

        if (now >= monthlyResetTimestamp) {
            resetPeriod(MurderMysteryLeaderboardPeriod.MONTHLY);
            monthlyResetTimestamp = computeNextMonthlyReset();
        }
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

    private String key(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return mode.getKey() + ":" + period.getKey();
    }

    // Getters for stats
    public long getWins(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return wins.getOrDefault(key(mode, period), 0L);
    }

    public long getKills(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return kills.getOrDefault(key(mode, period), 0L);
    }

    public long getGamesPlayed(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return gamesPlayed.getOrDefault(key(mode, period), 0L);
    }

    public long getBowKills(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return bowKills.getOrDefault(key(mode, period), 0L);
    }

    public long getKnifeKills(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return knifeKills.getOrDefault(key(mode, period), 0L);
    }

    public long getThrownKnifeKills(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return thrownKnifeKills.getOrDefault(key(mode, period), 0L);
    }

    public long getTrapKills(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return trapKills.getOrDefault(key(mode, period), 0L);
    }

    public long getDetectiveWins(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return detectiveWins.getOrDefault(key(mode, period), 0L);
    }

    public long getMurdererWins(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return murdererWins.getOrDefault(key(mode, period), 0L);
    }

    public long getKillsAsHero(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return killsAsHero.getOrDefault(key(mode, period), 0L);
    }

    public long getKillsAsMurderer(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return killsAsMurderer.getOrDefault(key(mode, period), 0L);
    }

    public long getSurvivorWins(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return survivorWins.getOrDefault(key(mode, period), 0L);
    }

    public long getAlphaWins(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return alphaWins.getOrDefault(key(mode, period), 0L);
    }

    public long getKillsAsInfected(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return killsAsInfected.getOrDefault(key(mode, period), 0L);
    }

    public long getKillsAsSurvivor(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return killsAsSurvivor.getOrDefault(key(mode, period), 0L);
    }

    public long getTimeSurvived(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return timeSurvived.getOrDefault(key(mode, period), 0L);
    }

    public long getQuickestDetectiveWin(MurderMysteryLeaderboardMode mode) {
        return quickestDetectiveWin.getOrDefault(mode.getKey(), 0L);
    }

    public long getQuickestMurdererWin(MurderMysteryLeaderboardMode mode) {
        return quickestMurdererWin.getOrDefault(mode.getKey(), 0L);
    }

    public long getTokens(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        return tokens.getOrDefault(key(mode, period), 0L);
    }

    // Add methods for incrementing stats
    private void addStat(Map<String, Long> map, MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        map.merge(key(mode, period), 1L, Long::sum);
    }

    private void addStat(Map<String, Long> map, MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period, long amount) {
        map.merge(key(mode, period), amount, Long::sum);
    }

    public void addWin(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(wins, mode, period);
    }

    public void addKill(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(kills, mode, period);
    }

    public void addGamePlayed(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(gamesPlayed, mode, period);
    }

    public void addBowKill(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(bowKills, mode, period);
    }

    public void addKnifeKill(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(knifeKills, mode, period);
    }

    public void addThrownKnifeKill(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(thrownKnifeKills, mode, period);
    }

    public void addTrapKill(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(trapKills, mode, period);
    }

    public void addDetectiveWin(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(detectiveWins, mode, period);
    }

    public void addMurdererWin(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(murdererWins, mode, period);
    }

    public void addKillAsHero(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(killsAsHero, mode, period);
    }

    public void addKillAsMurderer(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(killsAsMurderer, mode, period);
    }

    public void addSurvivorWin(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(survivorWins, mode, period);
    }

    public void addAlphaWin(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(alphaWins, mode, period);
    }

    public void addKillAsInfected(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(killsAsInfected, mode, period);
    }

    public void addKillAsSurvivor(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period) {
        addStat(killsAsSurvivor, mode, period);
    }

    public void addTimeSurvived(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period, long millis) {
        addStat(timeSurvived, mode, period, millis);
    }

    public void addTokens(MurderMysteryLeaderboardMode mode, MurderMysteryLeaderboardPeriod period, long amount) {
        addStat(tokens, mode, period, amount);
    }

    public void setQuickestDetectiveWin(MurderMysteryLeaderboardMode mode, long millis) {
        long current = quickestDetectiveWin.getOrDefault(mode.getKey(), Long.MAX_VALUE);
        if (millis < current) {
            quickestDetectiveWin.put(mode.getKey(), millis);
        }
    }

    public void setQuickestMurdererWin(MurderMysteryLeaderboardMode mode, long millis) {
        long current = quickestMurdererWin.getOrDefault(mode.getKey(), Long.MAX_VALUE);
        if (millis < current) {
            quickestMurdererWin.put(mode.getKey(), millis);
        }
    }

    // Record methods that add to all periods
    public void recordWin(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addWin(mode, period);
        }
    }

    public void recordKill(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addKill(mode, period);
        }
    }

    public void recordGamePlayed(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addGamePlayed(mode, period);
        }
    }

    public void recordBowKill(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addBowKill(mode, period);
        }
        recordKill(mode);
    }

    public void recordKnifeKill(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addKnifeKill(mode, period);
        }
        recordKill(mode);
    }

    public void recordThrownKnifeKill(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addThrownKnifeKill(mode, period);
        }
        recordKill(mode);
    }

    public void recordTrapKill(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addTrapKill(mode, period);
        }
        recordKill(mode);
    }

    public void recordDetectiveWin(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addDetectiveWin(mode, period);
        }
        recordWin(mode);
    }

    public void recordMurdererWin(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addMurdererWin(mode, period);
        }
        recordWin(mode);
    }

    public void recordKillAsHero(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addKillAsHero(mode, period);
        }
    }

    public void recordKillAsMurderer(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addKillAsMurderer(mode, period);
        }
    }

    public void recordSurvivorWin(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addSurvivorWin(mode, period);
        }
        recordWin(mode);
    }

    public void recordAlphaWin(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addAlphaWin(mode, period);
        }
        recordWin(mode);
    }

    public void recordKillAsInfected(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addKillAsInfected(mode, period);
        }
        recordKill(mode);
    }

    public void recordKillAsSurvivor(MurderMysteryLeaderboardMode mode) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addKillAsSurvivor(mode, period);
        }
        recordKill(mode);
    }

    public void recordTimeSurvived(MurderMysteryLeaderboardMode mode, long millis) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addTimeSurvived(mode, period, millis);
        }
    }

    public void recordTokens(MurderMysteryLeaderboardMode mode, long amount) {
        for (MurderMysteryLeaderboardPeriod period : MurderMysteryLeaderboardPeriod.values()) {
            addTokens(mode, period, amount);
        }
    }

    public void resetPeriod(MurderMysteryLeaderboardPeriod period) {
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            String k = key(mode, period);
            wins.remove(k);
            kills.remove(k);
            gamesPlayed.remove(k);
            bowKills.remove(k);
            knifeKills.remove(k);
            thrownKnifeKills.remove(k);
            trapKills.remove(k);
            detectiveWins.remove(k);
            murdererWins.remove(k);
            killsAsHero.remove(k);
            killsAsMurderer.remove(k);
            survivorWins.remove(k);
            alphaWins.remove(k);
            killsAsInfected.remove(k);
            killsAsSurvivor.remove(k);
            timeSurvived.remove(k);
            tokens.remove(k);
        }
    }

    // Get total stats across all modes for a period
    public long getTotalWins(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getWins(mode, period);
        }
        return total;
    }

    public long getTotalKills(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getKills(mode, period);
        }
        return total;
    }

    public long getTotalGamesPlayed(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getGamesPlayed(mode, period);
        }
        return total;
    }

    public long getTotalBowKills(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getBowKills(mode, period);
        }
        return total;
    }

    public long getTotalKnifeKills(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getKnifeKills(mode, period);
        }
        return total;
    }

    public long getTotalThrownKnifeKills(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getThrownKnifeKills(mode, period);
        }
        return total;
    }

    public long getTotalTrapKills(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getTrapKills(mode, period);
        }
        return total;
    }

    public long getTotalDetectiveWins(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getDetectiveWins(mode, period);
        }
        return total;
    }

    public long getTotalMurdererWins(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getMurdererWins(mode, period);
        }
        return total;
    }

    public long getTotalKillsAsHero(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getKillsAsHero(mode, period);
        }
        return total;
    }

    public long getTotalTokens(MurderMysteryLeaderboardPeriod period) {
        long total = 0;
        for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
            total += getTokens(mode, period);
        }
        return total;
    }

    public MurderMysteryModeStats copy() {
        return new MurderMysteryModeStats(
                wins, kills, gamesPlayed, bowKills, knifeKills, thrownKnifeKills, trapKills,
                detectiveWins, murdererWins, killsAsHero, killsAsMurderer, survivorWins, alphaWins,
                killsAsInfected, killsAsSurvivor, timeSurvived,
                quickestDetectiveWin, quickestMurdererWin,
                tokens,
                weeklyResetTimestamp, monthlyResetTimestamp
        );
    }
}
