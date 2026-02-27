package net.swofty.type.skyblockgeneric.user;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.data.datapoints.DatapointInteger;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.utility.BlockUtility;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.darkauction.DarkAuctionHandler;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.mission.LocationAssociatedMission;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.SkyBlockMission;
import net.swofty.type.skyblockgeneric.mission.SkyBlockProgressMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SkyBlockScoreboard {
    private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
    private static Integer skyblockName = 0;

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        scheduler.submitTask(() -> {
            skyblockName++;
            if (skyblockName > 50) {
                skyblockName = 0;
            }

            for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
                SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();
                SkyBlockRegion region = player.getRegion();
                MissionData missionData = player.getMissionData();

                if (dataHandler == null) {
                    continue;
                }

                List<String> lines = new ArrayList<>();
                lines.add("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()) + " §8" + HypixelConst.getServerName());
                lines.add("§7 ");
                lines.add("§f " + SkyBlockCalendar.getMonthName() + " " + StringUtility.ntify(SkyBlockCalendar.getDay()));
                lines.add("§7 " + SkyBlockCalendar.getDisplay(SkyBlockCalendar.getElapsed()));
                try {
                    RegionType type = region.getType();
                    String name = type.getName();
                    if (type == RegionType.PLAYER_MUSEUM) {
                        name = name.formatted(player.getUsername());
                    }
                    lines.add("§7 ⏣ " + region.getType().getColor() + name);
                } catch (NullPointerException ignored) {
                    lines.add(" " + I18n.string("scoreboard.skyblock.region_unknown"));
                }
                lines.add("§7 ");
                lines.add(I18n.string("scoreboard.skyblock.purse_label") + StringUtility.commaify(dataHandler.get(SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue()));
                lines.add(I18n.string("scoreboard.skyblock.bits_label") + StringUtility.commaify(dataHandler.get(SkyBlockDataHandler.Data.BITS, DatapointInteger.class).getValue()));

                if (DarkAuctionHandler.isPlayerInAuction(player.getUuid())
                        && DarkAuctionHandler.getLocalState() != null
                        && DarkAuctionHandler.getLocalState().getPhase() == DarkAuctionPhase.BIDDING
                ) {
                    lines.add("§8 ");
                    DarkAuctionHandler.DarkAuctionLocalState auctionState = DarkAuctionHandler.getLocalState();
                    int timeRemaining = DarkAuctionHandler.getTimeLeft().get();

                    lines.add(I18n.string("scoreboard.skyblock.dark_auction.time_left_label") + timeRemaining + I18n.string("scoreboard.skyblock.dark_auction.time_left_suffix"));
                    lines.add(I18n.string("scoreboard.skyblock.dark_auction.current_item_label"));

                    String currentItem = auctionState.getCurrentItemType();
                    if (currentItem != null) {
                        try {
                            ItemType itemType = ItemType.valueOf(currentItem);
                            SkyBlockItem item = new SkyBlockItem(itemType);
                            lines.add(" " + item.getDisplayName());
                        } catch (Exception e) {
                            lines.add(" §f" + currentItem.replace("_", " "));
                        }
                    } else {
                        lines.add(" " + I18n.string("scoreboard.skyblock.dark_auction.waiting"));
                    }
                } else {
                    if (region != null &&
                            !missionData.getActiveMissions(region.getType()).isEmpty()) {
                        lines.add("§7 ");
                        MissionData.ActiveMission mission = missionData.getActiveMissions(region.getType()).getFirst();
                        SkyBlockMission skyBlockMission = MissionData.getMissionClass(mission.getMissionID());

                        if (skyBlockMission instanceof LocationAssociatedMission locationAssociatedMission) {
                            lines.add(I18n.string("scoreboard.skyblock.objective_label") + " " + BlockUtility.getArrow(
                                    player.getPosition(),
                                    locationAssociatedMission.getLocation()
                            ));
                            lines.add("§e" + mission);
                        } else {
                            lines.add(I18n.string("scoreboard.skyblock.objective_label"));
                            lines.add("§e" + mission);
                        }

                        SkyBlockProgressMission progressMission = missionData.getAsProgressMission(mission.getMissionID());
                        if (progressMission != null)
                            lines.add("§7 (§e" + mission.getMissionProgress() + "§7/§a" + progressMission.getMaxProgress() + "§7)");
                    }
                }

                lines.add("§7 ");
                lines.add(I18n.string("scoreboard.common.footer"));

                String title = "  " + getSidebarName(skyblockName, false)
                        + (player.isCoop() ? " " + I18n.string("scoreboard.skyblock.coop_suffix") + "  " : "  ");

                if (!scoreboard.hasScoreboard(player)) {
                    scoreboard.createScoreboard(player, title);
                }

                scoreboard.updateLines(player, lines);
                scoreboard.updateTitle(player, title);
            }
            return TaskSchedule.tick(4);
        });
    }

    public static void removeCache(Player player) {
        scoreboard.removeScoreboard(player);
    }

    private static String getSidebarName(int counter, boolean isGuest) {
        String baseText = I18n.string("scoreboard.skyblock.title_base");
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
