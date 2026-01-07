package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsUnlocks;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

/**
 * Main Kits & Perks menu for SkyWars.
 * Allows players to access kit selectors and perk configuration for different game modes.
 */
public class GUIKitsPerks extends HypixelInventoryGUI {

    public GUIKitsPerks() {
        super("Kits & Perks", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        if (handler == null) return;

        DatapointSkywarsUnlocks.SkywarsUnlocks unlocks = handler.get(
                SkywarsDataHandler.Data.UNLOCKS,
                DatapointSkywarsUnlocks.class
        ).getValue();

        long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();

        // ===== KITS ROW =====

        // Mini Kits (slot 10) - NOT IMPLEMENTED
        set(new GUIClickableItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMini Kits",
                        Material.WOODEN_SWORD,
                        1,
                        "§7Selection of unique kits for Mini",
                        "§7games.",
                        "",
                        "§cNot implemented yet!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§cMini mode kits are not implemented yet!");
            }
        });

        // Normal Kits (slot 12)
        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aNormal Kits",
                        Material.STONE_SWORD,
                        1,
                        "§7Selection of unique kits for Normal",
                        "§7games.",
                        "",
                        "§eClick to browse!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIKitSelector("NORMAL").open(player);
            }
        });

        // Insane Kits (slot 14)
        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aInsane Kits",
                        Material.IRON_SWORD,
                        1,
                        "§7Selection of unique kits for Insane",
                        "§7games.",
                        "",
                        "§eClick to browse!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIKitSelector("INSANE").open(player);
            }
        });

        // Mega Kits (slot 16) - NOT IMPLEMENTED
        set(new GUIClickableItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMega Kits",
                        Material.DIAMOND_SWORD,
                        1,
                        "§7Selection of unique kits for Mega",
                        "§7Mode.",
                        "",
                        "§cNot implemented yet!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§cMega mode kits are not implemented yet!");
            }
        });

        // ===== PERKS ROW =====

        // Mini Perks (slot 19) - NOT IMPLEMENTED
        set(new GUIClickableItem(19) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSelect Mini Perks",
                        Material.CAULDRON,
                        1,
                        "§7Selection of unique perks for Mini",
                        "§7games.",
                        "",
                        "§cNot implemented yet!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§cMini mode perks are not implemented yet!");
            }
        });

        // Normal Perks (slot 21)
        set(new GUIClickableItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                List<String> selectedPerks = unlocks.getSelectedPerksForMode("NORMAL");
                boolean hasEmptySlot = selectedPerks.isEmpty();

                if (hasEmptySlot) {
                    return ItemStackCreator.getStack(
                            "§aSelect Normal Perks",
                            Material.CAULDRON,
                            1,
                            "§7Selection of unique perks for Normal",
                            "§7games.",
                            "",
                            "§cYou have 1 empty Perk Slot in this",
                            "§cmode!",
                            "",
                            "§eClick to browse!"
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "§aSelect Normal Perks",
                            Material.CAULDRON,
                            1,
                            "§7Selection of unique perks for Normal",
                            "§7games.",
                            "",
                            "§eClick to browse!"
                    );
                }
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUISelectNormalPerks().open(player);
            }
        });

        // Insane Perks (slot 23)
        set(new GUIClickableItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aToggle Insane Perks",
                        Material.CAULDRON,
                        1,
                        "§7All perks you own in this mode are",
                        "§7enabled simultaneously.",
                        "",
                        "§7You can choose to disable any you",
                        "§7don't want to have active.",
                        "",
                        "§eClick to browse!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIToggleInsanePerks().open(player);
            }
        });

        // Mega Perks (slot 25) - NOT IMPLEMENTED
        set(new GUIClickableItem(25) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSelect Mega Perks",
                        Material.CAULDRON,
                        1,
                        "§7Selection of unique perks for Mega",
                        "§7Mode.",
                        "",
                        "§cNot implemented yet!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§cMega mode perks are not implemented yet!");
            }
        });

        // ===== BOTTOM ROW =====

        // Go Back (slot 39)
        set(new GUIClickableItem(39) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGo Back",
                        Material.ARROW,
                        1,
                        "§7To SkyWars Menu"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUISkyWarsMenu().open(player);
            }
        });

        // Total Coins display (slot 40)
        set(new GUIItem(40) {
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
