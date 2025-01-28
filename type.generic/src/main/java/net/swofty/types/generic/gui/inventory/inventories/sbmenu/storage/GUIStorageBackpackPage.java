package net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBackpacks;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.BackpackComponent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.MathUtility;

import java.util.concurrent.atomic.AtomicInteger;

public class GUIStorageBackpackPage extends SkyBlockAbstractInventory {
    private final int page;
    private final int slots;
    private final SkyBlockItem item;

    public GUIStorageBackpackPage(int page, SkyBlockItem item) {
        super(MathUtility.getFromSize(9 + item.getComponent(BackpackComponent.class).getRows() * 9));

        this.slots = item.getComponent(BackpackComponent.class).getRows() * 9;
        this.page = page;
        this.item = item;

        doAction(new SetTitleAction(Component.text(StringUtility.getTextFromComponent(
                new NonPlayerItemUpdater(item).getUpdatedItem().build().get(ItemComponent.CUSTOM_NAME))
                + " (Slot #" + page + ")")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        DatapointBackpacks.PlayerBackpacks data = player.getDataHandler()
                .get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class).getValue();

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build(), 0, 8);

        // Close button
        attachItem(GUIItem.builder(0)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, unused) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(1)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Storage").build())
                .onClick((ctx, unused) -> {
                    ctx.player().openInventory(new GUIStorage());
                    return true;
                })
                .build());

        setupNavigationButtons(player, data);
        setupBackpackItems(player);
    }

    private void setupNavigationButtons(SkyBlockPlayer player, DatapointBackpacks.PlayerBackpacks data) {
        if (page != data.getHighestBackpackSlot()) {
            // Last page button
            attachItem(GUIItem.builder(8)
                    .item(ItemStackCreator.getStackHead("§eLast Page >>",
                            "1ceb50d0d79b9fb790a7392660bc296b7ad2f856c5cbe1c566d99cfec191e668").build())
                    .onClick((ctx, unused) -> {
                        ctx.player().openInventory(new GUIStorageBackpackPage(data.getHighestBackpackSlot(),
                                new SkyBlockItem(data.getBackpacks().get(data.getHighestBackpackSlot()))));
                        return true;
                    })
                    .build());

            if (data.getBackpacks().containsKey(page + 1)) {
                // Next page button
                attachItem(GUIItem.builder(7)
                        .item(ItemStackCreator.getStackHead("§aNext Page >>",
                                "848ca732a6e35dafd15e795ebc10efedd9ef58ff2df9b17af6e3d807bdc0708b").build())
                        .onClick((ctx, unused) -> {
                            ctx.player().openInventory(new GUIStorageBackpackPage(page + 1,
                                    new SkyBlockItem(data.getBackpacks().get(page + 1))));
                            return true;
                        })
                        .build());
            }
        }

        if (page != data.getLowestBackpackSlot()) {
            // First page button
            attachItem(GUIItem.builder(5)
                    .item(ItemStackCreator.getStackHead("§e< First Page",
                            "8af22a97292de001079a5d98a0ae3a82c427172eabc370ed6d4a31c7e3a0024f").build())
                    .onClick((ctx, unused) -> {
                        ctx.player().openInventory(new GUIStorageBackpackPage(data.getLowestBackpackSlot(),
                                new SkyBlockItem(data.getBackpacks().get(data.getLowestBackpackSlot()))));
                        return true;
                    })
                    .build());

            if (data.getBackpacks().containsKey(page - 1)) {
                // Previous page button
                attachItem(GUIItem.builder(6)
                        .item(ItemStackCreator.getStackHead("§a< Previous Page",
                                "9c042597eda9f061794fe11dacf78926d247f9eea8ddef39dfbe6022989b8395").build())
                        .onClick((ctx, unused) -> {
                            ctx.player().openInventory(new GUIStorageBackpackPage(page - 1,
                                    new SkyBlockItem(data.getBackpacks().get(page - 1))));
                            return true;
                        })
                        .build());
            }
        }
    }

    private void setupBackpackItems(SkyBlockPlayer player) {
        AtomicInteger slot = new AtomicInteger(9);
        item.getAttributeHandler().getBackpackData().items().forEach(backpackItem -> {
            attachItem(GUIItem.builder(slot.getAndIncrement())
                    .item(() -> {
                        if (backpackItem == null || new SkyBlockItem(backpackItem).isNA()) {
                            return ItemStackCreator.createNamedItemStack(Material.AIR).build();
                        }
                        return PlayerItemUpdater.playerUpdate(player, new SkyBlockItem(backpackItem).getItemStack()).build();
                    })
                    .onClick((ctx, clickedItem) -> true) // Allow pickup
                    .build());
        });
    }

    private void saveBackpackContents() {
        item.getAttributeHandler().getBackpackData().items().clear();

        for (int i = 9; i < slots + 9; i++) {
            item.getAttributeHandler().getBackpackData().items()
                    .add(new SkyBlockItem(getItemStack(i)).toUnderstandable());
        }

        owner.getDataHandler().get(DataHandler.Data.BACKPACKS, DatapointBackpacks.class)
                .getValue().getBackpacks().put(page, item.toUnderstandable());
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
        saveBackpackContents();
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
        saveBackpackContents();
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        // Empty implementation
    }
}