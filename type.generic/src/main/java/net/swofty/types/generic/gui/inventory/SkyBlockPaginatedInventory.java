package net.swofty.types.generic.gui.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class SkyBlockPaginatedInventory<T> extends SkyBlockAbstractInventory {
    protected PaginationList<T> latestPaged;
    protected String currentQuery = "";
    protected int currentPage = 1;

    protected SkyBlockPaginatedInventory(InventoryType type) {
        super(type);
    }

    protected abstract int[] getPaginatedSlots();
    protected abstract PaginationList<T> fillPaged(SkyBlockPlayer player, PaginationList<T> paged);
    protected abstract boolean shouldFilterFromSearch(String query, T item);
    protected abstract void performSearch(SkyBlockPlayer player, String query, int page, int maxPage);
    protected abstract Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<T> paged);
    protected abstract GUIItem createItemFor(T item, int slot, SkyBlockPlayer player);

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        open(player, "", 1);
    }

    public void open(SkyBlockPlayer player, String query, int page) {
        Thread.startVirtualThread(() -> {
            PaginationList<T> paged = fillPaged(player, new PaginationList<>(getPaginatedSlots().length));

            if (!query.isEmpty()) {
                paged.removeIf(type -> shouldFilterFromSearch(query, type));
            }

            this.currentQuery = query;
            this.currentPage = page;
            this.latestPaged = paged;

            doAction(new SetTitleAction(getTitle(player, query, page, paged)));

            try {
                updatePagedItems(paged, page, player);
            } catch (IllegalStateException ex) {
                // Handle invalid page
            }

            performSearch(player, query, page, paged.getPageCount());
        });
    }

    private void updatePagedItems(PaginationList<?> paged, int page, SkyBlockPlayer player) {
        List<?> thisPage = paged.getPage(page);
        if (thisPage == null) throw new IllegalStateException();

        // Clear current slots
        Arrays.stream(getPaginatedSlots()).forEach(slot ->
                attachItem(GUIItem.builder(slot)
                        .item(ItemStack.of(Material.AIR))
                        .build())
        );

        // Add new items
        for (int i = 0; i < thisPage.size(); i++) {
            attachItem(createItemFor((T) thisPage.get(i), getPaginatedSlots()[i], player));
        }
    }

    protected GUIItem createSearchItem(int slot, String search) {
        return GUIItem.builder(slot)
                .item(ItemStackCreator.getStack("§aSearch",
                        Material.BIRCH_SIGN, 1,
                        "§7Query: §e" + (Objects.equals(search, "") ? "None" : search)).build())
                .onClick((context, item) -> {
                    SkyBlockSignGUI signGUI = new SkyBlockSignGUI(context.player());
                    signGUI.open(new String[]{"§eQuery: §a" + search, "§7Enter your query above!"}).join();
                    open(context.player(), search, 1);
                    return true;
                })
                .build();
    }

    protected GUIItem createNavigationButton(int slot, String search, int page, boolean forward) {
        String name = forward ? "§aNext Page" : "§aPrevious Page";
        String lore = forward ? "§ePage " + (page + 1) : "§ePage " + (page - 1);

        return GUIItem.builder(slot)
                .item(ItemStackCreator.getStack(name,
                        Material.ARROW, 1,
                        "§7" + (forward ? "Next Page" : "Previous Page")).build())
                .onClick((context, item) -> {
                    int newPage = forward ? page + 1 : page - 1;
                    open(context.player(), search, newPage);
                    return true;
                })
                .build();
    }
}
