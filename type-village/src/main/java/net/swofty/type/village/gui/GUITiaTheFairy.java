package net.swofty.type.village.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUITiaTheFairy extends SkyBlockInventoryGUI {
    public GUITiaTheFairy() {
        super("Fairy", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§aExchange Fairy Souls", "b96923ad247310007f6ae5d326d847ad53864cf16c3565a181dc8e6b20be2387", 1,
                        "§7Find §dFairy Souls §7around the",
                        "§7world and bring them back to me",
                        "§7and I will reward you with",
                        "§7Skyblock XP and Backpack Slots!",
                        "",
                        "§7Fairy Souls: §e0§7/§d5",
                        "",
                        "§7Next Reward:",
                        "§8+ §b10 Skyblock XP",
                        "§6Backpack Slot #2",
                        "",
                        "§cYou don't have enough Fairy Souls!");
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
        e.setCancelled(true);
    }
}
