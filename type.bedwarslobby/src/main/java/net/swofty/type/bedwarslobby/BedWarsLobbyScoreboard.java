package net.swofty.type.bedwarslobby;

import net.kyori.adventure.text.Component;
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
import java.util.Locale;

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
				Locale l = player.getLocale();
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
				String date = new SimpleDateFormat(I18n.string("scoreboard.common.date_format", l)).format(new Date());
				StringBuilder progressBar = new StringBuilder();
				for (int i = 0; i < 10; i++) {
					if (i < filledSquares) {
						progressBar.append(I18n.string("scoreboard.bedwars_lobby.progress_bar_filled", l));
					} else {
						progressBar.append(I18n.string("scoreboard.bedwars_lobby.progress_bar_empty", l));
					}
				}

				long tokens = bwDataHandler.get(BedWarsDataHandler.Data.TOKENS, DatapointLeaderboardLong.class).getValue();
				long tickets = bwDataHandler.get(BedWarsDataHandler.Data.SLUMBER_TICKETS, DatapointLeaderboardLong.class).getValue();

				List<Component> lines = new ArrayList<>();
				lines.add(I18n.t("scoreboard.common.date_line", Component.text(date), Component.text(HypixelConst.getServerName())));
				lines.add(Component.text("§7 "));
				lines.add(I18n.t("scoreboard.bedwars_lobby.level_line",
					Component.text(BedwarsLevelColor.constructLevelString(BedwarsLevelUtil.calculateLevel(experience)))));
				lines.add(Component.text("§7 "));
				lines.add(I18n.t("scoreboard.bedwars_lobby.progress_line",
					Component.text(suffix(progress)),
					Component.text(suffix(maxExperience))));
				lines.add(Component.space().append(I18n.t("scoreboard.bedwars_lobby.progress_bar",
					Component.text(progressBar.toString()))));
				lines.add(Component.text("§7 "));
				lines.add(I18n.t("scoreboard.bedwars_lobby.tokens_line", Component.text(String.valueOf(tokens))));
				lines.add(I18n.t("scoreboard.bedwars_lobby.tickets_line", Component.text(String.valueOf(tickets))));
				lines.add(Component.text("§7 "));
				lines.add(I18n.t("scoreboard.bedwars_lobby.total_kills_label"));
				lines.add(I18n.t("scoreboard.bedwars_lobby.total_wins_label"));
				lines.add(Component.text("§7 "));
				lines.add(I18n.t("scoreboard.common.footer"));

				if (!scoreboard.hasScoreboard(player)) {
					scoreboard.createScoreboard(player, Component.text(getSidebarName(prototypeName, l)));
				}

				scoreboard.updateLines(player, lines);
				scoreboard.updateTitle(player, Component.text(getSidebarName(prototypeName, l)));
			}
			return TaskSchedule.tick(4);
		});
	}

	public static void removeCache(Player player) {
		scoreboard.removeScoreboard(player);
	}

	private static String getSidebarName(int counter, Locale locale) {
		String baseText = I18n.string("scoreboard.bedwars_lobby.title_base", locale);
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
