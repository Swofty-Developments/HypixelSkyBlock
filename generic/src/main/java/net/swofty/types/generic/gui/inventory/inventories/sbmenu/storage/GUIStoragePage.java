package net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStorage;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIStoragePage extends SkyBlockInventoryGUI {
    public int page;
    public GUIStoragePage(int page) {
        super("Ender Chest", InventoryType.CHEST_6_ROW);

        this.page = page;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = getPlayer();
        int highestPage = player.getDataHandler().get(DataHandler.Data.STORAGE, DatapointStorage.class)
                .getValue().getHighestPage();

        e.inventory().setTitle(Component.text(
                "Ender Chest (" + page + "/" + highestPage + ")"
        ));

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 0, 8);

        set(GUIClickableItem.getCloseItem(0));
        set(GUIClickableItem.getGoBackItem(1, new GUIStorage()));

        if (page != highestPage) {
            set(new GUIClickableItem(8) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIStoragePage(highestPage).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead("§eLast Page >>",
                            "1ceb50d0d79b9fb790a7392660bc296b7ad2f856c5cbe1c566d99cfec191e668");
                }
            });
            set(new GUIClickableItem(7) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIStoragePage(page + 1).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead("§aNext Page >>",
                            "848ca732a6e35dafd15e795ebc10efedd9ef58ff2df9b17af6e3d807bdc0708b");
                }
            });
        }
        if (page != 1) {
            set(new GUIClickableItem(5) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIStoragePage(1).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead("§e< First Page",
                            "8af22a97292de001079a5d98a0ae3a82c427172eabc370ed6d4a31c7e3a0024f");
                }
            });

            set(new GUIClickableItem(6) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIStoragePage(page - 1).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead("§a< Previous Page",
                            "9c042597eda9f061794fe11dacf78926d247f9eea8ddef39dfbe6022989b8395");
                }
            });
        }

        DatapointStorage.PlayerStorage storage = getPlayer()
                .getDataHandler()
                .get(DataHandler.Data.STORAGE, DatapointStorage.class).getValue();

        AtomicInteger slot = new AtomicInteger(9);
        Arrays.stream(storage.getPage(page).getItems()).forEach(item -> {
            set(new GUIItem(slot.getAndIncrement()) {
                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (item == null || item.isNA())
                        return ItemStackCreator.createNamedItemStack(Material.AIR);
                    return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
                }
            });
        });

        updateItemStacks(getInventory(), player);
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        DatapointStorage.PlayerStorage storage = getPlayer()
                .getDataHandler()
                .get(DataHandler.Data.STORAGE, DatapointStorage.class).getValue();

        storage.setItems(page, getItemsInInventory());
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
        DatapointStorage.PlayerStorage storage = getPlayer()
                .getDataHandler()
                .get(DataHandler.Data.STORAGE, DatapointStorage.class).getValue();

        storage.setItems(page, getItemsInInventory());
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }

    public SkyBlockItem[] getItemsInInventory() {
        SkyBlockItem[] items = new SkyBlockItem[45];

        for (int i = 9; i < 54; i++) {
            items[i - 9] = new SkyBlockItem(getInventory().getItemStack(i));
        }

        return items;
    }
}
