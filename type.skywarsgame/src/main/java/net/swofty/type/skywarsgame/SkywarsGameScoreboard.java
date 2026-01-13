package net.swofty.type.skywarsgame;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skywars.SkywarsLevelColor;
import net.swofty.commons.skywars.SkywarsLevelUtil;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.game.SkywarsGameStatus;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkywarsGameScoreboard {
    private static final Map<UUID, Sidebar> sidebarCache = new HashMap<>();
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
                SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);

                int level = 1;
                if (handler != null) {
                    long experience = handler.get(SkywarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue();
                    level = SkywarsLevelUtil.calculateLevel(experience);
                }

                Sidebar sidebar = new Sidebar(Component.text(getSidebarName(animationFrame)));

                addLine("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName(), sidebar);
                addLine("§7 ", sidebar);

                if (game.getGameStatus() == SkywarsGameStatus.IN_PROGRESS) {
                    long elapsedMs = System.currentTimeMillis() - game.getGameStartTime();
                    long elapsedSeconds = elapsedMs / 1000;

                    String nextEventLine = getNextEventLine(game.getCurrentEvent(), elapsedSeconds);
                    if (nextEventLine != null) {
                        addLine("§fNext Event:", sidebar);
                        addLine(nextEventLine, sidebar);
                        addLine("§7 ", sidebar);
                    }

                    int alive = (int) game.getPlayers().stream().filter(p -> !p.isEliminated()).count();
                    addLine("§fPlayers Left: §a" + alive, sidebar);
                    addLine("§7 ", sidebar);
                    addLine("§fKills: §a" + swPlayer.getKillsThisGame(), sidebar);
                } else if (game.getGameStatus() == SkywarsGameStatus.ENDING) {
                    addLine("§fTop Killers:", sidebar);

                    java.util.List<SkywarsPlayer> topKillers = game.getPlayers().stream()
                            .sorted((a, b) -> Integer.compare(b.getKillsThisGame(), a.getKillsThisGame()))
                            .limit(3)
                            .toList();

                    String[] places = {"§61st", "§72nd", "§c3rd"};
                    for (int i = 0; i < topKillers.size(); i++) {
                        SkywarsPlayer killer = topKillers.get(i);
                        addLine(places[i] + " §f" + killer.getUsername() + " §7- §a" + killer.getKillsThisGame(), sidebar);
                    }

                    addLine("§7 ", sidebar);
                    addLine("§fYour Kills: §a" + swPlayer.getKillsThisGame(), sidebar);
                } else if (game.getGameStatus() == SkywarsGameStatus.WAITING) {
                    addLine("§fPlayers: §a" + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers(), sidebar);
                    addLine("§7 ", sidebar);
                    addLine("Waiting...", sidebar);
                } else if (game.getGameStatus() == SkywarsGameStatus.STARTING) {
                    addLine("§fPlayers: §a" + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers(), sidebar);
                    addLine("§7 ", sidebar);
                    addLine("§fStarting in §a" + game.getCountdown().getSecondsRemaining() + "s", sidebar);
                }

                addLine("§7 ", sidebar);
                addLine("§fMap: §a" + game.getMapEntry().getName(), sidebar);
                addLine("§fMode: §a" + game.getGameType().getDisplayName(), sidebar);
                addLine("§7 ", sidebar);
                addLine("§ewww.hypixel.net", sidebar);

                Sidebar oldSidebar = sidebarCache.get(player.getUuid());
                
                sidebar.addViewer(player);
                
                sidebarCache.put(player.getUuid(), sidebar);


                if (oldSidebar != null && oldSidebar != sidebar) {
                    final Sidebar finalOldSidebar = oldSidebar;
                    scheduler.scheduleNextTick(() -> {
                        if (sidebarCache.get(player.getUuid()) == sidebar) {
                            try {
                                finalOldSidebar.removeViewer(player);
                            } catch (Exception e) {
                            }
                        }
                    });
                }
            }
            return TaskSchedule.tick(10);
        });
    }

    public static void removeCache(Player player) {
        sidebarCache.remove(player.getUuid());
    }

    private static void addLine(String text, Sidebar sidebar) {

        int score = sidebar.getLines().size();
        sidebar.createLine(new Sidebar.ScoreboardLine(UUID.randomUUID().toString(), Component.text(text), score));
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
