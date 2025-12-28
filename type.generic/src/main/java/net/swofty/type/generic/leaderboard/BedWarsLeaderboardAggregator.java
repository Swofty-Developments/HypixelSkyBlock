package net.swofty.type.generic.leaderboard;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.BedwarsLeaderboardMode;
import net.swofty.commons.bedwars.BedwarsLeaderboardPeriod;
import net.swofty.commons.bedwars.BedwarsStatType;
import net.swofty.type.generic.data.mongodb.BedWarsStatsDatabase;
import org.tinylog.Logger;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class BedWarsLeaderboardAggregator {

	private static boolean initialized = false;

	public static void initialize() {
		if (initialized) return;
		initialized = true;

		// Run aggregation every 5 minutes
		MinecraftServer.getSchedulerManager().buildTask(() -> {
			try {
				runAggregation();
			} catch (Exception e) {
				Logger.error("Error running BedWars leaderboard aggregation: " + e.getMessage());
				e.printStackTrace();
			}
		}).delay(TaskSchedule.minutes(1)) // Initial delay
				.repeat(TaskSchedule.minutes(5))
				.schedule();

		// Also run cleanup task daily
		MinecraftServer.getSchedulerManager().buildTask(() -> {
			try {
				BedWarsStatsDatabase.cleanupOldEvents(35); // Keep 35 days of data
			} catch (Exception e) {
				Logger.error("Error cleaning up old BedWars stats: " + e.getMessage());
			}
		}).delay(TaskSchedule.hours(1))
				.repeat(TaskSchedule.hours(24))
				.schedule();

		Logger.info("BedWars Leaderboard Aggregator initialized");
	}

	public static void runAggregation() {
		Logger.info("Running BedWars leaderboard aggregation...");

		for (BedwarsStatType statType : BedwarsStatType.values()) {
			if (!statType.isTimedTracking()) continue; // Skip LEVEL, it uses experience

			for (BedwarsLeaderboardMode mode : BedwarsLeaderboardMode.values()) {
				aggregatePeriod(statType, mode, BedwarsLeaderboardPeriod.DAILY);
				aggregatePeriod(statType, mode, BedwarsLeaderboardPeriod.WEEKLY);
				aggregatePeriod(statType, mode, BedwarsLeaderboardPeriod.MONTHLY);
				// LIFETIME is updated in real-time by BedWarsStatsRecorder
			}
		}

		Logger.info("BedWars leaderboard aggregation complete");
	}

	private static void aggregatePeriod(BedwarsStatType statType, BedwarsLeaderboardMode mode,
	                                     BedwarsLeaderboardPeriod period) {
		Date since = getSinceDate(period);
		if (since == null) return; // LIFETIME handled separately

		String leaderboardKey = buildLeaderboardKey(statType, mode, period);

		try {
			Map<UUID, Long> aggregates = BedWarsStatsDatabase.aggregateStats(statType, mode, since);

			// Update all player scores in the leaderboard
			for (Map.Entry<UUID, Long> entry : aggregates.entrySet()) {
				LeaderboardService.updateScoreAsync(leaderboardKey, entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			Logger.error("Error aggregating " + leaderboardKey + ": " + e.getMessage());
		}
	}

	private static Date getSinceDate(BedwarsLeaderboardPeriod period) {
		return switch (period) {
			case DAILY -> BedWarsStatsDatabase.getStartOfDay();
			case WEEKLY -> BedWarsStatsDatabase.getStartOfWeek();
			case MONTHLY -> BedWarsStatsDatabase.getStartOfMonth();
			case LIFETIME -> null;
		};
	}

	public static String buildLeaderboardKey(BedwarsStatType statType, BedwarsLeaderboardMode mode,
	                                          BedwarsLeaderboardPeriod period) {
		return String.format("bedwars:%s:%s:%s", statType.getKey(), mode.getKey(), period.getKey());
	}

	/**
	 * Clear a specific period's leaderboard (used for period resets)
	 */
	public static void clearLeaderboard(BedwarsStatType statType, BedwarsLeaderboardMode mode,
	                                     BedwarsLeaderboardPeriod period) {
		String leaderboardKey = buildLeaderboardKey(statType, mode, period);
		// Note: LeaderboardService would need a clear method, for now we rebuild from aggregation
		Logger.info("Leaderboard {} would be cleared on period reset", leaderboardKey);
	}
}
