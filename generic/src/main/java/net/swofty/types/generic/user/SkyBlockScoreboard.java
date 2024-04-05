package net.swofty.types.generic.user;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.scoreboard.SwoftySidebar;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.utility.StringUtility;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockProgressMission;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyBlockScoreboard {
    private static final Map<UUID, SwoftySidebar> sidebarCache = new HashMap<>();
    private static Integer skyblockName = 0;

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        // Scoreboard Updater
        scheduler.submitTask(() -> {
            skyblockName++;
            if (skyblockName > 50) {
                skyblockName = 0;
            }

            for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
                DataHandler dataHandler = player.getDataHandler();
                SkyBlockRegion region = player.getRegion();
                MissionData missionData = player.getMissionData();
                StatisticDisplayReplacement coinReplacement = player.getCoinsDisplayReplacement();

                if (dataHandler == null) {
                    continue;
                }

                if (sidebarCache.containsKey(player.getUuid())) {
                    sidebarCache.get(player.getUuid()).removeViewer(player);
                }

                SwoftySidebar sidebar = new SwoftySidebar(Component.text(STR."  \{getSidebarName(skyblockName, false)}\{player.isCoop() ? " §b§lCO-OP  " : "  "}"));

                addLine(STR."§7\{new SimpleDateFormat("MM/dd/yy").format(new Date())} §8m\{SkyBlockConst.getServerName()}   ", sidebar , player);
                addLine("§7 ", sidebar , player);
                addLine(STR."§f \{SkyBlockCalendar.getMonthName()} \{StringUtility.ntify(SkyBlockCalendar.getDay())}", sidebar , player);
                addLine(STR."§7 \{SkyBlockCalendar.getDisplay(SkyBlockCalendar.getElapsed())}", sidebar , player);
                try {
                    addLine(STR."§7 ⏣ \{region.getType().getColor()}\{region.getType().getName()}", sidebar , player);
                } catch (NullPointerException ignored) {
                    addLine(" §7Unknown", sidebar , player);
                }
                addLine("§7 ", sidebar , player);
                addLine(STR."§fPurse: §6\{StringUtility.commaify(dataHandler.get(DataHandler.Data.COINS, DatapointDouble.class).getValue())}\{coinReplacement == null ? "" : STR." §e(+\{coinReplacement.getDisplay()})"}", sidebar , player);
                addLine("§7 ", sidebar , player);
                if (region != null &&
                        !missionData.getActiveMissions(region.getType()).isEmpty()) {
                    MissionData.ActiveMission mission = missionData.getActiveMissions(region.getType()).getFirst();

                    addLine("§fObjective", sidebar , player);
                    addLine(STR."§e\{mission}", sidebar , player);

                    SkyBlockProgressMission progressMission = missionData.getAsProgressMission(mission.getMissionID());
                    if (progressMission != null)
                        addLine(STR."§7 (§e\{mission.getMissionProgress()}§7/§a\{progressMission.getMaxProgress()}§7)", sidebar , player);
                    addLine("§7 ", sidebar , player);
                }

                addLine("§ewww.hypixel.net", sidebar , player);

                sidebar.addViewer(player);

                sidebarCache.put(player.getUuid(), sidebar);
            }
            return TaskSchedule.tick(2);
        });
    }

    public static void removeCache(Player player) {
        sidebarCache.remove(player.getUuid());
    }

    private static void addLine(String text, SwoftySidebar sidebar , Player player) {
        for (SwoftySidebar.ScoreboardLine existingLine : sidebar.getLines()) {
            sidebar.updateLineScore(existingLine.getId(), existingLine.getLine() + 1);
        }

        sidebar.createLine(new SwoftySidebar.ScoreboardLine(UUID.randomUUID().toString(), Component.text(text), 0) , player );
    }

    private static String getSidebarName(int counter, boolean isGuest) {
        String baseText = "SKYBLOCK";
        String[] colors = {"§f§l", "§6§l", "§e§l"};
        String endColor = "§a§l";
        String endText = isGuest ? " GUEST" : "";

        if (counter > 0 && counter <= 8) {
            return colors[0] + baseText.substring(0, counter - 1) +
                    colors[1] + baseText.charAt(counter - 1) +
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
