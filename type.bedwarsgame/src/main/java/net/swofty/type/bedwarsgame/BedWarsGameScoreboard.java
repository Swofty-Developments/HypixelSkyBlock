package net.swofty.type.bedwarsgame;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.VersionConst;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGameEventManager;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BedWarsGameScoreboard {
    private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
    private static Integer prototypeName = 0;

    public static void start(BedWarsGame game) {
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
                lines.add("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName());
                lines.add("§7 ");

                if (game.getState() == GameState.WAITING || game.getState() == GameState.STARTING) {
                    lines.add("§fMap: §a" + game.getMapEntry().getName());
                    lines.add("§fPlayers: §a" + game.getPlayers().size() + "/" + game.getMapEntry().getConfiguration().getTeams().size());
                    lines.add("§7 ");
                    if (game.getState() == GameState.STARTING) {
                        long seconds = game.getCountdown().getRemainingSeconds();
                        lines.add("§fStarting in §a" + seconds + "s");
                    } else {
                        lines.add("§fWaiting...");
                    }
                    lines.add("§7 ");
                    lines.add("§fMode: §a" + game.getGameType().getDisplayName());
                    lines.add("§fVersion: §7v" + VersionConst.BED_WARS_VERSION);
                } else {
                    BedWarsGameEventManager.GamePhase nextGamePhase = game.getGameEventManager().getCurrentPhase().next();
                    String eventName = nextGamePhase != null
                        ? nextGamePhase.getDisplayName()
                        : game.getGameEventManager().getCurrentEvent().getDisplayName();
                    long seconds = game.getGameEventManager().getSecondsUntilNextPhase();
                    long minutesPart = seconds / 60;
                    long secondsPart = seconds % 60;
                    String timeLeft = String.format("%d:%02d", minutesPart, secondsPart);
                    lines.add("§f" + eventName + " in §a" + timeLeft);
                    lines.add("§7 ");

                    for (BedWarsTeam team : game.getTeams()) {
                        TeamKey teamKey = team.getTeamKey();
                        String teamName = teamKey.getName();
                        String teamInitial = teamName.substring(0, 1).toUpperCase();

                        String bedStatus;
                        if (!team.hasPlayers()) {
                            bedStatus = "§c✖";
                        } else if (team.isBedAlive()) {
                            bedStatus = "§a✔";
                        } else {
                            int alivePlayers = game.getPlayersOnTeam(teamKey).stream()
                                .filter(p -> !Boolean.TRUE.equals(p.getTag(BedWarsGame.ELIMINATED_TAG)))
                                .toList()
                                .size();
                            if (alivePlayers > 0) {
                                bedStatus = "§c" + alivePlayers;
                            } else {
                                bedStatus = "§c✖";
                            }
                        }
                        lines.add(String.format("%s%s §f%s %s", teamKey.chatColor(), teamInitial, teamName, bedStatus));
                    }
                }
                lines.add("§7 ");
                lines.add("§ewww.hypixel.net");

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
