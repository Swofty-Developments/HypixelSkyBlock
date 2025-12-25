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

	@Setter private long dailyResetTimestamp;
	@Setter private long weeklyResetTimestamp;
	@Setter private long monthlyResetTimestamp;

	public BedWarsModeStats() {
		this.wins = new HashMap<>();
		this.finalKills = new HashMap<>();
		this.bedsBroken = new HashMap<>();
		initializeResetTimestamps();
	}

	public BedWarsModeStats(Map<String, Long> wins, Map<String, Long> finalKills, Map<String, Long> bedsBroken) {
		this.wins = new HashMap<>(wins);
		this.finalKills = new HashMap<>(finalKills);
		this.bedsBroken = new HashMap<>(bedsBroken);
		initializeResetTimestamps();
	}

	public BedWarsModeStats(Map<String, Long> wins, Map<String, Long> finalKills, Map<String, Long> bedsBroken,
	                        long dailyResetTimestamp, long weeklyResetTimestamp, long monthlyResetTimestamp) {
		this.wins = new HashMap<>(wins);
		this.finalKills = new HashMap<>(finalKills);
		this.bedsBroken = new HashMap<>(bedsBroken);
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

	public void addWin(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		wins.merge(key(mode, period), 1L, Long::sum);
	}

	public void addFinalKill(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		finalKills.merge(key(mode, period), 1L, Long::sum);
	}

	public void addBedBroken(BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		bedsBroken.merge(key(mode, period), 1L, Long::sum);
	}

	public void recordWin(BedwarsLeaderboardMode mode) {
		for (BedwarsLeaderboardPeriod period : BedwarsLeaderboardPeriod.values()) {
			addWin(mode, period);
		}
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

	public void resetPeriod(BedwarsLeaderboardPeriod period) {
		for (BedwarsLeaderboardMode mode : BedwarsLeaderboardMode.values()) {
			String k = key(mode, period);
			wins.remove(k);
			finalKills.remove(k);
			bedsBroken.remove(k);
		}
	}

	public BedWarsModeStats copy() {
		return new BedWarsModeStats(wins, finalKills, bedsBroken,
				dailyResetTimestamp, weeklyResetTimestamp, monthlyResetTimestamp);
	}
}
