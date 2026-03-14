package net.swofty.type.bedwarslobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.BedwarsLevelColor;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.swofty.commons.bedwars.BedwarsLevelUtil.suffix;

public class BedWarsLobbyScoreboard {
	private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
	private static Integer prototypeName = 0;

	public static void start() {
		Scheduler scheduler = MinecraftServer.getSchedulerManager();

		scheduler.submitTask(() -> {
			prototypeName++;
			if (prototypeName > 50) {
				prototypeName = 0;
			}

			for (HypixelPlayer player : HypixelGenericLoader.getLoadedPlayers()) {
				HypixelDataHandler dataHandler = player.getDataHandler();
				BedWarsDataHandler bwDataHandler = BedWarsDataHandler.getUser(player);

				if (dataHandler == null || bwDataHandler == null) {
					continue;
				}

				long experience = bwDataHandler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class).getValue();
				int progress = BedwarsLevelUtil.calculateExperienceSinceLastLevel(experience);
				int maxExperience = BedwarsLevelUtil.calculateMaxExperienceFromExperience(experience);

				double percentage = Math.min(1.0, (double) progress / maxExperience);
				int filledSquares = (int) Math.round(percentage * 10);
				StringBuilder progressBar = new StringBuilder(" " + I18n.string("scoreboard.bedwars_lobby.progress_bar_open"));
				for (int i = 0; i < 10; i++) {
					if (i < filledSquares) {
						progressBar.append(I18n.string("scoreboard.bedwars_lobby.progress_bar_filled"));
					} else {
						progressBar.append(I18n.string("scoreboard.bedwars_lobby.progress_bar_empty"));
					}
				}
				progressBar.append(I18n.string("scoreboard.bedwars_lobby.progress_bar_close"));

				long tokens = bwDataHandler.get(BedWarsDataHandler.Data.TOKENS, DatapointLeaderboardLong.class).getValue();
				long tickets = bwDataHandler.get(BedWarsDataHandler.Data.SLUMBER_TICKETS, DatapointLeaderboardLong.class).getValue();

				List<String> lines = new ArrayList<>();
				lines.add("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()) + " §8" + HypixelConst.getServerName());
				lines.add("§7 ");
				lines.add(I18n.string("scoreboard.bedwars_lobby.level_label") + BedwarsLevelColor.constructLevelString(BedwarsLevelUtil.calculateLevel(experience)));
				lines.add("§7 ");
				lines.add(I18n.string("scoreboard.bedwars_lobby.progress_label") + suffix(progress) + I18n.string("scoreboard.bedwars_lobby.progress_separator") + suffix(maxExperience));
				lines.add(progressBar.toString());
				lines.add("§7 ");
				lines.add(I18n.string("scoreboard.bedwars_lobby.tokens_label") + tokens);
				lines.add(I18n.string("scoreboard.bedwars_lobby.tickets_label") + tickets + I18n.string("scoreboard.bedwars_lobby.tickets_max"));
				lines.add("§7 ");
				lines.add(I18n.string("scoreboard.bedwars_lobby.total_kills_label"));
				lines.add(I18n.string("scoreboard.bedwars_lobby.total_wins_label"));
				lines.add("§7 ");
				lines.add(I18n.string("scoreboard.common.footer"));

				if (!scoreboard.hasScoreboard(player)) {
					scoreboard.createScoreboard(player, getSidebarName(prototypeName));
				}

				scoreboard.updateLines(player, lines);
				scoreboard.updateTitle(player, getSidebarName(prototypeName));
			}
			return TaskSchedule.tick(4);
		});
	}

	public static void removeCache(Player player) {
		scoreboard.removeScoreboard(player);
	}

	private static String getSidebarName(int counter) {
		String baseText = I18n.string("scoreboard.bedwars_lobby.title_base");
		String[] colors = {"§f§l", "§6§l", "§e§l"};
		String endColor = "§a§l";

		if (counter > 0 && counter <= 8) {
			return colors[0] + baseText.substring(0, counter - 1) +
					colors[1] + baseText.charAt(counter - 1) +
					colors[2] + baseText.substring(counter) +
					endColor;
		} else if ((counter >= 9 && counter <= 19) ||
				(counter >= 25 && counter <= 29)) {
			return colors[0] + baseText + endColor;
		} else {
			return colors[2] + baseText + endColor;
		}
	}
}
