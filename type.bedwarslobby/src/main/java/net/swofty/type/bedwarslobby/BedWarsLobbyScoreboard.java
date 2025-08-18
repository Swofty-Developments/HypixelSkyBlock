package net.swofty.type.bedwarslobby;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgeneric.data.BedWarsDataHandler;
import net.swofty.type.bedwarsgeneric.util.LevelColor;
import net.swofty.type.bedwarsgeneric.util.LevelUtil;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BedWarsLobbyScoreboard {
    private static final DecimalFormat FORMAT = new DecimalFormat("#,###.#");
    private static final Map<UUID, Sidebar> sidebarCache = new HashMap<>();
    private static Integer prototypeName = 0;

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        // Scoreboard Updater
        scheduler.submitTask(() -> {
            prototypeName++;
            if (prototypeName > 50) {
                prototypeName = 0;
            }

            for (HypixelPlayer player : HypixelGenericLoader.getLoadedPlayers()) {
                HypixelDataHandler dataHandler = player.getDataHandler();
                BedWarsDataHandler bwDataHandler = BedWarsDataHandler.getUser(player);

                if (dataHandler == null || bwDataHandler == null) {
                    continue;
                }

                long experience = bwDataHandler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue();
                int progress = LevelUtil.calculateExperienceSinceLastLevel(experience);
                int maxExperience = LevelUtil.calculateMaxExperienceFromExperience(experience);

                double percentage = Math.min(1.0, (double) progress / maxExperience);
                int filledSquares = (int) Math.round(percentage * 10);
                StringBuilder progressBar = new StringBuilder(" §8[");
                for (int i = 0; i < 10; i++) {
                    if (i < filledSquares) {
                        progressBar.append("§b■");
                    } else {
                        progressBar.append("§7■");
                    }
                }
                progressBar.append("§8]");

                if (sidebarCache.containsKey(player.getUuid())) {
                    sidebarCache.get(player.getUuid()).removeViewer(player);
                }

                Sidebar sidebar = new Sidebar(Component.text(getSidebarName(prototypeName)));

                addLine("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName(), sidebar);
                addLine("§7 ", sidebar);
                addLine("§fLevel: §7" + LevelColor.constructLevelString(LevelUtil.calculateLevel(experience)), sidebar);
                addLine("§7 ", sidebar);
                addLine("§fProgress: §b" + suffix(progress) + "§7/" + suffix(maxExperience), sidebar);
                addLine(progressBar.toString(), sidebar);
                addLine("§7 ", sidebar);
                addLine("§fTokens: §2" + bwDataHandler.get(BedWarsDataHandler.Data.TOKENS, DatapointLong.class).getValue(), sidebar);
                addLine("§fTickets: §b350§7/75", sidebar);
                addLine("§7 ", sidebar);
                addLine("§fTotal Kills: §a0", sidebar);
                addLine("§fTotal Wins: §a0", sidebar);
                addLine("§7 ", sidebar);

                addLine("§ewww.hypixel.net", sidebar);

                sidebar.addViewer(player);

                sidebarCache.put(player.getUuid(), sidebar);
            }
            return TaskSchedule.tick(2);
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

    private static String suffix(double value) {
        if (value < 1000) {
            return FORMAT.format(value);
        } else {
            return FORMAT.format(value / 1000) + "k";
        }
    }
}
