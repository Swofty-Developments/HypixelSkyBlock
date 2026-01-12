package net.swofty.type.murdermysterylobby;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardPeriod;
import net.swofty.commons.murdermystery.MurderMysteryModeStats;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MurderMysteryLobbyScoreboard {
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
                if (player.getDataHandler() == null) {
                    continue;
                }

                Sidebar sidebar = sidebarCache.get(player.getUuid());
                
                if (sidebar == null) {
                    sidebar = new Sidebar(Component.text(getSidebarName(animationFrame)));

                    // Get player stats
                    MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(player);
                    long totalKills = 0;
                    long totalWins = 0;
                    long detectiveWins = 0;
                    long murdererWins = 0;
                    long tokens = 0;

                    if (handler != null) {
                        DatapointMurderMysteryModeStats statsDP = handler.get(
                                MurderMysteryDataHandler.Data.MODE_STATS,
                                DatapointMurderMysteryModeStats.class);
                        MurderMysteryModeStats stats = statsDP.getValue();
                        totalKills = stats.getTotalKills(MurderMysteryLeaderboardPeriod.LIFETIME);
                        totalWins = stats.getTotalWins(MurderMysteryLeaderboardPeriod.LIFETIME);
                        detectiveWins = stats.getTotalDetectiveWins(MurderMysteryLeaderboardPeriod.LIFETIME);
                        murdererWins = stats.getTotalMurdererWins(MurderMysteryLeaderboardPeriod.LIFETIME);
                        tokens = stats.getTotalTokens(MurderMysteryLeaderboardPeriod.LIFETIME);
                    }

                    addLine("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName(), sidebar);
                    addLine("§7 ", sidebar);
                    addLine("§fTotal Kills: §a" + totalKills, sidebar);
                    addLine("§fTotal Wins: §a" + totalWins, sidebar);
                    addLine("§7 ", sidebar);
                    addLine("§fWins as Detective: §a" + detectiveWins, sidebar);
                    addLine("§fWins as Murderer: §a" + murdererWins, sidebar);
                    addLine("§7 ", sidebar);
                    addLine("§fTokens: §2" + tokens, sidebar);
                    addLine("§7 ", sidebar);
                    addLine("§ewww.hypixel.net", sidebar);

                    sidebar.addViewer(player);
                    sidebarCache.put(player.getUuid(), sidebar);
                }

            }
            return TaskSchedule.tick(5);
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
        String baseText = "MURDER MYSTERY";
        String[] colors = {"§f§l", "§6§l", "§e§l"};
        String endColor = "§a§l";

        if (counter > 0 && counter <= 14) {
            return colors[0] + baseText.substring(0, counter - 1) +
                    colors[1] + baseText.charAt(counter - 1) +
                    colors[2] + baseText.substring(counter) +
                    endColor;
        } else if ((counter >= 15 && counter <= 25) || (counter >= 35 && counter <= 45)) {
            return colors[0] + baseText + endColor;
        } else {
            return colors[2] + baseText + endColor;
        }
    }
}
