package net.swofty.type.murdermysterygame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.role.GameRole;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MurderMysteryGameScoreboard {
	private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
	private static Integer animationFrame = 0;
	private static final long GAME_DURATION_MS = 5 * 60 * 1000;

	public static void start() {
		Scheduler scheduler = MinecraftServer.getSchedulerManager();

		scheduler.submitTask(() -> {
			animationFrame++;
			if (animationFrame > 50) {
				animationFrame = 0;
			}

			for (Game game : TypeMurderMysteryGameLoader.getGames()) {
				for (MurderMysteryPlayer player : game.getPlayers()) {
					if (player.getInstance() == null) continue;

					List<String> lines = new ArrayList<>();
					lines.add("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName());
					lines.add("§7 ");

					if (game.getGameStatus() == GameStatus.WAITING) {
						lines.add("§fMap: §a" + game.getMapEntry().getName());
						lines.add("§fPlayers: §a" + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers());
						lines.add("§7 ");
						if (game.getCountdown().isActive()) {
							lines.add("§fStarting in §a" + game.getCountdown().getSecondsRemaining() + "s");
						} else {
							lines.add("§fWaiting for players...");
						}
						lines.add("§7 ");
						lines.add("§fMode: §a" + game.getGameType().getDisplayName());

						int playerCount = game.getPlayers().size();
						int murdererChance = playerCount > 0 ? Math.round(100f / playerCount) : 0;
						int detectiveChance = playerCount > 0 ? Math.round(100f / playerCount) : 0;
						Component actionBar = Component.empty()
								.append(Component.text("Murderer Chance: " + murdererChance + "%", NamedTextColor.RED))
								.append(Component.text("    ", NamedTextColor.GRAY))
								.append(Component.text("Detective Chance: " + detectiveChance + "%", NamedTextColor.AQUA));
						player.sendActionBar(actionBar);
					} else if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
						GameRole role = game.getRoleManager().getRole(player.getUuid());

						if (player.isEliminated()) {
							lines.add("§7§lSPECTATING");
							lines.add("§7 ");

							if (role != null) {
								lines.add("§fYour Role: " + getScoreboardRoleColor(role) + role.getDisplayName());
							}
							lines.add("§7 ");

							int playersAlive = game.getRoleManager().countAliveWithRole(GameRole.INNOCENT)
									+ game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE)
									+ game.getRoleManager().countAliveWithRole(GameRole.MURDERER);
							lines.add("§fPlayers Alive: §a" + playersAlive);

							String timeLeft = formatTimeRemaining(game.getGameStartTime());
							lines.add("§fTime Left: §a" + timeLeft);
							lines.add("§7 ");

							boolean detectiveAlive = game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE) > 0;
							String detectiveStatus = detectiveAlive ? "§aAlive" : "§cDead";
							lines.add("§fDetective: " + detectiveStatus);
							lines.add("§7 ");

							lines.add("§fMap: §a" + game.getMapEntry().getName());
						} else {
							if (role != null) {
								lines.add("§fRole: " + getScoreboardRoleColor(role) + role.getDisplayName());
							}
							lines.add("§7 ");

							int innocentsLeft = game.getRoleManager().countAliveWithRole(GameRole.INNOCENT)
									+ game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE);
							lines.add("§fInnocents Left: §a" + innocentsLeft);

							String timeLeft = formatTimeRemaining(game.getGameStartTime());
							lines.add("§fTime Left: §a" + timeLeft);
							lines.add("§7 ");

							boolean detectiveAlive = game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE) > 0;
							String detectiveStatus = detectiveAlive ? "§aAlive" : "§cDead";
							lines.add("§fDetective: " + detectiveStatus);
							lines.add("§7 ");

							lines.add("§fMap: §a" + game.getMapEntry().getName());
						}
					} else if (game.getGameStatus() == GameStatus.ENDING) {
						lines.add("§a§lGAME OVER!");
						lines.add("§7 ");

						GameRole role = game.getRoleManager().getRole(player.getUuid());
						if (role != null) {
							lines.add("§fYour Role: " + getScoreboardRoleColor(role) + role.getDisplayName());
						}
						lines.add("§7 ");

						int kills = player.getKillsThisGame();
						if (kills > 0) {
							lines.add("§fYour Kills: §a" + kills);
						}

						int tokens = player.getTokensEarnedThisGame();
						lines.add("§fTokens Earned: §6" + tokens);
						lines.add("§7 ");

						lines.add("§fMap: §a" + game.getMapEntry().getName());
						lines.add("§fMode: §a" + game.getGameType().getDisplayName());
					}

					lines.add("§7 ");
					lines.add("§ewww.hypixel.net");

					if (!scoreboard.hasScoreboard(player)) {
						scoreboard.createScoreboard(player, getSidebarName(animationFrame));
					}

					scoreboard.updateLines(player, lines);
					scoreboard.updateTitle(player, getSidebarName(animationFrame));
				}
			}
			return TaskSchedule.tick(4);
		});
	}

	private static String getScoreboardRoleColor(GameRole role) {
		return switch (role) {
			case MURDERER -> "§c";
			case DETECTIVE -> "§b";
			case INNOCENT -> "§a";
			case ASSASSIN -> "§6";
		};
	}

	private static String formatTimeRemaining(long gameStartTime) {
		if (gameStartTime == 0) return "5:00";
		long elapsed = System.currentTimeMillis() - gameStartTime;
		long remaining = GAME_DURATION_MS - elapsed;
		if (remaining < 0) remaining = 0;

		long minutes = remaining / 60000;
		long seconds = (remaining % 60000) / 1000;
		return String.format("%d:%02d", minutes, seconds);
	}

	public static void removeCache(Player player) {
		scoreboard.removeScoreboard(player);
	}

	private static String getSidebarName(int counter) {
		String baseText = "MURDER MYSTERY";
		String[] colors = {"§f§l", "§6§l", "§e§l"};
		String endColor = "§a§l";

		if (counter > 0 && counter <= 14) {
			return colors[0] + baseText.substring(0, Math.min(counter - 1, baseText.length())) +
					colors[1] + (counter <= baseText.length() ? String.valueOf(baseText.charAt(counter - 1)) : "") +
					colors[2] + (counter < baseText.length() ? baseText.substring(counter) : "") +
					endColor;
		} else if ((counter >= 15 && counter <= 25) || (counter >= 35 && counter <= 45)) {
			return colors[0] + baseText + endColor;
		} else {
			return colors[2] + baseText + endColor;
		}
	}
}
