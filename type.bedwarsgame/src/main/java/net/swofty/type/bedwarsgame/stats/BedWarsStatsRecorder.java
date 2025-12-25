package net.swofty.type.bedwarsgame.stats;

import net.swofty.commons.bedwars.BedWarsModeStats;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.bedwars.BedwarsLeaderboardMode;
import net.swofty.commons.bedwars.BedwarsLeaderboardPeriod;
import net.swofty.commons.bedwars.BedwarsStatType;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsModeStats;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.data.mongodb.BedWarsStatsDatabase;
import net.swofty.type.generic.leaderboard.LeaderboardService;

import java.util.UUID;

public class BedWarsStatsRecorder {

	public static void recordWin(BedWarsPlayer player, BedwarsGameType gameType) {
		UUID uuid = player.getUuid();
		BedWarsDataHandler handler = player.getBedWarsDataHandler();

		DatapointLong totalWins = handler.get(BedWarsDataHandler.Data.TOTAL_WINS, DatapointLong.class);
		long newTotal = totalWins.getValue() + 1;
		totalWins.setValue(newTotal);

		BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
		BedwarsLeaderboardMode mode = BedwarsLeaderboardMode.fromGameType(gameType);
		modeStats.recordWin(mode);
		modeStats.recordWin(BedwarsLeaderboardMode.ALL);
		if (BedwarsLeaderboardMode.CORE.includes(gameType)) {
			modeStats.recordWin(BedwarsLeaderboardMode.CORE);
		}

		BedWarsStatsDatabase.recordStatEventAsync(uuid, BedwarsStatType.WINS, gameType, 1);

		updateLeaderboards(uuid, BedwarsStatType.WINS, mode, modeStats, newTotal);
	}

	public static void recordFinalKill(BedWarsPlayer player, BedwarsGameType gameType) {
		UUID uuid = player.getUuid();
		BedWarsDataHandler handler = player.getBedWarsDataHandler();

		DatapointLong totalFinalKills = handler.get(BedWarsDataHandler.Data.TOTAL_FINAL_KILLS, DatapointLong.class);
		long newTotal = totalFinalKills.getValue() + 1;
		totalFinalKills.setValue(newTotal);

		BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
		BedwarsLeaderboardMode mode = BedwarsLeaderboardMode.fromGameType(gameType);
		modeStats.recordFinalKill(mode);
		modeStats.recordFinalKill(BedwarsLeaderboardMode.ALL);
		if (BedwarsLeaderboardMode.CORE.includes(gameType)) {
			modeStats.recordFinalKill(BedwarsLeaderboardMode.CORE);
		}

		BedWarsStatsDatabase.recordStatEventAsync(uuid, BedwarsStatType.FINAL_KILLS, gameType, 1);

		updateLeaderboards(uuid, BedwarsStatType.FINAL_KILLS, mode, modeStats, newTotal);
	}

	public static void recordBedBroken(BedWarsPlayer player, BedwarsGameType gameType) {
		UUID uuid = player.getUuid();
		BedWarsDataHandler handler = player.getBedWarsDataHandler();

		DatapointLong totalBedsBroken = handler.get(BedWarsDataHandler.Data.TOTAL_BEDS_BROKEN, DatapointLong.class);
		long newTotal = totalBedsBroken.getValue() + 1;
		totalBedsBroken.setValue(newTotal);

		BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
		BedwarsLeaderboardMode mode = BedwarsLeaderboardMode.fromGameType(gameType);
		modeStats.recordBedBroken(mode);
		modeStats.recordBedBroken(BedwarsLeaderboardMode.ALL);
		if (BedwarsLeaderboardMode.CORE.includes(gameType)) {
			modeStats.recordBedBroken(BedwarsLeaderboardMode.CORE);
		}

		BedWarsStatsDatabase.recordStatEventAsync(uuid, BedwarsStatType.BEDS_BROKEN, gameType, 1);

		updateLeaderboards(uuid, BedwarsStatType.BEDS_BROKEN, mode, modeStats, newTotal);
	}

