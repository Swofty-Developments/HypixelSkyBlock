package net.swofty.type.murdermysterygame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.role.GameRole;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

                    List<Component> lines = new ArrayList<>();
                    lines.add(HypixelScoreboard.legacy("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()) + " §8" + HypixelConst.getServerName()));
                    lines.add(HypixelScoreboard.legacy("§7 "));

					if (game.getGameStatus() == GameStatus.WAITING) {
                        lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.map_label") + game.getMapEntry().getName()));
                        lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.players_label") + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers()));
                        lines.add(HypixelScoreboard.legacy("§7 "));
						if (game.getCountdown().isActive()) {
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.starting_in_label") + game.getCountdown().getSecondsRemaining() + I18n.string("scoreboard.murdermystery_game.starting_in_suffix")));
						} else {
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.waiting_for_players")));
						}
                        lines.add(HypixelScoreboard.legacy("§7 "));
                        lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.mode_label") + game.getGameType().getDisplayName()));

						int playerCount = game.getPlayers().size();
						int murdererChance = playerCount > 0 ? Math.round(100f / playerCount) : 0;
						int detectiveChance = playerCount > 0 ? Math.round(100f / playerCount) : 0;
						Component actionBar = Component.empty()
								.append(Component.text(I18n.string("scoreboard.murdermystery_game.actionbar.murderer_chance", Map.of("chance", String.valueOf(murdererChance))), NamedTextColor.RED))
								.append(Component.text("    ", NamedTextColor.GRAY))
								.append(Component.text(I18n.string("scoreboard.murdermystery_game.actionbar.detective_chance", Map.of("chance", String.valueOf(detectiveChance))), NamedTextColor.AQUA));
						player.sendActionBar(actionBar);
					} else if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
						GameRole role = game.getRoleManager().getRole(player.getUuid());

						if (player.isEliminated()) {
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.spectating_label")));
                            lines.add(HypixelScoreboard.legacy("§7 "));

							if (role != null) {
                                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.your_role_label") + " " + getScoreboardRoleColor(role) + role.getDisplayName()));
							}
                            lines.add(HypixelScoreboard.legacy("§7 "));

							int playersAlive = game.getRoleManager().countAliveWithRole(GameRole.INNOCENT)
									+ game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE)
									+ game.getRoleManager().countAliveWithRole(GameRole.MURDERER);
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.players_alive_label") + playersAlive));

							String timeLeft = formatTimeRemaining(game.getGameStartTime());
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.time_left_label") + timeLeft));
                            lines.add(HypixelScoreboard.legacy("§7 "));

							boolean detectiveAlive = game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE) > 0;
							String detectiveStatus = detectiveAlive
									? I18n.string("scoreboard.murdermystery_game.detective_alive")
									: I18n.string("scoreboard.murdermystery_game.detective_dead");
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.detective_label") + " " + detectiveStatus));
                            lines.add(HypixelScoreboard.legacy("§7 "));

                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.map_label") + game.getMapEntry().getName()));
						} else {
							if (role != null) {
                                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.role_label") + " " + getScoreboardRoleColor(role) + role.getDisplayName()));
							}
                            lines.add(HypixelScoreboard.legacy("§7 "));

							int innocentsLeft = game.getRoleManager().countAliveWithRole(GameRole.INNOCENT)
									+ game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE);
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.innocents_left_label") + innocentsLeft));

							String timeLeft = formatTimeRemaining(game.getGameStartTime());
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.time_left_label") + timeLeft));
                            lines.add(HypixelScoreboard.legacy("§7 "));

							boolean detectiveAlive = game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE) > 0;
							String detectiveStatus = detectiveAlive
									? I18n.string("scoreboard.murdermystery_game.detective_alive")
									: I18n.string("scoreboard.murdermystery_game.detective_dead");
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.detective_label") + " " + detectiveStatus));
                            lines.add(HypixelScoreboard.legacy("§7 "));

                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.map_label") + game.getMapEntry().getName()));
						}
					} else if (game.getGameStatus() == GameStatus.ENDING) {
                        lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.game_over")));
                        lines.add(HypixelScoreboard.legacy("§7 "));

						GameRole role = game.getRoleManager().getRole(player.getUuid());
						if (role != null) {
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.your_role_label") + " " + getScoreboardRoleColor(role) + role.getDisplayName()));
						}
                        lines.add(HypixelScoreboard.legacy("§7 "));

						int kills = player.getKillsThisGame();
						if (kills > 0) {
                            lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.your_kills_label") + kills));
						}

						int tokens = player.getTokensEarnedThisGame();
                        lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.tokens_earned_label") + tokens));
                        lines.add(HypixelScoreboard.legacy("§7 "));

                        lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.map_label") + game.getMapEntry().getName()));
                        lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_game.mode_label") + game.getGameType().getDisplayName()));
					}

                    lines.add(HypixelScoreboard.legacy("§7 "));
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.common.footer")));

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
			case MURDERER -> I18n.string("scoreboard.murdermystery_game.role_color.murderer");
			case DETECTIVE -> I18n.string("scoreboard.murdermystery_game.role_color.detective");
			case INNOCENT -> I18n.string("scoreboard.murdermystery_game.role_color.innocent");
			case ASSASSIN -> I18n.string("scoreboard.murdermystery_game.role_color.assassin");
		};
	}

	private static String formatTimeRemaining(long gameStartTime) {
		if (gameStartTime == 0) return I18n.string("scoreboard.murdermystery_game.time_left_default");
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

    private static Component getSidebarName(int counter) {
        return HypixelScoreboard.animatedSidebarName(
            I18n.string("scoreboard.murdermystery_game.title_base"),
            counter,
            NamedTextColor.WHITE,
            NamedTextColor.GOLD,
            NamedTextColor.YELLOW,
            NamedTextColor.WHITE,
            NamedTextColor.YELLOW,
            14,
            15,
            25,
            35,
            45,
            Component.text("MYSTERY", NamedTextColor.GREEN, net.kyori.adventure.text.format.TextDecoration.BOLD)
        );
	}
}
