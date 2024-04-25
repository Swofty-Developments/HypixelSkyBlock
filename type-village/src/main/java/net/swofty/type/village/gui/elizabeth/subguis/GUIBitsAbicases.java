package net.swofty.type.village.gui.elizabeth.subguis;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.village.gui.elizabeth.GUIBitsShop;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIBitsAbicases extends SkyBlockInventoryGUI {

    public GUIBitsAbicases() {
        super("Bits Shop - Abicases", InventoryType.CHEST_5_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(40, new GUIBitsAbiphone()));
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIBitsSumsungAbicases().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§fSumsung© Abicases", "36a10ee2155fc0134d9392000a9eb9ebcba8526eff3893e54434e825e558fb55", 1,
                        "§7Sumsung focuses on the",
                        "§7technology.",
                        " ",
                        "§7Upgrade your Abiphone remotely",
                        "§7through cloud-based blockchain",
                        "§7agile immutable dev-ops fuzzy",
                        "§7cases.",
                        " ",
                        "§eClick to view models!");
            }
        });
        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIBitsRezarAbicase().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§aRezar® Abicase", "b2128f48d997186563fbc5b47a88c0d0aac92fa2c285cd1fae420c34fa8f2010", 1,
                        "§7Play hard, play fair and do it all in",
                        "§7green.",
                        " ",
                        "§7Perfect for those who have time",
                        "§7to grind but no time to call their",
                        "§7close ones.",
                        " ",
                        "§eClick to view THE model!");
            }
        });
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIBitsBlueAbicases().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§9Blue™ Abicases", "a3c153c391c34e2d328a60839e683a9f82ad3048299d8bc6a39e6f915cc5a", 1,
                        "§7Blue Abicases are not all blue.",
                        "§7Their color reflects your",
                        "§7personality, your life and your",
                        "§7legacy.",
                        " ",
                        "§7Think of it this way: Is your",
                        "§7personality just a recolor of last",
                        "§7year's?",
                        " ",
                        "§7Blue™ says... well §omaybe§7!",
                        " ",
                        "§eClick to pick a color!");
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
