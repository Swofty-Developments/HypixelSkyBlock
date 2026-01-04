package net.swofty.type.skywarslobby;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skywars.SkywarsLeaderboardMode;
import net.swofty.commons.skywars.SkywarsLeaderboardPeriod;
import net.swofty.commons.skywars.SkywarsLevelColor;
import net.swofty.type.skywarslobby.level.SkywarsLevelRegistry;
import net.swofty.commons.skywars.SkywarsModeStats;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsModeStats;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class SkywarsLobbyScoreboard {
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
                SkywarsDataHandler swDataHandler = SkywarsDataHandler.getUser(player);

                if (swDataHandler == null) {
                    continue;
                }

                long experience = swDataHandler.get(SkywarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue();
                long souls = swDataHandler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).getValue();
                long coins = swDataHandler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
                long tokens = swDataHandler.get(SkywarsDataHandler.Data.TOKENS, DatapointLong.class).getValue();

                SkywarsModeStats modeStats = swDataHandler.get(SkywarsDataHandler.Data.MODE_STATS, DatapointSkywarsModeStats.class).getValue();

                // Solo = SOLO_NORMAL + SOLO_INSANE (LIFETIME)
                long soloKills = modeStats.getKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + modeStats.getKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long soloWins = modeStats.getWins(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + modeStats.getWins(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);

                // Doubles
                long doublesKills = modeStats.getKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long doublesWins = modeStats.getWins(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);

                int level = SkywarsLevelRegistry.calculateLevel(experience);

                if (sidebarCache.containsKey(player.getUuid())) {
                    sidebarCache.get(player.getUuid()).removeViewer(player);
                }

                Sidebar sidebar = new Sidebar(Component.text(getSidebarName(animationFrame)));

                addLine("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName(), sidebar);
                addLine("§7 ", sidebar);
                addLine("§fYour Level: " + SkywarsLevelColor.getLevelDisplay(level), sidebar);
                addLine("§7 ", sidebar);
                addLine("§fSolo Kills: §a" + soloKills, sidebar);
                addLine("§fSolo Wins: §a" + soloWins, sidebar);
                addLine("§fDoubles Kills: §a" + doublesKills, sidebar);
                addLine("§fDoubles Wins: §a" + doublesWins, sidebar);
                addLine("§7 ", sidebar);
                addLine("§fCoins: §6" + coins, sidebar);
                addLine("§fSouls: §b" + souls, sidebar);
                addLine("§fTokens: §2" + tokens, sidebar);
                addLine("§7 ", sidebar);
                addLine("§ewww.hypixel.net", sidebar);

                sidebar.addViewer(player);
                sidebarCache.put(player.getUuid(), sidebar);
            }
            return TaskSchedule.tick(5);
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
}
