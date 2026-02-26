package net.swofty.type.skywarsgame;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.game.SkywarsGameStatus;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

				List<String> lines = new ArrayList<>();
				lines.add("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName());
				lines.add("§7 ");

				if (game.getGameStatus() == SkywarsGameStatus.IN_PROGRESS) {
					long elapsedMs = System.currentTimeMillis() - game.getGameStartTime();
					long elapsedSeconds = elapsedMs / 1000;

					String nextEventLine = getNextEventLine(game.getCurrentEvent(), elapsedSeconds);
					if (nextEventLine != null) {
						lines.add("§fNext Event:");
						lines.add(nextEventLine);
						lines.add("§7 ");
					}

					int alive = (int) game.getPlayers().stream().filter(p -> !p.isEliminated()).count();
					lines.add("§fPlayers Left: §a" + alive);
					lines.add("§7 ");
					lines.add("§fKills: §a" + swPlayer.getKillsThisGame());
				} else if (game.getGameStatus() == SkywarsGameStatus.ENDING) {
					lines.add("§fTop Killers:");

					java.util.List<SkywarsPlayer> topKillers = game.getPlayers().stream()
							.sorted((a, b) -> Integer.compare(b.getKillsThisGame(), a.getKillsThisGame()))
							.limit(3)
							.toList();

					String[] places = {"§61st", "§72nd", "§c3rd"};
					for (int i = 0; i < topKillers.size(); i++) {
						SkywarsPlayer killer = topKillers.get(i);
						lines.add(places[i] + " §f" + killer.getUsername() + " §7- §a" + killer.getKillsThisGame());
					}

					lines.add("§7 ");
					lines.add("§fYour Kills: §a" + swPlayer.getKillsThisGame());
				} else if (game.getGameStatus() == SkywarsGameStatus.WAITING) {
					lines.add("§fPlayers: §a" + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers());
					lines.add("§7 ");
					lines.add("Waiting...");
				} else if (game.getGameStatus() == SkywarsGameStatus.STARTING) {
					lines.add("§fPlayers: §a" + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers());
					lines.add("§7 ");
					lines.add("§fStarting in §a" + game.getCountdown().getSecondsRemaining() + "s");
				}

				lines.add("§7 ");
				lines.add("§fMap: §a" + game.getMapEntry().getName());
				lines.add("§fMode: §a" + game.getGameType().getDisplayName());
				lines.add("§7 ");
				lines.add("§ewww.hypixel.net");

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

	private static String getSidebarName(int counter) {
		String baseText = "SKYWARS";
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

    private static String getNextEventLine(SkywarsGame.GameEvent currentEvent, long elapsedSeconds) {
        SkywarsGame.GameEvent nextEvent = currentEvent.getNext();

        return switch (nextEvent) {
            case FIRST_REFILL -> {
                long timeUntil = Math.max(0, SkywarsGame.FIRST_REFILL_SECONDS - elapsedSeconds);
                yield "§aRefill " + formatTime(timeUntil);
            }
            case SECOND_REFILL -> {
                long timeUntil = Math.max(0, SkywarsGame.SECOND_REFILL_SECONDS - elapsedSeconds);
                yield "§aRefill " + formatTime(timeUntil);
            }
            case DRAGON_SPAWN -> {
                long timeUntil = Math.max(0, SkywarsGame.DRAGON_SPAWN_SECONDS - elapsedSeconds);
                yield "§cDragon " + formatTime(timeUntil);
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
