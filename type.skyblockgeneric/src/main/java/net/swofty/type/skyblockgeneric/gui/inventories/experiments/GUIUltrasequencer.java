package net.swofty.type.skyblockgeneric.gui.inventories.experiments;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.experimentation.ExperimentationManager;

public class GUIUltrasequencer extends HypixelInventoryGUI {

    private final int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44};

    public GUIUltrasequencer() {
        super("Ultrasequencer -> stakes", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, " ");

        for (int i : borderSlots) {
            set(new GUIItem(i) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(" ", Material.PURPLE_STAINED_GLASS_PANE, 1);
                }
            });
        }

        set(new GUIClickableItem(20) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                try {
                    if (ExperimentationManager.startUltrasequencer(player, "HIGH")) {
                        new GUIUltrasequencerPlay("HIGH").open(player);
                    } else {
                        player.sendMessage("§cFailed to start Ultrasequencer");
                    }
                } catch (Exception ex) {
                    player.sendMessage("§cFailed to start Ultrasequencer: " + ex.getMessage());
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§aHigh Experiment",
                        Material.LIME_DYE,
                        1,
                        "§7Ultrasequencer",
                        "",
                        "§7Numbers to remember: §d3-15",
                        "",
                        "§7XP Reward: §b200 Enchanting Exp",
                        "§7per §enumber §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e6 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "§7Chain of §e12 §a+3 Clicks",
                        "",
                        "§7Requires: §bEnchanting XX",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIClickableItem(21) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                try {
                    if (ExperimentationManager.startUltrasequencer(player, "SUPREME")) {
                        new GUIUltrasequencerPlay("SUPREME").open(player);
                    } else {
                        player.sendMessage("§cFailed to start Ultrasequencer");
                    }
                } catch (Exception ex) {
                    player.sendMessage("§cFailed to start Ultrasequencer: " + ex.getMessage());
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§6Supreme Experiment",
                        Material.YELLOW_DYE,
                        1,
                        "§7Ultrasequencer",
                        "",
                        "§7Numbers to remember: §d3-15",
                        "",
                        "§7XP Reward: §b400 Enchanting Exp",
                        "§7per §enumber §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e6 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "§7Chain of §e12 §a+3 Clicks",
                        "",
                        "§7Requires: §bEnchanting XXX",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                try {
                    if (ExperimentationManager.startUltrasequencer(player, "GRAND")) {
                        new GUIUltrasequencerPlay("GRAND").open(player);
                    } else {
                        player.sendMessage("§cFailed to start Ultrasequencer");
                    }
                } catch (Exception ex) {
                    player.sendMessage("§cFailed to start Ultrasequencer: " + ex.getMessage());
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§eGrand Experiment",
                        Material.ORANGE_DYE,
                        1,
                        "§7Ultrasequencer",
                        "",
                        "§7Numbers to remember: §d3-15",
                        "",
                        "§7XP Reward: §b300 Enchanting Exp",
                        "§7per §enumber §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e6 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "§7Chain of §e12 §a+3 Clicks",
                        "",
                        "§7Requires: §bEnchanting XXV",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIClickableItem(23) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                try {
                    if (ExperimentationManager.startUltrasequencer(player, "TRANSCENDENT")) {
                        new GUIUltrasequencerPlay("TRANSCENDENT").open(player);
                    } else {
                        player.sendMessage("§cFailed to start Ultrasequencer");
                    }
                } catch (Exception ex) {
                    player.sendMessage("§cFailed to start Ultrasequencer: " + ex.getMessage());
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§cTranscendent Experiment",
                        Material.RED_DYE,
                        1,
                        "§7Ultrasequencer",
                        "",
                        "§7Numbers to remember: §d3-15",
                        "",
                        "§7XP Reward: §b500 Enchanting Exp",
                        "§7per §enumber §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e6 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "§7Chain of §e12 §a+3 Clicks",
                        "",
                        "§7Requires: §bEnchanting XXXV",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(new GUIClickableItem(24) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                try {
                    if (ExperimentationManager.startUltrasequencer(player, "METAPHYSICAL")) {
                        new GUIUltrasequencerPlay("METAPHYSICAL").open(player);
                    } else {
                        player.sendMessage("§cFailed to start Ultrasequencer");
                    }
                } catch (Exception ex) {
                    player.sendMessage("§cFailed to start Ultrasequencer: " + ex.getMessage());
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        "§dMetaphysical Experiment",
                        Material.PURPLE_DYE,
                        1,
                        "§7Ultrasequencer",
                        "",
                        "§7Numbers to remember: §d3-15",
                        "",
                        "§7XP Reward: §b600 Enchanting Exp",
                        "§7per §enumber §7in longest chain!",
                        "",
                        "§7Superpairs Rewards:",
                        "§7Chain of §e6 §a+1 Click",
                        "§7Chain of §e9 §a+2 Clicks",
                        "§7Chain of §e12 §a+3 Clicks",
                        "",
                        "§7Requires: §bEnchanting XL",
                        "",
                        "§eClick to play!"
                );
            }
        });

        set(GUIClickableItem.getGoBackItem(40, new GUIExperiments()));
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
