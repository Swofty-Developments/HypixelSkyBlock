package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIBedWarsQuests extends HypixelInventoryGUI {

    public GUIBedWarsQuests() {
        super("Bed Wars Quests", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aBed Wars Quests & Challenges",
                        Material.RED_BED,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Bed Wars."
                );
            }
        });
        set(new GUIItem(36) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aBed Wars Achievements",
                        Material.DIAMOND,
                        1,
                        "§7Unlocked: §b0§7/§b0 §8(0%)",  // TODO: Replace with real achievement data
                        "§7Points: §e0§7/§e0 §8(0%)", // TODO: Replace with real achievement data
                        "",
                        "§7Legacy Unlocked: §b0", // you can't even unlock these anymore, so always 0
                        "§7Legacy Points: §e0",
                        "",
                        "§eClick to view achievements!"
                );
            }
        });

        set(new GUIItem(44) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aAuto-Accept Quests: §cOFF", // TODO: implement
                        Material.GRAY_DYE, // Material.LIME_DYE
                        1,
                        "§7Click to automatically accept",
                        "§7quests whenever you join a",
                        "§7game lobby.",
                        "",
                        "§7Requires §bMVP§c+"
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
