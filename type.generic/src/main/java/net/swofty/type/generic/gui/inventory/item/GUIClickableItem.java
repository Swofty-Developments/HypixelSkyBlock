package net.swofty.type.generic.gui.inventory.item;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;

public abstract class GUIClickableItem extends GUIItem {
    public GUIClickableItem(int slot) {
        super(slot);
    }

    public abstract void run(InventoryPreClickEvent e, HypixelPlayer player);

    public void runPost(InventoryClickEvent e, HypixelPlayer player) {}

    public static GUIClickableItem getCloseItem(int slot) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose");
            }
        };
    }

    public static GUIClickableItem getGoBackItem(int slot, HypixelInventoryGUI gui) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                gui.open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aGo Back",
                        Material.ARROW, 1, "§7To " + gui.getTitle());
            }
        };
    }

    static GUIClickableItem createGUIOpenerItem(HypixelInventoryGUI gui,
                                                String name, int slot,
                                                Material type, String... lore) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(name, type, 1, lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (gui == null) return;
                gui.open(player);
            }
        };
    }
}