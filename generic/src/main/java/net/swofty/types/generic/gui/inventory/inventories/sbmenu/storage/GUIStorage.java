package net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStorage;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIStorage extends SkyBlockInventoryGUI {
    public GUIStorage() {
        super("Storage", InventoryType.CHEST_6_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aEnder Chest", Material.ENDER_CHEST, 1,
                        "§7Store global items you can",
                        "§7access anywhere in your ender",
                        "§7chest.");
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        DatapointStorage.PlayerStorage storage = getPlayer().getDataHandler().get(
                DataHandler.Data.STORAGE, DatapointStorage.class
        ).getValue();

        // Initialize empty storages
        if (!storage.hasPage(1)) {
            storage.addPage(1);
            storage.addPage(2);
        }

        for (int ender_slot = 9; ender_slot < 18; ender_slot++) {
            int page = ender_slot - 8;

            set(new GUIClickableItem(ender_slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (!storage.hasPage(page)) return;

                    if (e.getClickType() == ClickType.RIGHT_CLICK) {
                        new GUIStorageIconSelection(page, GUIStorage.this).open(player);
                    } else {
                        new GUIStoragePage(page).open(player);
                    }
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (!storage.hasPage(page))
                        return ItemStackCreator.getStack("§cLocked Page", Material.RED_STAINED_GLASS_PANE, 1,
                                "§7Unlock more Ender Chest pages in",
                                "§7the community shop!");

                    Material material = storage.getPage(page).display;

                    return ItemStackCreator.getStack("§aEnder Chest Page " + (page), material, page,
                            " ",
                            "§eLeft-click to open!",
                            "§eRight-click to change icon!");
                }
            });
        }

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
