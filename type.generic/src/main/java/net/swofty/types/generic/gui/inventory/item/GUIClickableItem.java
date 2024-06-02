package net.swofty.types.generic.gui.inventory.item;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.user.SkyBlockPlayer;

public abstract class GUIClickableItem extends GUIItem {
    public GUIClickableItem(int slot) {
        super(slot);
    }

    public abstract void run(InventoryPreClickEvent e, SkyBlockPlayer player);

    public void runPost(InventoryClickEvent e, SkyBlockPlayer player) {}

    public static GUIClickableItem getCloseItem(int slot) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose");
            }
        };
    }

    public static GUIClickableItem getGoBackItem(int slot, SkyBlockInventoryGUI gui) {
        return new GUIClickableItem(slot) {
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

    static GUIClickableItem createGUIOpenerItem(SkyBlockInventoryGUI gui,
                                                String name, int slot,
                                                Material type, short data, String... lore) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(name, type, data, 1, lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (gui == null) return;
                gui.open(player);
            }
        };
    }
}