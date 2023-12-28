package net.swofty.gui.inventory.item;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.user.SkyBlockPlayer;

public interface GUIClickableItem extends GUIItem {
    void run(InventoryPreClickEvent e, SkyBlockPlayer player);

    static GUIClickableItem getCloseItem(int slot) {
        return new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
            }

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose");
            }
        };
    }

    static GUIClickableItem getGoBackItem(int slot, SkyBlockInventoryGUI gui) {
        return new GUIClickableItem() {
            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                gui.open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aGo Back",
                        Material.ARROW,
                        (short) 0, 1, "§7To " + gui.getTitle());
            }
        };
    }

    static GUIClickableItem createGUIOpenerItem(SkyBlockInventoryGUI gui, SkyBlockPlayer player, String name, int slot, Material type, short data, String... lore) {
        return new GUIClickableItem() {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(name, type, data, 1, lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (gui == null) return;
                gui.open(player);
            }

            @Override
            public int getSlot() {
                return slot;
            }
        };
    }
}