package net.swofty.commons.skywars;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Getter
public class SkywarsModeStats {
    private final Map<String, Long> wins;
    private final Map<String, Long> losses;
    private final Map<String, Long> kills;
    private final Map<String, Long> deaths;
    private final Map<String, Long> assists;
    private final Map<String, Long> meleeKills;
    private final Map<String, Long> bowKills;
    private final Map<String, Long> voidKills;
    private final Map<String, Long> arrowsShot;
    private final Map<String, Long> arrowsHit;
    private final Map<String, Long> chestsOpened;
    private final Map<String, Long> soulsGathered;
    private final Map<String, Long> heads;
    private final Map<String, Long> winstreaks;

    @Setter private long dailyResetTimestamp;
    @Setter private long weeklyResetTimestamp;
    @Setter private long monthlyResetTimestamp;

    public SkywarsModeStats() {
        this.wins = new HashMap<>();
        this.losses = new HashMap<>();
        this.kills = new HashMap<>();
        this.deaths = new HashMap<>();
        this.assists = new HashMap<>();
        this.meleeKills = new HashMap<>();
        this.bowKills = new HashMap<>();
        this.voidKills = new HashMap<>();
        this.arrowsShot = new HashMap<>();
        this.arrowsHit = new HashMap<>();
        this.chestsOpened = new HashMap<>();
        this.soulsGathered = new HashMap<>();
        this.heads = new HashMap<>();
        this.winstreaks = new HashMap<>();
        initializeResetTimestamps();
    }

    public SkywarsModeStats(Map<String, Long> wins, Map<String, Long> losses, Map<String, Long> kills,
                           Map<String, Long> deaths, Map<String, Long> assists, Map<String, Long> meleeKills,
                           Map<String, Long> bowKills, Map<String, Long> voidKills, Map<String, Long> arrowsShot,
                           Map<String, Long> arrowsHit, Map<String, Long> chestsOpened, Map<String, Long> soulsGathered,
                           Map<String, Long> heads, Map<String, Long> winstreaks,
                           long dailyResetTimestamp, long weeklyResetTimestamp, long monthlyResetTimestamp) {
        this.wins = new HashMap<>(wins);
        this.losses = new HashMap<>(losses);
        this.kills = new HashMap<>(kills);
        this.deaths = new HashMap<>(deaths);
        this.assists = new HashMap<>(assists);
        this.meleeKills = new HashMap<>(meleeKills);
        this.bowKills = new HashMap<>(bowKills);
        this.voidKills = new HashMap<>(voidKills);
        this.arrowsShot = new HashMap<>(arrowsShot);
        this.arrowsHit = new HashMap<>(arrowsHit);
        this.chestsOpened = new HashMap<>(chestsOpened);
        this.soulsGathered = new HashMap<>(soulsGathered);
        this.heads = new HashMap<>(heads);
        this.winstreaks = new HashMap<>(winstreaks);
        this.dailyResetTimestamp = dailyResetTimestamp;
        this.weeklyResetTimestamp = weeklyResetTimestamp;
        this.monthlyResetTimestamp = monthlyResetTimestamp;
    }

    private void initializeResetTimestamps() {
        this.dailyResetTimestamp = computeNextDailyReset();
        this.weeklyResetTimestamp = computeNextWeeklyReset();
        this.monthlyResetTimestamp = computeNextMonthlyReset();
    }

    public static SkywarsModeStats empty() {
        return new SkywarsModeStats();
    }

