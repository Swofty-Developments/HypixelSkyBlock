package net.swofty.type.skywarslobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skywars.SkywarsLeaderboardMode;
import net.swofty.commons.skywars.SkywarsLeaderboardPeriod;
import net.swofty.commons.skywars.SkywarsLevelColor;
import net.swofty.type.skywarslobby.level.SkywarsLevelRegistry;
import net.swofty.commons.skywars.SkywarsModeStats;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsModeStats;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SkywarsLobbyScoreboard {
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
				SkywarsDataHandler swDataHandler = SkywarsDataHandler.getUser(player);

				if (swDataHandler == null) {
					continue;
				}

				long experience = swDataHandler.get(SkywarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue();
				long souls = swDataHandler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).getValue();
				long coins = swDataHandler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
				long tokens = swDataHandler.get(SkywarsDataHandler.Data.TOKENS, DatapointLong.class).getValue();

				SkywarsModeStats modeStats = swDataHandler.get(SkywarsDataHandler.Data.MODE_STATS, DatapointSkywarsModeStats.class).getValue();

				long soloKills = modeStats.getKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
						+ modeStats.getKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
				long soloWins = modeStats.getWins(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
						+ modeStats.getWins(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);

				long doublesKills = modeStats.getKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
				long doublesWins = modeStats.getWins(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);

				int level = SkywarsLevelRegistry.calculateLevel(experience);

				List<String> lines = new ArrayList<>();
				lines.add("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName());
				lines.add("§7 ");
				lines.add("§fYour Level: " + SkywarsLevelColor.getLevelDisplay(level));
				lines.add("§7 ");
				lines.add("§fSolo Kills: §a" + soloKills);
				lines.add("§fSolo Wins: §a" + soloWins);
				lines.add("§fDoubles Kills: §a" + doublesKills);
				lines.add("§fDoubles Wins: §a" + doublesWins);
				lines.add("§7 ");
				lines.add("§fCoins: §6" + coins);
				lines.add("§fSouls: §b" + souls);
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
		String baseText = "SKYWARS";
		String[] colors = {"§f§l", "§e§l", "§6§l"};

		if (counter > 0 && counter <= 7) {
			return colors[0] + baseText.substring(0, counter - 1) +
					colors[1] + baseText.charAt(counter - 1) +
					colors[2] + baseText.substring(counter);
		} else if ((counter >= 8 && counter <= 18) ||
				(counter >= 25 && counter <= 29)) {
			return colors[0] + baseText;
		} else {
			return colors[1] + baseText;
		}
	}
}
