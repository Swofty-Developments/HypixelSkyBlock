package net.swofty.type.murdermysterylobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardMode;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardPeriod;
import net.swofty.commons.murdermystery.MurderMysteryModeStats;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.DecimalFormat;

public class GUIMurderMysteryStatistics extends HypixelInventoryGUI {
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");

    public GUIMurderMysteryStatistics() {
        super("Murder Mystery Statistics", InventoryType.CHEST_5_ROW);
    }

    private static String formatNumber(long value) {
        return NUMBER_FORMAT.format(value);
    }

    private static String formatTime(long millis) {
        if (millis <= 0) return "00:00";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        if (minutes >= 60) {
            long hours = minutes / 60;
            minutes = minutes % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(player);

        MurderMysteryModeStats stats;
        if (handler != null) {
            stats = handler.get(MurderMysteryDataHandler.Data.MODE_STATS, DatapointMurderMysteryModeStats.class).getValue();
        } else {
            stats = MurderMysteryModeStats.empty();
        }

        // Total Statistics
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                long lifetimeBowKills = stats.getTotalBowKills(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyBowKills = stats.getTotalBowKills(MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeKnifeKills = stats.getTotalKnifeKills(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyKnifeKills = stats.getTotalKnifeKills(MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeThrownKnifeKills = stats.getTotalThrownKnifeKills(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyThrownKnifeKills = stats.getTotalThrownKnifeKills(MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeTrapKills = stats.getTotalTrapKills(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyTrapKills = stats.getTotalTrapKills(MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeKills = stats.getTotalKills(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyKills = stats.getTotalKills(MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeGames = stats.getTotalGamesPlayed(MurderMysteryLeaderboardPeriod.LIFETIME);
                long lifetimeDetectiveWins = stats.getTotalDetectiveWins(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyDetectiveWins = stats.getTotalDetectiveWins(MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeMurdererWins = stats.getTotalMurdererWins(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyMurdererWins = stats.getTotalMurdererWins(MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeKillsAsHero = stats.getTotalKillsAsHero(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyKillsAsHero = stats.getTotalKillsAsHero(MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeWins = stats.getTotalWins(MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyWins = stats.getTotalWins(MurderMysteryLeaderboardPeriod.WEEKLY);

                // Get best quickest wins across all modes
                long quickestDetective = Long.MAX_VALUE;
                long quickestMurderer = Long.MAX_VALUE;
                for (MurderMysteryLeaderboardMode mode : MurderMysteryLeaderboardMode.values()) {
                    long det = stats.getQuickestDetectiveWin(mode);
                    long mur = stats.getQuickestMurdererWin(mode);
                    if (det > 0 && det < quickestDetective) quickestDetective = det;
                    if (mur > 0 && mur < quickestMurderer) quickestMurderer = mur;
                }

                return ItemStackCreator.getStack(
                        "§aTotal Statistics",
                        Material.PAPER,
                        1,
                        "§7Bow Kills: §a" + formatNumber(lifetimeBowKills) + " §8(Weekly: " + formatNumber(weeklyBowKills) + ")",
                        "§7Knife Kills: §a" + formatNumber(lifetimeKnifeKills) + " §8(Weekly: " + formatNumber(weeklyKnifeKills) + ")",
                        "§7Thrown Knife Kills: §a" + formatNumber(lifetimeThrownKnifeKills) + " §8(Weekly: " + formatNumber(weeklyThrownKnifeKills) + ")",
                        "§7Trap Kills: §a" + formatNumber(lifetimeTrapKills) + " §8(Weekly: " + formatNumber(weeklyTrapKills) + ")",
                        "§7Lifetime Kills: §a" + formatNumber(lifetimeKills),
                        "§7Weekly Kills: §a" + formatNumber(weeklyKills),
                        "",
                        "§7Games: §a" + formatNumber(lifetimeGames),
                        "§7Detective Wins: §a" + formatNumber(lifetimeDetectiveWins) + " §8(Weekly: " + formatNumber(weeklyDetectiveWins) + ")",
                        "§7Murderer Wins: §a" + formatNumber(lifetimeMurdererWins) + " §8(Weekly: " + formatNumber(weeklyMurdererWins) + ")",
                        "§7Kills as Hero: §a" + formatNumber(lifetimeKillsAsHero) + " §8(Weekly: " + formatNumber(weeklyKillsAsHero) + ")",
                        "§7Lifetime Wins: §a" + formatNumber(lifetimeWins),
                        "§7Weekly Wins: §a" + formatNumber(weeklyWins),
                        "",
                        "§7Quickest Detective Win Time: §a" + (quickestDetective == Long.MAX_VALUE ? "N/A" : formatTime(quickestDetective)),
                        "§7Quickest Murderer Win Time: §a" + (quickestMurderer == Long.MAX_VALUE ? "N/A" : formatTime(quickestMurderer))
                );
            }
        });

        // Classic Mode Statistics
        set(createModeStatsItem(19, "§aClassic Mode Statistics", MurderMysteryLeaderboardMode.CLASSIC, stats, true));

        // Double Up! Mode Statistics
        set(createModeStatsItem(21, "§aDouble Up! Mode Statistics", MurderMysteryLeaderboardMode.DOUBLE_UP, stats, true));

        // Assassins Mode Statistics
        set(createModeStatsItem(23, "§aAssassins Mode Statistics", MurderMysteryLeaderboardMode.ASSASSINS, stats, false));

        // Infection Mode Statistics
        set(new GUIItem(25) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                MurderMysteryLeaderboardMode mode = MurderMysteryLeaderboardMode.INFECTION;
                long lifetimeSurvivorWins = stats.getSurvivorWins(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklySurvivorWins = stats.getSurvivorWins(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeAlphaWins = stats.getAlphaWins(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyAlphaWins = stats.getAlphaWins(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeKillsAsInfected = stats.getKillsAsInfected(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyKillsAsInfected = stats.getKillsAsInfected(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeKillsAsSurvivor = stats.getKillsAsSurvivor(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyKillsAsSurvivor = stats.getKillsAsSurvivor(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeTimeSurvived = stats.getTimeSurvived(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyTimeSurvived = stats.getTimeSurvived(mode, MurderMysteryLeaderboardPeriod.WEEKLY);

                return ItemStackCreator.getStack(
                        "§aInfection Mode Statistics",
                        Material.PAPER,
                        1,
                        "§7Survivor Wins: §a" + formatNumber(lifetimeSurvivorWins) + " §8(Weekly: " + formatNumber(weeklySurvivorWins) + ")",
                        "§7Alpha Wins: §a" + formatNumber(lifetimeAlphaWins) + " §8(Weekly: " + formatNumber(weeklyAlphaWins) + ")",
                        "",
                        "§7Lifetime Kills as Infected: §a" + formatNumber(lifetimeKillsAsInfected),
                        "§7Weekly Kills as Infected: §a" + formatNumber(weeklyKillsAsInfected),
                        "",
                        "§7Lifetime Kills as Survivor: §a" + formatNumber(lifetimeKillsAsSurvivor),
                        "§7Weekly Kills as Survivor: §a" + formatNumber(weeklyKillsAsSurvivor),
                        "",
                        "§7Total Time Survived: §a" + formatTime(lifetimeTimeSurvived),
                        "§7Weekly Time Survived: §a" + formatTime(weeklyTimeSurvived)
                );
            }
        });

        set(GUIClickableItem.getCloseItem(40));
        updateItemStacks(getInventory(), getPlayer());
    }

    private GUIItem createModeStatsItem(int slot, String title, MurderMysteryLeaderboardMode mode, MurderMysteryModeStats stats, boolean includeRoleStats) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                long lifetimeBowKills = stats.getBowKills(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyBowKills = stats.getBowKills(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeKnifeKills = stats.getKnifeKills(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyKnifeKills = stats.getKnifeKills(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeThrownKnifeKills = stats.getThrownKnifeKills(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyThrownKnifeKills = stats.getThrownKnifeKills(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeTrapKills = stats.getTrapKills(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyTrapKills = stats.getTrapKills(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeKills = stats.getKills(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyKills = stats.getKills(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                long lifetimeGames = stats.getGamesPlayed(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long lifetimeWins = stats.getWins(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                long weeklyWins = stats.getWins(mode, MurderMysteryLeaderboardPeriod.WEEKLY);

                if (includeRoleStats) {
                    long lifetimeDetectiveWins = stats.getDetectiveWins(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                    long weeklyDetectiveWins = stats.getDetectiveWins(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                    long lifetimeMurdererWins = stats.getMurdererWins(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                    long weeklyMurdererWins = stats.getMurdererWins(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                    long lifetimeKillsAsHero = stats.getKillsAsHero(mode, MurderMysteryLeaderboardPeriod.LIFETIME);
                    long weeklyKillsAsHero = stats.getKillsAsHero(mode, MurderMysteryLeaderboardPeriod.WEEKLY);
                    long quickestDetective = stats.getQuickestDetectiveWin(mode);
                    long quickestMurderer = stats.getQuickestMurdererWin(mode);

                    return ItemStackCreator.getStack(
                            title,
                            Material.PAPER,
                            1,
                            "§7Bow Kills: §a" + formatNumber(lifetimeBowKills) + " §8(Weekly: " + formatNumber(weeklyBowKills) + ")",
                            "§7Knife Kills: §a" + formatNumber(lifetimeKnifeKills) + " §8(Weekly: " + formatNumber(weeklyKnifeKills) + ")",
                            "§7Thrown Knife Kills: §a" + formatNumber(lifetimeThrownKnifeKills) + " §8(Weekly: " + formatNumber(weeklyThrownKnifeKills) + ")",
                            "§7Trap Kills: §a" + formatNumber(lifetimeTrapKills) + " §8(Weekly: " + formatNumber(weeklyTrapKills) + ")",
                            "§7Lifetime Kills: §a" + formatNumber(lifetimeKills),
                            "§7Weekly Kills: §a" + formatNumber(weeklyKills),
                            "",
                            "§7Games: §a" + formatNumber(lifetimeGames),
                            "§7Detective Wins: §a" + formatNumber(lifetimeDetectiveWins) + " §8(Weekly: " + formatNumber(weeklyDetectiveWins) + ")",
                            "§7Murderer Wins: §a" + formatNumber(lifetimeMurdererWins) + " §8(Weekly: " + formatNumber(weeklyMurdererWins) + ")",
                            "§7Kills as Hero: §a" + formatNumber(lifetimeKillsAsHero) + " §8(Weekly: " + formatNumber(weeklyKillsAsHero) + ")",
                            "§7Lifetime Wins: §a" + formatNumber(lifetimeWins),
                            "§7Weekly Wins: §a" + formatNumber(weeklyWins),
                            "",
                            "§7Quickest Detective Win Time: §a" + (quickestDetective <= 0 ? "N/A" : formatTime(quickestDetective)),
                            "§7Quickest Murderer Win Time: §a" + (quickestMurderer <= 0 ? "N/A" : formatTime(quickestMurderer))
                    );
                } else {
                    // Assassins mode - no role-specific stats
                    return ItemStackCreator.getStack(
                            title,
                            Material.PAPER,
                            1,
                            "§7Bow Kills: §a" + formatNumber(lifetimeBowKills) + " §8(Weekly: " + formatNumber(weeklyBowKills) + ")",
                            "§7Knife Kills: §a" + formatNumber(lifetimeKnifeKills) + " §8(Weekly: " + formatNumber(weeklyKnifeKills) + ")",
                            "§7Thrown Knife Kills: §a" + formatNumber(lifetimeThrownKnifeKills) + " §8(Weekly: " + formatNumber(weeklyThrownKnifeKills) + ")",
                            "§7Trap Kills: §a" + formatNumber(lifetimeTrapKills) + " §8(Weekly: " + formatNumber(weeklyTrapKills) + ")",
                            "§7Lifetime Kills: §a" + formatNumber(lifetimeKills),
                            "§7Weekly Kills: §a" + formatNumber(weeklyKills),
                            "",
                            "§7Lifetime Wins: §a" + formatNumber(lifetimeWins),
                            "§7Weekly Wins: §a" + formatNumber(weeklyWins)
                    );
                }
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