    public void checkAndResetExpiredPeriods() {
        long now = System.currentTimeMillis();

        if (now >= dailyResetTimestamp) {
            resetPeriod(SkywarsLeaderboardPeriod.DAILY);
            dailyResetTimestamp = computeNextDailyReset();
        }

        if (now >= weeklyResetTimestamp) {
            resetPeriod(SkywarsLeaderboardPeriod.WEEKLY);
            weeklyResetTimestamp = computeNextWeeklyReset();
        }

        if (now >= monthlyResetTimestamp) {
            resetPeriod(SkywarsLeaderboardPeriod.MONTHLY);
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

    private String key(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return mode.getKey() + ":" + period.getKey();
    }

    // Getters for stats
    public long getWins(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return wins.getOrDefault(key(mode, period), 0L);
    }

    public long getLosses(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return losses.getOrDefault(key(mode, period), 0L);
    }

    public long getKills(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return kills.getOrDefault(key(mode, period), 0L);
    }

    public long getDeaths(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return deaths.getOrDefault(key(mode, period), 0L);
    }

    public long getAssists(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return assists.getOrDefault(key(mode, period), 0L);
    }

    public long getMeleeKills(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return meleeKills.getOrDefault(key(mode, period), 0L);
    }

    public long getBowKills(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return bowKills.getOrDefault(key(mode, period), 0L);
    }

    public long getVoidKills(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return voidKills.getOrDefault(key(mode, period), 0L);
    }

    public long getArrowsShot(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return arrowsShot.getOrDefault(key(mode, period), 0L);
    }

    public long getArrowsHit(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return arrowsHit.getOrDefault(key(mode, period), 0L);
    }

    public long getChestsOpened(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return chestsOpened.getOrDefault(key(mode, period), 0L);
    }

    public long getSoulsGathered(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return soulsGathered.getOrDefault(key(mode, period), 0L);
    }

    public long getHeads(SkywarsLeaderboardMode mode, SkywarsLeaderboardPeriod period) {
        return heads.getOrDefault(key(mode, period), 0L);
    }

    public long getWinstreak(SkywarsLeaderboardMode mode) {
        return winstreaks.getOrDefault(mode.getKey(), 0L);
    }

    // Aggregate getters (sum across all modes)
    public long getTotalWins(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getWins(mode, period);
        }
        return total;
    }

    public long getTotalKills(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getKills(mode, period);
        }
        return total;
    }

    public long getTotalDeaths(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getDeaths(mode, period);
        }
        return total;
    }

    public long getTotalSoulsGathered(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getSoulsGathered(mode, period);
        }
        return total;
    }

    public long getTotalChestsOpened(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getChestsOpened(mode, period);
        }
        return total;
    }

    public long getTotalHeads(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getHeads(mode, period);
        }
        return total;
    }

    public long getTotalLosses(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getLosses(mode, period);
        }
        return total;
    }

    public long getTotalAssists(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getAssists(mode, period);
        }
        return total;
    }

    public long getTotalMeleeKills(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getMeleeKills(mode, period);
        }
        return total;
    }

    public long getTotalBowKills(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getBowKills(mode, period);
        }
        return total;
    }

    public long getTotalVoidKills(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getVoidKills(mode, period);
        }
        return total;
    }

    public long getTotalArrowsShot(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getArrowsShot(mode, period);
        }
        return total;
    }

    public long getTotalArrowsHit(SkywarsLeaderboardPeriod period) {
        long total = 0;
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            total += getArrowsHit(mode, period);
        }
        return total;
    }

    // Record methods (for all periods)
    public void recordWin(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            wins.merge(key(mode, period), 1L, Long::sum);
        }
        incrementWinstreak(mode);
    }

    public void recordLoss(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            losses.merge(key(mode, period), 1L, Long::sum);
        }
        resetWinstreak(mode);
    }

    public void recordKill(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            kills.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordDeath(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            deaths.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordAssist(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            assists.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordMeleeKill(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            meleeKills.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordBowKill(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            bowKills.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordVoidKill(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            voidKills.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordArrowShot(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            arrowsShot.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordArrowHit(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            arrowsHit.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordChestOpened(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            chestsOpened.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void recordSoulGathered(SkywarsLeaderboardMode mode, long amount) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            soulsGathered.merge(key(mode, period), amount, Long::sum);
        }
    }

    public void recordHead(SkywarsLeaderboardMode mode) {
        for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
            heads.merge(key(mode, period), 1L, Long::sum);
        }
    }

    public void incrementWinstreak(SkywarsLeaderboardMode mode) {
        winstreaks.merge(mode.getKey(), 1L, Long::sum);
    }

    public void resetWinstreak(SkywarsLeaderboardMode mode) {
        winstreaks.put(mode.getKey(), 0L);
    }

    public void resetPeriod(SkywarsLeaderboardPeriod period) {
        for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
            String k = key(mode, period);
            wins.remove(k);
            losses.remove(k);
            kills.remove(k);
            deaths.remove(k);
            assists.remove(k);
            meleeKills.remove(k);
            bowKills.remove(k);
            voidKills.remove(k);
            arrowsShot.remove(k);
            arrowsHit.remove(k);
            chestsOpened.remove(k);
            soulsGathered.remove(k);
            heads.remove(k);
        }
    }

    public SkywarsModeStats copy() {
        return new SkywarsModeStats(wins, losses, kills, deaths, assists, meleeKills,
                bowKills, voidKills, arrowsShot, arrowsHit, chestsOpened, soulsGathered,
                heads, winstreaks, dailyResetTimestamp, weeklyResetTimestamp, monthlyResetTimestamp);
    }
}
