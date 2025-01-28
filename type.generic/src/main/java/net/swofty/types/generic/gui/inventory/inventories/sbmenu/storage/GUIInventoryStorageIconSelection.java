package net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStorage;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;

public class GUIInventoryStorageIconSelection extends SkyBlockPaginatedInventory<Material> {
    private final int page;
    private final SkyBlockAbstractInventory previous;

    protected GUIInventoryStorageIconSelection(int page, SkyBlockAbstractInventory previous) {
        super(InventoryType.CHEST_6_ROW);
        this.page = page;
        this.previous = previous;
    }

    @Override
    protected int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    protected PaginationList<Material> fillPaged(SkyBlockPlayer player, PaginationList<Material> paged) {
        paged.add(Material.BARRIER);

        List<Material> vanilla = new ArrayList<>(Material.values().stream().toList());
        vanilla.removeIf((element) -> ItemType.isVanillaReplaced(element.name()));
        paged.addAll(vanilla);

        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, Material item) {
        return !item.name().toLowerCase().contains(query.replaceAll(" ", "_").toLowerCase());
    }

    @Override
    protected void performSearch(SkyBlockPlayer player, String query, int currentPage, int maxPage) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        if (previous != null) {
            attachItem(GUIItem.builder(48)
                    .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                            "§7To " + previous.getTitle()).build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(previous);
                        return true;
                    })
                    .build());
        }

        // Search button
        attachItem(createSearchItem(50, query));

        // Navigation buttons
        if (currentPage > 1) {
            attachItem(createNavigationButton(45, query, currentPage, false));
        }
        if (currentPage < maxPage) {
            attachItem(createNavigationButton(53, query, currentPage, true));
        }
    }

    @Override
    protected Component getTitle(SkyBlockPlayer player, String query, int currentPage, PaginationList<Material> paged) {
        return Component.text("Choose an Icon (" + currentPage + "/" + paged.getPageCount() + ")");
    }

    @Override
    protected GUIItem createItemFor(Material item, int slot, SkyBlockPlayer player) {
        return GUIItem.builder(slot)
                .item(ItemStackCreator.getStack(
                        (item == Material.BARRIER ? "§cReset" :
                                StringUtility.toNormalCase(item.name().replace("minecraft:", ""))),
                        item, 1,
                        "§7Ender Chest icons replace the glass",
                        "§7panes in the navigation bar.",
                        " ",
                        "§eClick to select!").build())
                .onClick((ctx, clickedItem) -> {
                    DatapointStorage.PlayerStorage storage = ctx.player().getDataHandler()
                            .get(DataHandler.Data.STORAGE, DatapointStorage.class).getValue();

                    if (item == Material.BARRIER) {
                        storage.setDisplay(page, Material.PURPLE_STAINED_GLASS_PANE);
                    } else {
                        storage.setDisplay(page, item);
                    }

                    ctx.player().openInventory(new GUIStorage());
                    return true;
                })
                .build();
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }
}