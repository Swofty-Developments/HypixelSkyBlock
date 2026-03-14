package net.swofty.type.skywarsgame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

				SkywarsPlayer swPlayer = (SkywarsPlayer) player;

                List<Component> lines = new ArrayList<>();
                lines.add(HypixelScoreboard.legacy("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()) + " §8" + HypixelConst.getServerName()));
                lines.add(HypixelScoreboard.legacy("§7 "));

				if (game.getGameStatus() == SkywarsGameStatus.IN_PROGRESS) {
					long elapsedMs = System.currentTimeMillis() - game.getGameStartTime();
					long elapsedSeconds = elapsedMs / 1000;

					String nextEventLine = getNextEventLine(game.getCurrentEvent(), elapsedSeconds);
					if (nextEventLine != null) {
                        lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.next_event_label")));
                        lines.add(HypixelScoreboard.legacy(nextEventLine));
                        lines.add(HypixelScoreboard.legacy("§7 "));
					}

					int alive = (int) game.getPlayers().stream().filter(p -> !p.isEliminated()).count();
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.players_left_label") + alive));
                    lines.add(HypixelScoreboard.legacy("§7 "));
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.kills_label") + swPlayer.getKillsThisGame()));
				} else if (game.getGameStatus() == SkywarsGameStatus.ENDING) {
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.top_killers_label")));

					java.util.List<SkywarsPlayer> topKillers = game.getPlayers().stream()
							.sorted((a, b) -> Integer.compare(b.getKillsThisGame(), a.getKillsThisGame()))
							.limit(3)
							.toList();

					String[] places = {
							I18n.string("scoreboard.skywars_game.place_1st"),
							I18n.string("scoreboard.skywars_game.place_2nd"),
							I18n.string("scoreboard.skywars_game.place_3rd")
					};
					for (int i = 0; i < topKillers.size(); i++) {
						SkywarsPlayer killer = topKillers.get(i);
                        lines.add(HypixelScoreboard.legacy(places[i] + " §f" + killer.getUsername() + " §7- §a" + killer.getKillsThisGame()));
					}

                    lines.add(HypixelScoreboard.legacy("§7 "));
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.your_kills_label") + swPlayer.getKillsThisGame()));
				} else if (game.getGameStatus() == SkywarsGameStatus.WAITING) {
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.players_label") + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers()));
                    lines.add(HypixelScoreboard.legacy("§7 "));
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.waiting")));
				} else if (game.getGameStatus() == SkywarsGameStatus.STARTING) {
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.players_label") + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers()));
                    lines.add(HypixelScoreboard.legacy("§7 "));
                    lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.starting_in_label") + game.getCountdown().getSecondsRemaining() + I18n.string("scoreboard.skywars_game.starting_in_suffix")));
				}

                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.map_label") + game.getMapEntry().getName()));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_game.mode_label") + game.getGameType().getDisplayName()));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.common.footer")));

				if (!scoreboard.hasScoreboard(player)) {
					scoreboard.createScoreboard(player, getSidebarName(animationFrame));
				}

				scoreboard.updateLines(player, lines);
				scoreboard.updateTitle(player, getSidebarName(animationFrame));
			}
			return TaskSchedule.tick(4);
		});
	}

	public static void removeCache(Player player) {
		scoreboard.removeScoreboard(player);
	}

    private static Component getSidebarName(int counter) {
        return HypixelScoreboard.animatedSidebarName(
            I18n.string("scoreboard.skywars_game.title_base"),
            counter,
            NamedTextColor.WHITE,
            NamedTextColor.YELLOW,
            NamedTextColor.GOLD,
            NamedTextColor.WHITE,
            NamedTextColor.YELLOW,
            7,
            8,
            18,
            25,
            29,
            Component.empty()
        );
	}

    private static String getNextEventLine(SkywarsGame.GameEvent currentEvent, long elapsedSeconds) {
        SkywarsGame.GameEvent nextEvent = currentEvent.getNext();

        return switch (nextEvent) {
            case FIRST_REFILL -> {
                long timeUntil = Math.max(0, SkywarsGame.FIRST_REFILL_SECONDS - elapsedSeconds);
                yield I18n.string("scoreboard.skywars_game.event_refill", Map.of("time", formatTime(timeUntil)));
            }
            case SECOND_REFILL -> {
                long timeUntil = Math.max(0, SkywarsGame.SECOND_REFILL_SECONDS - elapsedSeconds);
                yield I18n.string("scoreboard.skywars_game.event_refill", Map.of("time", formatTime(timeUntil)));
            }
            case DRAGON_SPAWN -> {
                long timeUntil = Math.max(0, SkywarsGame.DRAGON_SPAWN_SECONDS - elapsedSeconds);
                yield I18n.string("scoreboard.skywars_game.event_dragon", Map.of("time", formatTime(timeUntil)));
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