	public static void recordLoss(BedWarsPlayer player, BedwarsGameType gameType) {
		BedWarsDataHandler handler = player.getBedWarsDataHandler();

		BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
		BedwarsLeaderboardMode mode = BedwarsLeaderboardMode.fromGameType(gameType);
		modeStats.recordLoss(mode);
		modeStats.recordLoss(BedwarsLeaderboardMode.ALL);
		if (BedwarsLeaderboardMode.CORE.includes(gameType)) {
			modeStats.recordLoss(BedwarsLeaderboardMode.CORE);
		}
	}

	public static void recordKill(BedWarsPlayer player, BedwarsGameType gameType) {
		BedWarsDataHandler handler = player.getBedWarsDataHandler();

		BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
		BedwarsLeaderboardMode mode = BedwarsLeaderboardMode.fromGameType(gameType);
		modeStats.recordKill(mode);
		modeStats.recordKill(BedwarsLeaderboardMode.ALL);
		if (BedwarsLeaderboardMode.CORE.includes(gameType)) {
			modeStats.recordKill(BedwarsLeaderboardMode.CORE);
		}
	}

	public static void recordDeath(BedWarsPlayer player, BedwarsGameType gameType) {
		BedWarsDataHandler handler = player.getBedWarsDataHandler();

		BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
		BedwarsLeaderboardMode mode = BedwarsLeaderboardMode.fromGameType(gameType);
		modeStats.recordDeath(mode);
		modeStats.recordDeath(BedwarsLeaderboardMode.ALL);
		if (BedwarsLeaderboardMode.CORE.includes(gameType)) {
			modeStats.recordDeath(BedwarsLeaderboardMode.CORE);
		}
	}

	public static void recordFinalDeath(BedWarsPlayer player, BedwarsGameType gameType) {
		BedWarsDataHandler handler = player.getBedWarsDataHandler();

		BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
		BedwarsLeaderboardMode mode = BedwarsLeaderboardMode.fromGameType(gameType);
		modeStats.recordFinalDeath(mode);
		modeStats.recordFinalDeath(BedwarsLeaderboardMode.ALL);
		if (BedwarsLeaderboardMode.CORE.includes(gameType)) {
			modeStats.recordFinalDeath(BedwarsLeaderboardMode.CORE);
		}
	}

	public static void recordBedLost(BedWarsPlayer player, BedwarsGameType gameType) {
		BedWarsDataHandler handler = player.getBedWarsDataHandler();

		BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
		BedwarsLeaderboardMode mode = BedwarsLeaderboardMode.fromGameType(gameType);
		modeStats.recordBedLost(mode);
		modeStats.recordBedLost(BedwarsLeaderboardMode.ALL);
		if (BedwarsLeaderboardMode.CORE.includes(gameType)) {
			modeStats.recordBedLost(BedwarsLeaderboardMode.CORE);
		}
	}

	private static void updateLeaderboards(UUID uuid, BedwarsStatType statType, BedwarsLeaderboardMode mode,
	                                        BedWarsModeStats modeStats, long allModesTotal) {
		String statKey = statType.getKey();

		String allModesKey = String.format("bedwars:%s:all:lifetime", statKey);
		LeaderboardService.updateScoreAsync(allModesKey, uuid, allModesTotal);

		long modeScore = getStatFromModeStats(modeStats, statType, mode, BedwarsLeaderboardPeriod.LIFETIME);
		String specificModeKey = String.format("bedwars:%s:%s:lifetime", statKey, mode.getKey());
		LeaderboardService.updateScoreAsync(specificModeKey, uuid, modeScore);
	}

	private static long getStatFromModeStats(BedWarsModeStats stats, BedwarsStatType statType,
	                                          BedwarsLeaderboardMode mode, BedwarsLeaderboardPeriod period) {
		return switch (statType) {
			case WINS -> stats.getWins(mode, period);
			case FINAL_KILLS -> stats.getFinalKills(mode, period);
			case BEDS_BROKEN -> stats.getBedsBroken(mode, period);
			default -> 0L;
		};
	}
}
