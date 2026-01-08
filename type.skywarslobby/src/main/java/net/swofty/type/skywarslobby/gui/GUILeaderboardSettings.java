package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skywars.SkywarsLeaderboardMode;
import net.swofty.commons.skywars.SkywarsLeaderboardPeriod;
import net.swofty.commons.skywars.SkywarsLeaderboardView;
import net.swofty.commons.skywars.SkywarsTextAlignment;
import net.swofty.type.skywarslobby.hologram.LeaderboardHologramManager;
import net.swofty.type.skywarslobby.hologram.LeaderboardHologramManager.PlayerLeaderboardState;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILeaderboardSettings extends HypixelInventoryGUI {
    private SkywarsLeaderboardMode selectedMode;
    private SkywarsLeaderboardPeriod selectedPeriod;
    private SkywarsLeaderboardView selectedView;
    private SkywarsTextAlignment selectedAlignment;

    public GUILeaderboardSettings() {
        super("Leaderboard Settings", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();

        PlayerLeaderboardState currentState = LeaderboardHologramManager.getState(player.getUuid());
        selectedMode = currentState.mode();
        selectedPeriod = currentState.period();
        selectedView = currentState.view();
        selectedAlignment = currentState.textAlignment();

        setupItems(player);
        updateItemStacks(getInventory(), player);
    }

    private void setupItems(HypixelPlayer player) {
        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                for (SkywarsLeaderboardMode mode : SkywarsLeaderboardMode.values()) {
                    if (mode == selectedMode) {
                        lore.add("§a\u279C §7" + mode.getDisplayName());
                    } else {
                        lore.add("   §7" + mode.getDisplayName());
                    }
                }
                lore.add("");
                lore.add("§8§oThis setting will save across lobbies.");
                lore.add("");
                lore.add("§8§oLeaderboard data is cached and");
                lore.add("§8§odoes not update immediately.");
                lore.add("");
                lore.add("§eLeft/Right Click to change!");

                return ItemStackCreator.getStack("§aSelect the Mode!",
                        Material.BOW, 1, lore.toArray(new String[0]));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (e.getClick() instanceof Click.Left) {
                    selectedMode = selectedMode.next();
                } else if (e.getClick() instanceof Click.Right) {
                    selectedMode = selectedMode.previous();
                }
                setupItems(player);
                updateItemStacks(getInventory(), player);
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                for (SkywarsLeaderboardPeriod period : SkywarsLeaderboardPeriod.values()) {
                    if (period == selectedPeriod) {
                        lore.add("§a\u279C §7" + period.getDisplayName());
                    } else {
                        lore.add("   §7" + period.getDisplayName());
                    }
                }
                lore.add("");
                lore.add("§8§oThis setting will save across lobbies.");
                lore.add("");
                lore.add("§8§oLeaderboard data is cached and");
                lore.add("§8§odoes not update immediately.");
                lore.add("");
                lore.add("§eLeft/Right Click to change!");

                return ItemStackCreator.getStack("§aSelect the Time!",
                        Material.CLOCK, 1, lore.toArray(new String[0]));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (e.getClick() instanceof Click.Left) {
                    selectedPeriod = selectedPeriod.next();
                } else if (e.getClick() instanceof Click.Right) {
                    selectedPeriod = selectedPeriod.previous();
                }
                setupItems(player);
                updateItemStacks(getInventory(), player);
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                for (SkywarsLeaderboardView view : SkywarsLeaderboardView.values()) {
                    if (view == selectedView) {
                        lore.add("§a\u279C §7" + view.getDisplayName());
                    } else {
                        lore.add("   §7" + view.getDisplayName());
                    }
                }
                lore.add("");
                lore.add("§8§oLeaderboard data is cached and");
                lore.add("§8§odoes not update immediately.");
                lore.add("");
                lore.add("§eLeft/Right Click to change!");

                return ItemStackCreator.getStack("§aSelect the View!",
                        Material.LADDER, 1, lore.toArray(new String[0]));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (e.getClick() instanceof Click.Left) {
                    selectedView = selectedView.next();
                } else if (e.getClick() instanceof Click.Right) {
                    selectedView = selectedView.previous();
                }
                setupItems(player);
                updateItemStacks(getInventory(), player);
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aSelect the Players!",
                        Material.SKELETON_SKULL, 1,
                        "",
                        "§a\u279C §7All",
                        "   §7Friends",
                        "   §7Best Friends",
                        "   §7Guild Members",
                        "",
                        "§8§oLeaderboard data is cached and",
                        "§8§odoes not update immediately.");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§cThis feature is coming soon!");
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                for (SkywarsTextAlignment alignment : SkywarsTextAlignment.values()) {
                    if (alignment == selectedAlignment) {
                        lore.add("§a\u279C §7" + alignment.getDisplayName());
                    } else {
                        lore.add("   §7" + alignment.getDisplayName());
                    }
                }
                lore.add("");
                lore.add("§cBlock alignment is showing correctly");
                lore.add("§conly for Vanilla Minecraft font sizes.");
                lore.add("");
                lore.add("§8§oThis setting will save across the");
                lore.add("§8§onetwork.");
                lore.add("");
                lore.add("§8§oLeaderboard data is cached and");
                lore.add("§8§odoes not update immediately.");
                lore.add("");
                lore.add("§eLeft/Right Click to change!");

                return ItemStackCreator.getStack("§aSelect the Text Alignment!",
                        Material.ITEM_FRAME, 1, lore.toArray(new String[0]));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (e.getClick() instanceof Click.Left) {
                    selectedAlignment = selectedAlignment.next();
                } else if (e.getClick() instanceof Click.Right) {
                    selectedAlignment = selectedAlignment.previous();
                }
                setupItems(player);
                updateItemStacks(getInventory(), player);
            }
        });

        set(new GUIClickableItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aApply changes",
                        Material.GREEN_TERRACOTTA, 1,
                        "§eClick to apply the changes!");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                PlayerLeaderboardState newState = new PlayerLeaderboardState(
                        selectedPeriod, selectedMode, selectedView, selectedAlignment);

                LeaderboardHologramManager.setState(player.getUuid(), newState);
                LeaderboardHologramManager.refreshAllHologramsForPlayer(player);

                player.sendMessage("§aLeaderboard settings applied!");
                player.closeInventory();
            }
        });

        set(new GUIClickableItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§cDiscard changes",
                        Material.RED_TERRACOTTA, 1,
                        "§eClose the menu without applying",
                        "§echanges!");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();
            }
        });
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
