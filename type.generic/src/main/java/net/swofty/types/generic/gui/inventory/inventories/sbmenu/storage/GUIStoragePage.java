package net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStorage;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIStoragePage extends SkyBlockAbstractInventory {
    private final int page;

    public GUIStoragePage(int page) {
        super(InventoryType.CHEST_6_ROW);
        this.page = page;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        int highestPage = player.getDataHandler().get(DataHandler.Data.STORAGE, DatapointStorage.class)
                .getValue().getHighestPage();

        doAction(new SetTitleAction(Component.text("Ender Chest (" + page + "/" + highestPage + ")")));

        // Fill top row with black glass panes
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build(), 0, 8);

        // Close button
        attachItem(GUIItem.builder(0)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(1)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Storage").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIStorage());
                    return true;
                })
                .build());

        setupNavigationButtons(player, highestPage);
        setupStorageItems(player);
    }

    private void setupNavigationButtons(SkyBlockPlayer player, int highestPage) {
        if (page != highestPage) {
            // Last page button
            attachItem(GUIItem.builder(8)
                    .item(ItemStackCreator.getStackHead("§eLast Page >>",
                            "1ceb50d0d79b9fb790a7392660bc296b7ad2f856c5cbe1c566d99cfec191e668").build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIStoragePage(highestPage));
                        return true;
                    })
                    .build());

            // Next page button
            attachItem(GUIItem.builder(7)
                    .item(ItemStackCreator.getStackHead("§aNext Page >>",
                            "848ca732a6e35dafd15e795ebc10efedd9ef58ff2df9b17af6e3d807bdc0708b").build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIStoragePage(page + 1));
                        return true;
                    })
                    .build());
        }

        if (page != 1) {
            // First page button
            attachItem(GUIItem.builder(5)
                    .item(ItemStackCreator.getStackHead("§e< First Page",
                            "8af22a97292de001079a5d98a0ae3a82c427172eabc370ed6d4a31c7e3a0024f").build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIStoragePage(1));
                        return true;
                    })
                    .build());

            // Previous page button
            attachItem(GUIItem.builder(6)
                    .item(ItemStackCreator.getStackHead("§a< Previous Page",
                            "9c042597eda9f061794fe11dacf78926d247f9eea8ddef39dfbe6022989b8395").build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIStoragePage(page - 1));
                        return true;
                    })
                    .build());
        }
    }

    private void setupStorageItems(SkyBlockPlayer player) {
        DatapointStorage.PlayerStorage storage = player.getDataHandler()
                .get(DataHandler.Data.STORAGE, DatapointStorage.class).getValue();

        AtomicInteger slot = new AtomicInteger(9);
        Arrays.stream(storage.getPage(page).getItems()).forEach(item -> {
            attachItem(GUIItem.builder(slot.getAndIncrement())
                    .item(() -> {
                        if (item == null || item.isNA()) {
                            return ItemStackCreator.createNamedItemStack(Material.AIR).build();
                        }
                        return PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build();
                    })
                    .onClick((ctx, clickedItem) -> true) // Allow pickup
                    .build());
        });
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        saveInventoryItems((SkyBlockPlayer) event.getPlayer());
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        saveInventoryItems(player);
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    private void saveInventoryItems(SkyBlockPlayer player) {
        DatapointStorage.PlayerStorage storage = player.getDataHandler()
                .get(DataHandler.Data.STORAGE, DatapointStorage.class).getValue();

        storage.setItems(page, getItemsInInventory());
    }

    private SkyBlockItem[] getItemsInInventory() {
        SkyBlockItem[] items = new SkyBlockItem[45];

        for (int i = 9; i < 54; i++) {
            items[i - 9] = new SkyBlockItem(getItemStack(i));
        }

        return items;
    }
}