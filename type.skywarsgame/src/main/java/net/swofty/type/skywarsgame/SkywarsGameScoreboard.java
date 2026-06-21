package net.swofty.type.skywarsgame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.game.SkywarsGameStatus;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SkywarsGameScoreboard {
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
				SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
				if (game == null) continue;
				Locale l = player.getLocale();

				SkywarsPlayer swPlayer = (SkywarsPlayer) player;

				List<Component> lines = new ArrayList<>();
				lines.add(I18n.t("scoreboard.common.date_line", Argument.tagResolver(Formatter.date("date", LocalDateTime.now(ZoneId.systemDefault()))), Argument.string("id", HypixelConst.getServerName())));
				lines.add(Component.space());

				if (game.getGameStatus() == SkywarsGameStatus.IN_PROGRESS) {
					long elapsedMs = System.currentTimeMillis() - game.getGameStartTime();
					long elapsedSeconds = elapsedMs / 1000;

					Component nextEventLine = getNextEventLine(game.getCurrentEvent(), elapsedSeconds);
					if (nextEventLine != null) {
						lines.add(I18n.t("scoreboard.skywars_game.next_event_label"));
						lines.add(nextEventLine);
						lines.add(Component.space());
					}

					int alive = (int) game.getPlayers().stream().filter(p -> !p.isEliminated()).count();
					lines.add(I18n.t("scoreboard.skywars_game.players_left_line", Component.text(String.valueOf(alive))));
					lines.add(Component.space());
					lines.add(I18n.t("scoreboard.skywars_game.kills_line", Component.text(String.valueOf(swPlayer.getKillsThisGame()))));
				} else if (game.getGameStatus() == SkywarsGameStatus.ENDING) {
					lines.add(I18n.t("scoreboard.skywars_game.top_killers_label"));

					java.util.List<SkywarsPlayer> topKillers = game.getPlayers().stream()
							.sorted((a, b) -> Integer.compare(b.getKillsThisGame(), a.getKillsThisGame()))
							.limit(3)
							.toList();

					Component[] places = {
						I18n.t("scoreboard.skywars_game.place_1st"),
						I18n.t("scoreboard.skywars_game.place_2nd"),
						I18n.t("scoreboard.skywars_game.place_3rd")
					};
					for (int i = 0; i < topKillers.size(); i++) {
						SkywarsPlayer killer = topKillers.get(i);
						lines.add(I18n.t("scoreboard.skywars_game.top_killer_line",
							places[i],
							Component.text(killer.getUsername()),
							Component.text(String.valueOf(killer.getKillsThisGame()))));
					}

					lines.add(Component.space());
					lines.add(I18n.t("scoreboard.skywars_game.your_kills_line", Component.text(String.valueOf(swPlayer.getKillsThisGame()))));
				} else if (game.getGameStatus() == SkywarsGameStatus.WAITING) {
					lines.add(I18n.t("scoreboard.skywars_game.players_line",
						Component.text(String.valueOf(game.getPlayers().size())),
						Component.text(String.valueOf(game.getGameType().getMaxPlayers()))));
					lines.add(Component.space());
					lines.add(I18n.t("scoreboard.skywars_game.waiting"));
				} else if (game.getGameStatus() == SkywarsGameStatus.STARTING) {
					lines.add(I18n.t("scoreboard.skywars_game.players_line",
						Component.text(String.valueOf(game.getPlayers().size())),
						Component.text(String.valueOf(game.getGameType().getMaxPlayers()))));
					lines.add(Component.space());
					lines.add(I18n.t("scoreboard.skywars_game.starting_in_line",
						Component.text(String.valueOf(game.getCountdown().getSecondsRemaining()))));
				}

				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.skywars_game.map_line", Component.text(game.getMapEntry().getName())));
				lines.add(I18n.t("scoreboard.skywars_game.mode_line", Component.text(game.getGameType().getDisplayName())));
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
		String baseText = I18n.string("scoreboard.skywars_game.title_base", locale);
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

	private static Component getNextEventLine(SkywarsGame.GameEvent currentEvent, long elapsedSeconds) {
        SkywarsGame.GameEvent nextEvent = currentEvent.getNext();

        return switch (nextEvent) {
            case FIRST_REFILL -> {
                long timeUntil = Math.max(0, SkywarsGame.FIRST_REFILL_SECONDS - elapsedSeconds);
				yield I18n.t("scoreboard.skywars_game.event_refill", Component.text(formatTime(timeUntil)));
            }
            case SECOND_REFILL -> {
                long timeUntil = Math.max(0, SkywarsGame.SECOND_REFILL_SECONDS - elapsedSeconds);
				yield I18n.t("scoreboard.skywars_game.event_refill", Component.text(formatTime(timeUntil)));
            }
            case DRAGON_SPAWN -> {
                long timeUntil = Math.max(0, SkywarsGame.DRAGON_SPAWN_SECONDS - elapsedSeconds);
				yield I18n.t("scoreboard.skywars_game.event_dragon", Component.text(formatTime(timeUntil)));
            }
            case GAME_END, GAME_START -> null;
        };
    }

    private static String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return minutes + ":" + String.format("%02d", secs);
    }
}
