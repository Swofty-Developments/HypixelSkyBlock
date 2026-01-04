package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skywars.SkywarsLeaderboardMode;
import net.swofty.commons.skywars.SkywarsLeaderboardPeriod;
import net.swofty.commons.skywars.SkywarsModeStats;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsModeStats;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.DecimalFormat;

public class GUISkywarsStatistics extends HypixelInventoryGUI {
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");

    public GUISkywarsStatistics() {
        super("SkyWars Statistics", InventoryType.CHEST_6_ROW);
    }

    private static String fmt(long value) {
        return NUMBER_FORMAT.format(value);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);

        SkywarsModeStats stats;
        if (handler != null) {
            stats = handler.get(SkywarsDataHandler.Data.MODE_STATS, DatapointSkywarsModeStats.class).getValue();
        } else {
            stats = SkywarsModeStats.empty();
        }

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                long wins = stats.getTotalWins(SkywarsLeaderboardPeriod.LIFETIME);
                long losses = stats.getTotalLosses(SkywarsLeaderboardPeriod.LIFETIME);
                long kills = stats.getTotalKills(SkywarsLeaderboardPeriod.LIFETIME);
                long assists = stats.getTotalAssists(SkywarsLeaderboardPeriod.LIFETIME);
                long deaths = stats.getTotalDeaths(SkywarsLeaderboardPeriod.LIFETIME);
                long meleeKills = stats.getTotalMeleeKills(SkywarsLeaderboardPeriod.LIFETIME);
                long bowKills = stats.getTotalBowKills(SkywarsLeaderboardPeriod.LIFETIME);
                long voidKills = stats.getTotalVoidKills(SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsShot = stats.getTotalArrowsShot(SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsHit = stats.getTotalArrowsHit(SkywarsLeaderboardPeriod.LIFETIME);
                long chests = stats.getTotalChestsOpened(SkywarsLeaderboardPeriod.LIFETIME);
                long souls = stats.getTotalSoulsGathered(SkywarsLeaderboardPeriod.LIFETIME);
                long heads = stats.getTotalHeads(SkywarsLeaderboardPeriod.LIFETIME);

                return ItemStackCreator.getStack(
                        "§aAll Mode Statistics",
                        Material.PAPER,
                        1,
                        "§7Wins: §a" + fmt(wins),
                        "§7Losses: §a" + fmt(losses),
                        "",
                        "§7Kills: §a" + fmt(kills),
                        "§7Assists: §a" + fmt(assists),
                        "§7Deaths: §a" + fmt(deaths),
                        "",
                        "§7Melee Kills: §a" + fmt(meleeKills),
                        "§7Bow Kills: §a" + fmt(bowKills),
                        "§7Void Kills: §a" + fmt(voidKills),
                        "",
                        "§7Arrows Shot: §a" + fmt(arrowsShot),
                        "§7Arrows Hit: §a" + fmt(arrowsHit),
                        "",
                        "§7Chests Opened: §a" + fmt(chests),
                        "§7Enderpearls Thrown: §a0",
                        "§7Souls Gathered: §a" + fmt(souls),
                        "§7Heads: §a" + fmt(heads)
                );
            }
        });

        set(new GUIItem(18) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMini Statistics",
                        Material.PAPER,
                        1,
                        "§7Wins: §a0",
                        "",
                        "§7Kills: §a0",
                        "§7Assists: §a0",
                        "",
                        "§7Melee Kills: §a0",
                        "§7Bow Kills: §a0",
                        "§7Void Kills: §a0",
                        "",
                        "§7Arrows Shot: §a0",
                        "§7Arrows Hit: §a0",
                        "",
                        "§7Chests Opened: §a0"
                );
            }
        });

        set(new GUIItem(20) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                long wins = stats.getWins(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getWins(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long losses = stats.getLosses(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getLosses(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long kills = stats.getKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long assists = stats.getAssists(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getAssists(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long deaths = stats.getDeaths(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getDeaths(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long meleeKills = stats.getMeleeKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getMeleeKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long bowKills = stats.getBowKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getBowKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long voidKills = stats.getVoidKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getVoidKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsShot = stats.getArrowsShot(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getArrowsShot(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsHit = stats.getArrowsHit(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getArrowsHit(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long chests = stats.getChestsOpened(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getChestsOpened(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long heads = stats.getHeads(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getHeads(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);

                return ItemStackCreator.getStack(
                        "§aSolo Statistics",
                        Material.PAPER,
                        1,
                        "§7Wins: §a" + fmt(wins),
                        "§7Losses: §a" + fmt(losses),
                        "",
                        "§7Kills: §a" + fmt(kills),
                        "§7Assists: §a" + fmt(assists),
                        "§7Deaths: §a" + fmt(deaths),
                        "",
                        "§7Melee Kills: §a" + fmt(meleeKills),
                        "§7Bow Kills: §a" + fmt(bowKills),
                        "§7Void Kills: §a" + fmt(voidKills),
                        "",
                        "§7Arrows Shot: §a" + fmt(arrowsShot),
                        "§7Arrows Hit: §a" + fmt(arrowsHit),
                        "",
                        "§7Chests Opened: §a" + fmt(chests),
                        "",
                        "§7Heads: §a" + fmt(heads)
                );
            }
        });

        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                long wins = stats.getWins(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long losses = stats.getLosses(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long kills = stats.getKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long assists = stats.getAssists(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long deaths = stats.getDeaths(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long meleeKills = stats.getMeleeKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long bowKills = stats.getBowKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long voidKills = stats.getVoidKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsShot = stats.getArrowsShot(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsHit = stats.getArrowsHit(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long chests = stats.getChestsOpened(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long heads = stats.getHeads(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);

                return ItemStackCreator.getStack(
                        "§aDoubles Statistics",
                        Material.PAPER,
                        1,
                        "§7Wins: §a" + fmt(wins),
                        "§7Losses: §a" + fmt(losses),
                        "",
                        "§7Kills: §a" + fmt(kills),
                        "§7Assists: §a" + fmt(assists),
                        "§7Deaths: §a" + fmt(deaths),
                        "",
                        "§7Melee Kills: §a" + fmt(meleeKills),
                        "§7Bow Kills: §a" + fmt(bowKills),
                        "§7Void Kills: §a" + fmt(voidKills),
                        "",
                        "§7Arrows Shot: §a" + fmt(arrowsShot),
                        "§7Arrows Hit: §a" + fmt(arrowsHit),
                        "",
                        "§7Chests Opened: §a" + fmt(chests),
                        "",
                        "§7Heads: §a" + fmt(heads)
                );
            }
        });

        set(new GUIItem(24) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMega Statistics",
                        Material.PAPER,
                        1,
                        "§7Wins: §a0",
                        "§7Losses: §a0",
                        "",
                        "§7Kills: §a0",
                        "§7Assists: §a0",
                        "§7Deaths: §a0",
                        "",
                        "§7Melee Kills: §a0",
                        "§7Bow Kills: §a0",
                        "§7Void Kills: §a0",
                        "",
                        "§7Arrows Shot: §a0",
                        "§7Arrows Hit: §a0",
                        "",
                        "§7Chests Opened: §a0"
                );
            }
        });

        set(new GUIItem(26) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aRanked Statistics",
                        Material.PAPER,
                        1,
                        "§7Wins: §a0",
                        "§7Losses: §a0",
                        "",
                        "§7Kills: §a0",
                        "§7Assists: §a0",
                        "§7Deaths: §a0",
                        "",
                        "§7Melee Kills: §a0",
                        "§7Bow Kills: §a0",
                        "§7Void Kills: §a0",
                        "",
                        "§7Arrows Shot: §a0",
                        "§7Arrows Hit: §a0",
                        "",
                        "§7Chests Opened: §a0"
                );
            }
        });

        set(new GUIItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSkyWars Challenge Statistics",
                        Material.BLAZE_POWDER,
                        1,
                        "§7Total Challenge Wins: §a0",
                        "",
                        "§7Archer Wins: §a0",
                        "§7Half Health Wins: §a0",
                        "§7No Block Wins: §a0",
                        "§7No Chest Wins: §a0",
                        "§7Paper Wins: §a0",
                        "§7Rookie Wins: §a0",
                        "§7UHC Wins: §a0",
                        "§7Ultimate Warrior Wins: §a0",
                        "",
                        "§7Wins with 2 Challenges: §a0",
                        "§7Wins with 3 Challenges: §a0",
                        "§7Wins with 4 Challenges: §a0",
                        "§7Wins with 5 Challenges: §a0",
                        "§7Wins with 6 Challenges: §a0",
                        "§7Wins with 7 Challenges: §a0",
                        "§7Wins with 8 Challenges: §a0"
                );
            }
        });

        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aLab Statistics",
                        Material.BREWING_STAND,
                        1,
                        "§7Hunters vs Beasts Wins: §a0",
                        "§7Lucky Blocks Wins: §a0",
                        "§7TNT Madness Wins: §a0",
                        "§7Slime Wins: §a0",
                        "§7Rush Wins: §a0"
                );
            }
        });

        set(GUIClickableItem.getCloseItem(49));
        updateItemStacks(getInventory(), getPlayer());
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
