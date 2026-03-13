package net.swofty.type.skyblockgeneric.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointGardenCore;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointGardenPersonal;
import net.swofty.type.skyblockgeneric.garden.GardenData;
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
import java.util.Objects;

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

                List<Component> lines = new ArrayList<>();
                lines.add(Component.empty()
                    .append(Component.text(new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()), NamedTextColor.GRAY))
                    .append(Component.text(" " + HypixelConst.getServerName(), NamedTextColor.DARK_GRAY)));
                lines.add(Component.text(" ", NamedTextColor.GRAY));
                lines.add(Component.empty()
                    .append(Component.text(" ", NamedTextColor.WHITE))
                    .append(Component.text(SkyBlockCalendar.getMonthName() + " " + StringUtility.ntify(SkyBlockCalendar.getDay()), NamedTextColor.WHITE)));
                lines.add(Component.empty()
                    .append(Component.text(" ", NamedTextColor.GRAY))
                    .append(Component.text(SkyBlockCalendar.getDisplay(SkyBlockCalendar.getElapsed()), NamedTextColor.GRAY)));
                try {
                    RegionType type = Objects.requireNonNull(region).getType();
                    String name = type.getColor() + type.getName();
                    if (type == RegionType.PLAYER_MUSEUM) {
                        name = name.formatted(player.getUsername());
                    }
                    lines.add(Component.empty()
                        .append(Component.text(" ⏣ ", NamedTextColor.GRAY))
                        .append(Component.text(name)));
                } catch (NullPointerException ignored) {
                    lines.add(Component.empty()
                        .append(Component.text(" ", NamedTextColor.WHITE))
                        .append(I18n.t("scoreboard.skyblock.region_unknown")));
                }
                lines.add(Component.text(" ", NamedTextColor.GRAY));

                lines.add(I18n.t("scoreboard.skyblock.purse", Component.text(StringUtility.commaify(dataHandler.get(SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue()))));
                if (HypixelConst.isGarden()) {
                    GardenData.GardenCoreData core = dataHandler.get(SkyBlockDataHandler.Data.GARDEN_CORE, DatapointGardenCore.class).getValue();
                    GardenData.GardenPersonalData personal = dataHandler.get(SkyBlockDataHandler.Data.GARDEN_PERSONAL, DatapointGardenPersonal.class).getValue();
                    lines.add(Component.text("Copper: ", NamedTextColor.WHITE)
                        .append(Component.text(StringUtility.commaify(core.getCopper()), NamedTextColor.RED)));
                    lines.add(Component.text("Sawdust: ", NamedTextColor.WHITE)
                        .append(Component.text(StringUtility.commaify(personal.getSowdust()), NamedTextColor.DARK_GREEN)));
                } else {
                    lines.add(I18n.t("scoreboard.skyblock.bits", Component.text(StringUtility.commaify(dataHandler.get(SkyBlockDataHandler.Data.BITS, DatapointInteger.class).getValue()))));

                    if (DarkAuctionHandler.isPlayerInAuction(player.getUuid())
                        && DarkAuctionHandler.getLocalState() != null
                        && DarkAuctionHandler.getLocalState().getPhase() == DarkAuctionPhase.BIDDING
                    ) {
                        lines.add(Component.text(" ", NamedTextColor.DARK_GRAY));
                        DarkAuctionHandler.DarkAuctionLocalState auctionState = DarkAuctionHandler.getLocalState();
                        int timeRemaining = DarkAuctionHandler.getTimeLeft().get();

                        lines.add(Component.empty()
                            .append(I18n.t("scoreboard.skyblock.dark_auction.time_left_label"))
                            .append(Component.text(String.valueOf(timeRemaining)))
                            .append(I18n.t("scoreboard.skyblock.dark_auction.time_left_suffix")));
                        lines.add(I18n.t("scoreboard.skyblock.dark_auction.current_item_label"));

                        String currentItem = auctionState.getCurrentItemType();
                        if (currentItem != null) {
                            try {
                                ItemType itemType = ItemType.valueOf(currentItem);
                                SkyBlockItem item = new SkyBlockItem(itemType);
                                lines.add(Component.empty()
                                    .append(Component.text(" ", NamedTextColor.WHITE))
                                    .append(Component.text(item.getDisplayName())));
                            } catch (Exception e) {
                                lines.add(Component.empty()
                                    .append(Component.text(" ", NamedTextColor.WHITE))
                                    .append(Component.text(currentItem.replace("_", " "), NamedTextColor.WHITE)));
                            }
                        } else {
                            lines.add(Component.empty()
                                .append(Component.text(" ", NamedTextColor.WHITE))
                                .append(I18n.t("scoreboard.skyblock.dark_auction.waiting")));
                        }
                    } else {
                        if (region != null &&
                            !missionData.getActiveMissions(region.getType()).isEmpty()) {
                            lines.add(Component.text(" ", NamedTextColor.GRAY));
                            MissionData.ActiveMission mission = missionData.getActiveMissions(region.getType()).getFirst();
                            SkyBlockMission skyBlockMission = MissionData.getMissionClass(mission.getMissionID());

                            if (skyBlockMission instanceof LocationAssociatedMission locationAssociatedMission) {
                                lines.add(Component.empty()
                                    .append(I18n.t("scoreboard.skyblock.objective_label"))
                                    .append(Component.text(" " + BlockUtility.getArrow(
                                        player.getPosition(),
                                        locationAssociatedMission.getLocation()
                                    ))));
                            } else {
                                lines.add(I18n.t("scoreboard.skyblock.objective_label"));
                            }
                            lines.add(Component.text(String.valueOf(mission), NamedTextColor.YELLOW));

                            SkyBlockProgressMission progressMission = missionData.getAsProgressMission(mission.getMissionID());
                            if (progressMission != null)
                                lines.add(Component.empty()
                                    .append(Component.text(" (", NamedTextColor.GRAY))
                                    .append(Component.text(String.valueOf(mission.getMissionProgress()), NamedTextColor.YELLOW))
                                    .append(Component.text("/", NamedTextColor.GRAY))
                                    .append(Component.text(String.valueOf(progressMission.getMaxProgress()), NamedTextColor.GREEN))
                                    .append(Component.text(")", NamedTextColor.GRAY)));
                        }
                    }
                }

                lines.add(Component.text(" ", NamedTextColor.GRAY));
                lines.add(I18n.t("scoreboard.common.footer"));

                Component title = Component.empty()
                    .append(Component.text("  "))
                    .append(HypixelScoreboard.getSidebarName(I18n.string("scoreboard.skyblock.title_base"), skyblockName, false))
                    .append(Component.text("  "));

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

}
