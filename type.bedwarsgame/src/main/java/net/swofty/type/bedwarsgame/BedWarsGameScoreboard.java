package net.swofty.type.bedwarsgame;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
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
				HypixelDataHandler dataHandler = player.getDataHandler();
				BedWarsDataHandler bwDataHandler = BedWarsDataHandler.getUser(player);

				if (dataHandler == null || bwDataHandler == null) {
					continue;
				}

				List<String> lines = new ArrayList<>();
				lines.add("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()) + " §8" + HypixelConst.getServerName());
				lines.add("§7 ");

				if (game.getGameStatus() == GameStatus.WAITING) {
					lines.add(I18n.string("scoreboard.bedwars_game.map_label") + game.getMapEntry().getName());
					lines.add(I18n.string("scoreboard.bedwars_game.players_label") + game.getPlayers().size() + "/" + game.getMapEntry().getConfiguration().getTeams().size());
					lines.add("§7 ");
					lines.add(I18n.string("scoreboard.bedwars_game.starting_in_label") + game.getCountdown().getRemainingSeconds() + I18n.string("scoreboard.bedwars_game.starting_in_suffix"));
					lines.add("§7 ");
					lines.add(I18n.string("scoreboard.bedwars_game.mode_label") + game.getBedwarsGameType().getDisplayName());
					lines.add(I18n.string("scoreboard.bedwars_game.version_label"));
				} else {
					String eventName = game.getEventManager().getNextEvent() != null
							? game.getEventManager().getNextEvent().getDisplayName()
							: game.getEventManager().getCurrentEvent().getDisplayName();
					long seconds = game.getEventManager().getSecondsUntilNextEvent();
					long minutesPart = seconds / 60;
					long secondsPart = seconds % 60;
					String timeLeft = String.format("%d:%02d", minutesPart, secondsPart);
					lines.add(I18n.string("scoreboard.bedwars_game.event_in_label", Map.of("event_name", eventName, "time_left", timeLeft)));
					lines.add("§7 ");
					for (Map.Entry<TeamKey, net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam> entry : game.getMapEntry().getConfiguration().getTeams().entrySet()) {
						TeamKey teamKey = entry.getKey();
						String teamName = teamKey.getName();
						String teamInitial = teamName.substring(0, 1).toUpperCase();

						String bedStatus = game.getTeamManager().isBedAlive(teamKey)
								? I18n.string("scoreboard.bedwars_game.bed_alive")
								: I18n.string("scoreboard.bedwars_game.bed_dead");
						lines.add(String.format("%s%s §f%s %s", teamKey.chatColor(), teamInitial, teamName, bedStatus));
					}
				}
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
		String baseText = I18n.string("scoreboard.bedwars_game.title_base");
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
