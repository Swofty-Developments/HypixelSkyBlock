package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsKitStats;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.kit.SkywarsKit;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI showing per-kit statistics for a player.
 * Matches Hypixel's kit stats layout.
 */
public class GUIKitStats extends HypixelInventoryGUI {
    private final SkywarsKit kit;
    private final String mode;

    public GUIKitStats(SkywarsKit kit, String mode) {
        super(kit.getName() + " Stats", InventoryType.CHEST_6_ROW);
        this.kit = kit;
        this.mode = mode;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        if (handler == null) return;

        long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();

        DatapointSkywarsKitStats.SkywarsKitStats kitStatsData = handler.get(
                SkywarsDataHandler.Data.KIT_STATS,
                DatapointSkywarsKitStats.class
        ).getValue();

        DatapointSkywarsKitStats.KitStatistics stats = kitStatsData.getStatsForKit(kit.getId());

        // Kit icon with starting items (slot 13)
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.addAll(kit.getItemsLore(mode));

                String name = "§a" + kit.getName();
                if (kit.hasCustomTexture()) {
                    return ItemStackCreator.getStackHead(name, kit.getIconTexture(), 1, lore);
                } else {
                    return ItemStackCreator.getStack(name, kit.getIconMaterial(), 1, lore);
                }
            }
        });

        // General Stats (slot 28)
        set(new GUIItem(28) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String timePlayed = stats.getFormattedTimePlayed();
                String fastestWin = stats.getFormattedFastestWin();

                return ItemStackCreator.getStack(
                        "§aGeneral Stats with " + kit.getName(),
                        Material.ITEM_FRAME,
                        1,
                        "§7Time Played: §a" + timePlayed,
                        "",
                        "§7Wins: §a" + stats.getWins(),
                        "§7Fastest Win: §a" + fastestWin,
                        "",
                        "§7Most Kills in a Game: §a" + stats.getMostKillsInGame(),
                        "§7Mobs Killed: §a" + stats.getMobsKilled(),
                        "§7Chests Opened: §a" + stats.getChestsOpened(),
                        "",
                        "§7Heads Gathered: §a" + stats.getHeadsGathered()
                );
            }
        });

        // Kills stats (slot 30)
        set(new GUIItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aKills with " + kit.getName(),
                        Material.REDSTONE,
                        1,
                        "§7Kills: §a" + stats.getKills(),
                        "§7Assists: §a" + stats.getAssists(),
                        "",
                        "§7Melee Kills: §a" + stats.getMeleeKills(),
                        "§7Bow Kills: §a" + stats.getBowKills(),
                        "§7Void Kills: §a" + stats.getVoidKills(),
                        "§7Kills by Mobs: §a" + stats.getMobKills()
                );
            }
        });

        // Archery stats (slot 32)
        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aArchery with " + kit.getName(),
                        Material.BOW,
                        1,
                        "§7Accuracy: §a" + String.format("%.0f", stats.getAccuracy()) + "%",
                        "§7Bow Kills: §a" + stats.getBowKills(),
                        "",
                        "§7Longest Bow Kill: §a" + stats.getLongestBowKill() + " blocks",
                        "§7Longest Bow Shot: §a" + stats.getLongestBowShot() + " blocks"
                );
            }
        });

        // SkyWars Challenges (slot 34)
        set(new GUIItem(34) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSkyWars Challenges with " + kit.getName(),
                        Material.BLAZE_POWDER,
                        1,
                        "§7Archer Wins: §a" + stats.getArcherWins(),
                        "§7Half Health Wins: §a" + stats.getHalfHealthWins(),
                        "§7No Block Wins: §a" + stats.getNoBlockWins(),
                        "§7No Chest Wins: §a" + stats.getNoChestWins(),
                        "§7Paper Wins: §a" + stats.getPaperWins(),
                        "§7Rookie Wins: §a" + stats.getRookieWins(),
                        "§7UHC Wins: §a" + stats.getUhcWins(),
                        "§7Ultimate Warrior Wins: §a" + stats.getUltimateWarriorWins()
                );
            }
        });

        // Go Back (slot 48)
        set(new GUIClickableItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGo Back",
                        Material.ARROW,
                        1,
                        "§7To " + kit.getName() + " Kit"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIKitBreakdown(kit, mode).open(player);
            }
        });

        // Total Coins (slot 49)
        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§7Total Coins: §6" + String.format("%,d", coins),
                        Material.EMERALD,
                        1,
                        "§6https://store.hypixel.net"
                );
            }
        });

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
