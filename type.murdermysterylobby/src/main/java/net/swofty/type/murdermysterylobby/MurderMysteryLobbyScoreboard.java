package net.swofty.type.murdermysterylobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.translation.Argument;
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
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
				Locale l = player.getLocale();

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

				List<Component> lines = new ArrayList<>();
				lines.add(I18n.t("scoreboard.common.date_line", Argument.tagResolver(Formatter.date("date", LocalDateTime.now(ZoneId.systemDefault()))), Argument.string("id", HypixelConst.getServerName())));
				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.murdermystery_lobby.total_kills_line", Component.text(String.valueOf(totalKills))));
				lines.add(I18n.t("scoreboard.murdermystery_lobby.total_wins_line", Component.text(String.valueOf(totalWins))));
				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.murdermystery_lobby.wins_as_detective_line", Component.text(String.valueOf(detectiveWins))));
				lines.add(I18n.t("scoreboard.murdermystery_lobby.wins_as_murderer_line", Component.text(String.valueOf(murdererWins))));
				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.murdermystery_lobby.tokens_line", Component.text(String.valueOf(tokens))));
				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.common.footer"));

				if (!scoreboard.hasScoreboard(player)) {
					scoreboard.createScoreboard(player, Component.text(getSidebarName(animationFrame, l)));
				}

				scoreboard.updateLines(player, lines);
				scoreboard.updateTitle(player, Component.text(getSidebarName(animationFrame, l)));
			}
			return TaskSchedule.tick(4);
		});
	}

	public static void removeCache(Player player) {
		scoreboard.removeScoreboard(player);
	}

	private static String getSidebarName(int counter, Locale locale) {
		String baseText = I18n.string("scoreboard.murdermystery_lobby.title_base", locale);
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
