package net.swofty.type.bedwarsgame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgeneric.data.BedWarsDataHandler;
import net.swofty.type.bedwarsgeneric.game.MapsConfig;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BedWarsGameScoreboard {
	private static final Map<UUID, Sidebar> sidebarCache = new HashMap<>();
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
				if (player.joined - System.currentTimeMillis() > 5000) { // for now let's not show scoreboard too early
					continue;
				}
				HypixelDataHandler dataHandler = player.getDataHandler();
				BedWarsDataHandler bwDataHandler = BedWarsDataHandler.getUser(player);

				if (dataHandler == null || bwDataHandler == null) {
					continue;
				}

				if (sidebarCache.containsKey(player.getUuid())) {
					sidebarCache.get(player.getUuid()).removeViewer(player);
				}

				Sidebar sidebar = new Sidebar(Component.text(getSidebarName(prototypeName)));

				addLine("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName(), sidebar);
				addLine("§7 ", sidebar);
				if (game.getGameStatus() == GameStatus.WAITING) {
					addLine("§fMap: §a" + game.getMapEntry().getName(), sidebar);
					addLine("§fPlayers: §a" + game.getPlayers().size() + "/" + game.getMapEntry().getConfiguration().getTeams().size(), sidebar);
					addLine("§7 ", sidebar);
					addLine("§fStarting in §a" + game.getCountdown().getRemainingSeconds() + "s", sidebar);
					addLine("§7 ", sidebar);
					addLine("§fMode: §a" + game.getBedwarsGameType().getDisplayName(), sidebar);
					addLine("§fVersion: §7v1.9", sidebar);
				} else {
					String eventName = game.getEventManager().getNextEvent() != null
						? game.getEventManager().getNextEvent().getDisplayName()
						: game.getEventManager().getCurrentEvent().getDisplayName();
					long seconds = game.getEventManager().getSecondsUntilNextEvent();
					long minutesPart = seconds / 60;
					long secondsPart = seconds % 60;
					String timeLeft = String.format("%d:%02d", minutesPart, secondsPart);
					addLine("§f" + eventName + " in §a" + timeLeft, sidebar);
					addLine("§7 ", sidebar);
					for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : game.getMapEntry().getConfiguration().getTeams()) {
						String teamName = team.getName();
						String teamColor = team.getColor().toLowerCase();
						String teamInitial = teamName.substring(0, 1).toUpperCase();
						String capitalizedTeamName = teamName.substring(0, 1).toUpperCase() + teamName.substring(1).toLowerCase();

						String bedStatus = game.getTeamManager().getTeamBedStatus().getOrDefault(team.getName(), false) ? "<green>✔</green>" : "<red>✖</red>";
						addLine(MiniMessage.miniMessage().deserialize(String.format("<%s><b>%s</b> <white>%s:</white> %s", teamColor, teamInitial, capitalizedTeamName, bedStatus)), sidebar);
					}
				}
				addLine("§7 ", sidebar);
				addLine("§ewww.hypixel.net", sidebar);

				sidebar.addViewer(player);

				sidebarCache.put(player.getUuid(), sidebar);
			}
			return TaskSchedule.tick(2);
		});
	}

	public static void removeCache(Player player) {
		sidebarCache.remove(player.getUuid());
	}

	private static void addLine(String text, Sidebar sidebar) {
		for (Sidebar.ScoreboardLine existingLine : sidebar.getLines()) {
			sidebar.updateLineScore(existingLine.getId(), existingLine.getLine() + 1);
		}

		sidebar.createLine(new Sidebar.ScoreboardLine(UUID.randomUUID().toString(), Component.text(text), 0));
	}

	private static void addLine(Component text, Sidebar sidebar) {
		for (Sidebar.ScoreboardLine existingLine : sidebar.getLines()) {
			sidebar.updateLineScore(existingLine.getId(), existingLine.getLine() + 1);
		}

		sidebar.createLine(new Sidebar.ScoreboardLine(UUID.randomUUID().toString(), text, 0));
	}

	private static String getSidebarName(int counter) {
		String baseText = "BED WARS";
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
