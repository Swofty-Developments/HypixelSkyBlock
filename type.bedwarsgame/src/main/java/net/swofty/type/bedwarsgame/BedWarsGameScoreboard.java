package net.swofty.type.bedwarsgame;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BedWarsGameScoreboard {
	private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
	private static Integer prototypeName = 0;

	public static void start(Game game) {
		Scheduler scheduler = MinecraftServer.getSchedulerManager();
		scheduler.submitTask(() -> {
			if (game == null) {
				return TaskSchedule.stop();
			}

			prototypeName++;
			if (prototypeName > 50) {
				prototypeName = 0;
			}

			for (HypixelPlayer player : game.getPlayers()) {
				if (player.joined - System.currentTimeMillis() > 5000) {
					continue;
				}
				Locale l = player.getLocale();
				HypixelDataHandler dataHandler = player.getDataHandler();
				BedWarsDataHandler bwDataHandler = BedWarsDataHandler.getUser(player);

				if (dataHandler == null || bwDataHandler == null) {
					continue;
				}

				String date = new SimpleDateFormat(I18n.string("scoreboard.common.date_format", l)).format(new Date());

				List<Component> lines = new ArrayList<>();
				lines.add(I18n.t("scoreboard.common.date_line", Component.text(date), Component.text(HypixelConst.getServerName())));
				lines.add(Component.text("§7 "));

				if (game.getGameStatus() == GameStatus.WAITING) {
					lines.add(I18n.t("scoreboard.bedwars_game.map_line", Component.text(game.getMapEntry().getName())));
					lines.add(I18n.t("scoreboard.bedwars_game.players_line",
						Component.text(String.valueOf(game.getPlayers().size())),
						Component.text(String.valueOf(game.getMapEntry().getConfiguration().getTeams().size()))));
					lines.add(Component.text("§7 "));
					lines.add(I18n.t("scoreboard.bedwars_game.starting_in_line",
						Component.text(String.valueOf(game.getCountdown().getRemainingSeconds()))));
					lines.add(Component.text("§7 "));
					lines.add(I18n.t("scoreboard.bedwars_game.mode_line", Component.text(game.getBedwarsGameType().getDisplayName())));
					lines.add(I18n.t("scoreboard.bedwars_game.version_label"));
				} else {
					String eventName = game.getEventManager().getNextEvent() != null
							? game.getEventManager().getNextEvent().getDisplayName()
							: game.getEventManager().getCurrentEvent().getDisplayName();
					long seconds = game.getEventManager().getSecondsUntilNextEvent();
					long minutesPart = seconds / 60;
					long secondsPart = seconds % 60;
					String timeLeft = String.format("%d:%02d", minutesPart, secondsPart);
					lines.add(I18n.t("scoreboard.bedwars_game.event_in_line",
						Component.text(eventName),
						Component.text(timeLeft)));
					lines.add(Component.text("§7 "));
					for (Map.Entry<TeamKey, BedWarsMapsConfig.MapTeam> entry : game.getMapEntry().getConfiguration().getTeams().entrySet()) {
						TeamKey teamKey = entry.getKey();
						String teamName = teamKey.getName();
						String teamInitial = teamName.substring(0, 1).toUpperCase();

						Component bedStatus = game.getTeamManager().isBedAlive(teamKey)
							? I18n.t("scoreboard.bedwars_game.bed_alive")
							: I18n.t("scoreboard.bedwars_game.bed_dead");
						lines.add(I18n.t("scoreboard.bedwars_game.team_status_line",
							Component.text(teamKey.chatColor() + teamInitial),
							Component.text(teamName),
							bedStatus));
					}
				}
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
		String baseText = I18n.string("scoreboard.bedwars_game.title_base", locale);
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
