package net.swofty.user;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.Utility;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointDouble;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.calendar.SkyBlockCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyBlockScoreboard {
    private static Map<UUID, Sidebar> sidebarCache = new HashMap<>();
    private static Integer skyblockName = 0;

    public static char _DaySymbol = '☀';
    public static char _NightSymbol = '☽';
    public static char _LocationSymbol = '⏣';

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        // Calendar Updater
        scheduler.submitTask(() -> {
            SkyBlockCalendar.ELAPSED += 10L;
            SkyBlockCalendar.checkForEvents(SkyBlockCalendar.ELAPSED % SkyBlockCalendar.YEAR);
           return TaskSchedule.tick(10);
        });

        // Scoreboard Updater
        scheduler.submitTask(() -> {
            skyblockName++;
            if (skyblockName > 50) {
                skyblockName = 0;
            }

            boolean day = true;
            int time = (int) ((SkyBlockCalendar.ELAPSED % 24000) - 6000);
            if (time < 0)
                time += 24000;
            int hours = 6 + (time / 1000);
            int minutes = (int) ((time % ((hours - 6) * 1000.0)) / 16.66666);
            String sMin = String.valueOf(minutes);
            minutes = minutes - Integer.parseInt(sMin.substring(sMin.length() - 1));
            if (hours >= 24) hours -= 24;
            if (hours <= 6 || hours >= 20) day = false;

            for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                DataHandler dataHandler = skyBlockPlayer.getDataHandler();
                SkyBlockRegion region = SkyBlockRegion.getRegionOfEntity(player);

                if (dataHandler == null) {
                    continue;
                }

                if (sidebarCache.containsKey(player.getUuid())) {
                    sidebarCache.get(player.getUuid()).removeViewer(player);
                }

                Sidebar sidebar = new Sidebar(getSidebarName(skyblockName, false));

                addLine("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8???", sidebar);
                addLine("§7 ", sidebar);
                addLine("§f" + SkyBlockCalendar.getMonthName() + " " + Utility.ntify(SkyBlockCalendar.getDay()), sidebar);
                addLine("§7" + (hours > 12 ? hours - 12 : (hours == 0 ? 12 : hours)) + ":" + Utility.zeroed(minutes) +
                (hours >= 12 ? "pm" : "am") + " " + (day ? "§e" + _DaySymbol : "§b" + _NightSymbol), sidebar);
                try {
                    addLine("§7 " + _LocationSymbol + " " + region.getType().getColor() + region.getType().getName(), sidebar);
                } catch (NullPointerException ignored) {
                    addLine(" §7Unknown", sidebar);
                }
                addLine("§7 ", sidebar);
                addLine("§fPurse: §6" + dataHandler.get(DataHandler.Data.COINS, DatapointDouble.class).getValue(), sidebar);
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
    private static String getSidebarName(int counter, boolean isGuest) {
        String baseText = "SKYBLOCK";
        String[] colors = {"§f§l", "§6§l", "§e§l"};
        String endColor = "§a§l";
        String endText = isGuest ? " GUEST" : "";

        if (counter > 0 && counter <= 8) {
            return colors[0] + baseText.substring(0, counter - 1) +
                    colors[1] + baseText.substring(counter - 1, counter) +
                    colors[2] + baseText.substring(counter) +
                    endColor + endText;
        } else if ((counter >= 9 && counter <= 19) ||
                (counter >= 25 && counter <= 29)) {
            return colors[0] + baseText + endColor + endText;
        } else {
            return colors[2] + baseText + endColor + endText;
        }
    }
}
