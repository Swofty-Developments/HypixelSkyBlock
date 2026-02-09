package net.swofty.type.murdermysterylobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardPeriod;
import net.swofty.commons.murdermystery.MurderMysteryModeStats;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MurderMysteryLobbyScoreboard {
	private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
	private static Integer animationFrame = 0;

	public static void start() {
		Scheduler scheduler = MinecraftServer.getSchedulerManager();

		scheduler.submitTask(() -> {
			animationFrame++;
			if (animationFrame > 50) {
				animationFrame = 0;
			}

			for (HypixelPlayer player : HypixelGenericLoader.getLoadedPlayers()) {
				if (player.getDataHandler() == null) {
					continue;
				}

				MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(player);
				long totalKills = 0;
				long totalWins = 0;
				long detectiveWins = 0;
				long murdererWins = 0;
				long tokens = 0;

				if (handler != null) {
					DatapointMurderMysteryModeStats statsDP = handler.get(
							MurderMysteryDataHandler.Data.MODE_STATS,
							DatapointMurderMysteryModeStats.class);
					MurderMysteryModeStats stats = statsDP.getValue();
					totalKills = stats.getTotalKills(MurderMysteryLeaderboardPeriod.LIFETIME);
					totalWins = stats.getTotalWins(MurderMysteryLeaderboardPeriod.LIFETIME);
					detectiveWins = stats.getTotalDetectiveWins(MurderMysteryLeaderboardPeriod.LIFETIME);
					murdererWins = stats.getTotalMurdererWins(MurderMysteryLeaderboardPeriod.LIFETIME);
					tokens = stats.getTotalTokens(MurderMysteryLeaderboardPeriod.LIFETIME);
				}

				List<String> lines = new ArrayList<>();
				lines.add("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName());
				lines.add("§7 ");
				lines.add("§fTotal Kills: §a" + totalKills);
				lines.add("§fTotal Wins: §a" + totalWins);
				lines.add("§7 ");
				lines.add("§fWins as Detective: §a" + detectiveWins);
				lines.add("§fWins as Murderer: §a" + murdererWins);
				lines.add("§7 ");
				lines.add("§fTokens: §2" + tokens);
				lines.add("§7 ");
				lines.add("§ewww.hypixel.net");

				if (!scoreboard.hasScoreboard(player)) {
					scoreboard.createScoreboard(player, getSidebarName(animationFrame));
				}

				scoreboard.updateLines(player, lines);
				scoreboard.updateTitle(player, getSidebarName(animationFrame));
			}
			return TaskSchedule.tick(4);
		});
	}

	public static void removeCache(Player player) {
		scoreboard.removeScoreboard(player);
	}

	private static String getSidebarName(int counter) {
		String baseText = "MURDER MYSTERY";
		String[] colors = {"§f§l", "§6§l", "§e§l"};
		String endColor = "§a§l";

		if (counter > 0 && counter <= 14) {
			return colors[0] + baseText.substring(0, counter - 1) +
					colors[1] + baseText.charAt(counter - 1) +
					colors[2] + baseText.substring(counter) +
					endColor;
		} else if ((counter >= 15 && counter <= 25) || (counter >= 35 && counter <= 45)) {
			return colors[0] + baseText + endColor;
		} else {
			return colors[2] + baseText + endColor;
		}
	}
}
