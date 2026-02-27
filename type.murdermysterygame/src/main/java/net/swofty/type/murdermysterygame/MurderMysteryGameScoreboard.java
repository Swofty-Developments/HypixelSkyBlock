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
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
					Locale l = player.getLocale();

					List<String> lines = new ArrayList<>();
					lines.add("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format", l)).format(new Date()) + " §8" + HypixelConst.getServerName());
					lines.add("§7 ");

					if (game.getGameStatus() == GameStatus.WAITING) {
						lines.add(I18n.string("scoreboard.murdermystery_game.map_label", l) + game.getMapEntry().getName());
						lines.add(I18n.string("scoreboard.murdermystery_game.players_label", l) + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers());
						lines.add("§7 ");
						if (game.getCountdown().isActive()) {
							lines.add(I18n.string("scoreboard.murdermystery_game.starting_in_label", l) + game.getCountdown().getSecondsRemaining() + I18n.string("scoreboard.murdermystery_game.starting_in_suffix", l));
						} else {
							lines.add(I18n.string("scoreboard.murdermystery_game.waiting_for_players", l));
						}
						lines.add("§7 ");
						lines.add(I18n.string("scoreboard.murdermystery_game.mode_label", l) + game.getGameType().getDisplayName());

						int playerCount = game.getPlayers().size();
						int murdererChance = playerCount > 0 ? Math.round(100f / playerCount) : 0;
						int detectiveChance = playerCount > 0 ? Math.round(100f / playerCount) : 0;
						Component actionBar = Component.empty()
								.append(Component.text(I18n.string("scoreboard.murdermystery_game.actionbar.murderer_chance", l, Map.of("chance", String.valueOf(murdererChance))), NamedTextColor.RED))
								.append(Component.text("    ", NamedTextColor.GRAY))
								.append(Component.text(I18n.string("scoreboard.murdermystery_game.actionbar.detective_chance", l, Map.of("chance", String.valueOf(detectiveChance))), NamedTextColor.AQUA));
						player.sendActionBar(actionBar);
					} else if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
						GameRole role = game.getRoleManager().getRole(player.getUuid());

						if (player.isEliminated()) {
							lines.add(I18n.string("scoreboard.murdermystery_game.spectating_label", l));
							lines.add("§7 ");

							if (role != null) {
								lines.add(I18n.string("scoreboard.murdermystery_game.your_role_label", l) + " " + getScoreboardRoleColor(role, l) + role.getDisplayName());
							}
							lines.add("§7 ");

							int playersAlive = game.getRoleManager().countAliveWithRole(GameRole.INNOCENT)
									+ game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE)
									+ game.getRoleManager().countAliveWithRole(GameRole.MURDERER);
							lines.add(I18n.string("scoreboard.murdermystery_game.players_alive_label", l) + playersAlive);

							String timeLeft = formatTimeRemaining(game.getGameStartTime(), l);
							lines.add(I18n.string("scoreboard.murdermystery_game.time_left_label", l) + timeLeft);
							lines.add("§7 ");

							boolean detectiveAlive = game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE) > 0;
							String detectiveStatus = detectiveAlive
									? I18n.string("scoreboard.murdermystery_game.detective_alive", l)
									: I18n.string("scoreboard.murdermystery_game.detective_dead", l);
							lines.add(I18n.string("scoreboard.murdermystery_game.detective_label", l) + " " + detectiveStatus);
							lines.add("§7 ");

							lines.add(I18n.string("scoreboard.murdermystery_game.map_label", l) + game.getMapEntry().getName());
						} else {
							if (role != null) {
								lines.add(I18n.string("scoreboard.murdermystery_game.role_label", l) + " " + getScoreboardRoleColor(role, l) + role.getDisplayName());
							}
							lines.add("§7 ");

							int innocentsLeft = game.getRoleManager().countAliveWithRole(GameRole.INNOCENT)
									+ game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE);
							lines.add(I18n.string("scoreboard.murdermystery_game.innocents_left_label", l) + innocentsLeft);

							String timeLeft = formatTimeRemaining(game.getGameStartTime(), l);
							lines.add(I18n.string("scoreboard.murdermystery_game.time_left_label", l) + timeLeft);
							lines.add("§7 ");

							boolean detectiveAlive = game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE) > 0;
							String detectiveStatus = detectiveAlive
									? I18n.string("scoreboard.murdermystery_game.detective_alive", l)
									: I18n.string("scoreboard.murdermystery_game.detective_dead", l);
							lines.add(I18n.string("scoreboard.murdermystery_game.detective_label", l) + " " + detectiveStatus);
							lines.add("§7 ");

							lines.add(I18n.string("scoreboard.murdermystery_game.map_label", l) + game.getMapEntry().getName());
						}
					} else if (game.getGameStatus() == GameStatus.ENDING) {
						lines.add(I18n.string("scoreboard.murdermystery_game.game_over", l));
						lines.add("§7 ");

						GameRole role = game.getRoleManager().getRole(player.getUuid());
						if (role != null) {
							lines.add(I18n.string("scoreboard.murdermystery_game.your_role_label", l) + " " + getScoreboardRoleColor(role, l) + role.getDisplayName());
						}
						lines.add("§7 ");

						int kills = player.getKillsThisGame();
						if (kills > 0) {
							lines.add(I18n.string("scoreboard.murdermystery_game.your_kills_label", l) + kills);
						}

						int tokens = player.getTokensEarnedThisGame();
						lines.add(I18n.string("scoreboard.murdermystery_game.tokens_earned_label", l) + tokens);
						lines.add("§7 ");

						lines.add(I18n.string("scoreboard.murdermystery_game.map_label", l) + game.getMapEntry().getName());
						lines.add(I18n.string("scoreboard.murdermystery_game.mode_label", l) + game.getGameType().getDisplayName());
					}

					lines.add("§7 ");
					lines.add(I18n.string("scoreboard.common.footer", l));

					if (!scoreboard.hasScoreboard(player)) {
						scoreboard.createScoreboard(player, getSidebarName(animationFrame, l));
					}

					scoreboard.updateLines(player, lines);
					scoreboard.updateTitle(player, getSidebarName(animationFrame, l));
				}
			}
			return TaskSchedule.tick(4);
		});
	}

	private static String getScoreboardRoleColor(GameRole role, Locale locale) {
		return switch (role) {
			case MURDERER -> I18n.string("scoreboard.murdermystery_game.role_color.murderer", locale);
			case DETECTIVE -> I18n.string("scoreboard.murdermystery_game.role_color.detective", locale);
			case INNOCENT -> I18n.string("scoreboard.murdermystery_game.role_color.innocent", locale);
			case ASSASSIN -> I18n.string("scoreboard.murdermystery_game.role_color.assassin", locale);
		};
	}

	private static String formatTimeRemaining(long gameStartTime, Locale locale) {
		if (gameStartTime == 0) return I18n.string("scoreboard.murdermystery_game.time_left_default", locale);
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

	private static String getSidebarName(int counter, Locale locale) {
		String baseText = I18n.string("scoreboard.murdermystery_game.title_base", locale);
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
