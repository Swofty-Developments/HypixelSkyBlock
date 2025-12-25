package net.swofty.commons.bedwars;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BedWarsModeStats {
	private final Map<String, Long> wins;
	private final Map<String, Long> finalKills;
	private final Map<String, Long> bedsBroken;
	private final Map<String, Long> losses;
	private final Map<String, Long> bedsLost;
	private final Map<String, Long> kills;
	private final Map<String, Long> deaths;
	private final Map<String, Long> finalDeaths;
	private final Map<String, Long> winstreaks;

	@Setter private long dailyResetTimestamp;
	@Setter private long weeklyResetTimestamp;
	@Setter private long monthlyResetTimestamp;

	public BedWarsModeStats() {
		this.wins = new HashMap<>();
		this.finalKills = new HashMap<>();
		this.bedsBroken = new HashMap<>();
		this.losses = new HashMap<>();
		this.bedsLost = new HashMap<>();
		this.kills = new HashMap<>();
		this.deaths = new HashMap<>();
		this.finalDeaths = new HashMap<>();
		this.winstreaks = new HashMap<>();
		initializeResetTimestamps();
	}

	public BedWarsModeStats(Map<String, Long> wins, Map<String, Long> finalKills, Map<String, Long> bedsBroken,
	                        Map<String, Long> losses, Map<String, Long> bedsLost, Map<String, Long> kills,
	                        Map<String, Long> deaths, Map<String, Long> finalDeaths, Map<String, Long> winstreaks,
	                        long dailyResetTimestamp, long weeklyResetTimestamp, long monthlyResetTimestamp) {
		this.wins = new HashMap<>(wins);
		this.finalKills = new HashMap<>(finalKills);
		this.bedsBroken = new HashMap<>(bedsBroken);
		this.losses = new HashMap<>(losses);
		this.bedsLost = new HashMap<>(bedsLost);
		this.kills = new HashMap<>(kills);
		this.deaths = new HashMap<>(deaths);
		this.finalDeaths = new HashMap<>(finalDeaths);
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

	public static BedWarsModeStats empty() {
		return new BedWarsModeStats();
	}

	public void checkAndResetExpiredPeriods() {
		long now = System.currentTimeMillis();

		if (now >= dailyResetTimestamp) {
			resetPeriod(BedwarsLeaderboardPeriod.DAILY);
			dailyResetTimestamp = computeNextDailyReset();
		}

		if (now >= weeklyResetTimestamp) {
			resetPeriod(BedwarsLeaderboardPeriod.WEEKLY);
			weeklyResetTimestamp = computeNextWeeklyReset();
		}

		if (now >= monthlyResetTimestamp) {
			resetPeriod(BedwarsLeaderboardPeriod.MONTHLY);
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

	private String key(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return mode.getKey() + ":" + period.getKey();
	}

	public long getWins(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return wins.getOrDefault(key(mode, period), 0L);
	}

	public long getFinalKills(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return finalKills.getOrDefault(key(mode, period), 0L);
	}

	public long getBedsBroken(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return bedsBroken.getOrDefault(key(mode, period), 0L);
	}

	public long getLosses(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return losses.getOrDefault(key(mode, period), 0L);
	}

	public long getBedsLost(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return bedsLost.getOrDefault(key(mode, period), 0L);
	}

	public long getKills(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return kills.getOrDefault(key(mode, period), 0L);
	}

	public long getDeaths(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return deaths.getOrDefault(key(mode, period), 0L);
	}

	public long getFinalDeaths(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return finalDeaths.getOrDefault(key(mode, period), 0L);
	}

	public long getWinstreak(BedwarsLeaderboardMode mode) {
		return winstreaks.getOrDefault(mode.getKey(), 0L);
	}

	public void addWin(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		wins.merge(key(mode, period), 1L, Long::sum);
	}

	public void addFinalKill(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		finalKills.merge(key(mode, period), 1L, Long::sum);
	}

	public void addBedBroken(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		bedsBroken.merge(key(mode, period), 1L, Long::sum);
	}

	public void addLoss(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		losses.merge(key(mode, period), 1L, Long::sum);
	}

	public void addBedLost(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		bedsLost.merge(key(mode, period), 1L, Long::sum);
	}

	public void addKill(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		kills.merge(key(mode, period), 1L, Long::sum);
	}

	public void addDeath(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		deaths.merge(key(mode, period), 1L, Long::sum);
	}

	public void addFinalDeath(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		finalDeaths.merge(key(mode, period), 1L, Long::sum);
	}

	public void incrementWinstreak(BedwarsLeaderboardMode mode) {
		winstreaks.merge(mode.getKey(), 1L, Long::sum);
	}

	public void resetWinstreak(BedwarsLeaderboardMode mode) {
		winstreaks.put(mode.getKey(), 0L);
	}

	public void recordWin(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addWin(mode, period);
		}
		incrementWinstreak(mode);
	}

	public void recordFinalKill(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addFinalKill(mode, period);
		}
	}

	public void recordBedBroken(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addBedBroken(mode, period);
		}
	}

	public void recordLoss(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addLoss(mode, period);
		}
		resetWinstreak(mode);
	}

	public void recordBedLost(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addBedLost(mode, period);
		}
	}

	public void recordKill(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addKill(mode, period);
		}
	}

	public void recordDeath(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addDeath(mode, period);
		}
	}

	public void recordFinalDeath(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addFinalDeath(mode, period);
		}
	}

	public void resetPeriod(BedwarsLeaderboardPeriod period) {
		for (BedwarsLeaderboardMode mode : BedwarsLeaderboardMode.values()) {
			String k = key(mode, period);
			wins.remove(k);
			finalKills.remove(k);
			bedsBroken.remove(k);
			losses.remove(k);
			bedsLost.remove(k);
			kills.remove(k);
			deaths.remove(k);
			finalDeaths.remove(k);
		}
	}

	public BedWarsModeStats copy() {
		return new BedWarsModeStats(wins, finalKills, bedsBroken,
				losses, bedsLost, kills, deaths, finalDeaths, winstreaks,
				dailyResetTimestamp, weeklyResetTimestamp, monthlyResetTimestamp);
	}
}
