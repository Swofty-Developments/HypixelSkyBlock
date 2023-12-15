package net.swofty.gui.inventory;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIQueryItem;
import net.swofty.item.ItemType;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.PaginationList;

import java.util.List;

public abstract class SkyBlockPaginatedGUI<T> extends SkyBlockInventoryGUI {

    protected PaginationList<T> latestPaged;

    protected SkyBlockPaginatedGUI(InventoryType size) {
        super("", size);
    }

    protected abstract int[] getPaginatedSlots();
    protected abstract PaginationList<T> fillPaged(SkyBlockPlayer player, PaginationList<T> paged);
    protected abstract boolean shouldFilterFromSearch(String query, T item);
    protected abstract void performSearch(SkyBlockPlayer player, String query, int page, int maxPage);
    protected abstract String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<T> paged);
    protected abstract GUIClickableItem createItemFor(T item, int slot);

    @Override
    public void open(SkyBlockPlayer player) {
        open(player, "");
    }

    public void open(SkyBlockPlayer player, String query) {
        PaginationList<T> paged = fillPaged(player, new PaginationList<>(getPaginatedSlots().length));

        if (!query.isEmpty()) {
            paged.removeIf(type -> shouldFilterFromSearch(query, type));
        }

        this.title = getTitle(player, query, 1, paged);
        latestPaged = paged;

        updatePagedItems(paged);
        performSearch(player, query, 1, paged.getPageCount());
        super.open(player);
    }

    private void updatePagedItems(PaginationList<T> paged) {
        List<T> thisPage = paged.getPage(1);
        for (int i = 0; i < thisPage.size(); i++) {
            set(createItemFor(thisPage.get(i), getPaginatedSlots()[i]));
        }
    }

    public static GUIQueryItem createSearchItem(SkyBlockPaginatedGUI<?> gui, int slot) {
        return new GUIQueryItem() {

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {}

            @Override
            public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                gui.items.clear();
                gui.performSearch(player, query, 1, gui.latestPaged.getPageCount());
                gui.open(player, query);
                return null;
            }

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.BIRCH_SIGN, "§aSearch");
            }
        };
    }

    public static GUIClickableItem createNavigationButton(SkyBlockPaginatedGUI<?> gui, int slot, String search, int page, boolean forward) {
        return new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                gui.items.clear();
                int newPage = forward ? page + 1 : page - 1;
                gui.performSearch(player, search, newPage, gui.latestPaged.getPageCount());
            }

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                String name = forward ? "&a->" : "&a<-";
                return ItemStackCreator.createNamedItemStack(Material.ARROW, name);
            }
        };
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        // Not implemented. If the method is not required, consider removing it
        // or creating a default implementation in the parent class.
    }
}
