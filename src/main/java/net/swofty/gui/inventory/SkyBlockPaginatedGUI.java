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

    public PaginationList<T> latestPaged;

    public SkyBlockPaginatedGUI(InventoryType size) {
        super("", size);
    }

    public abstract int[] getPaginatedSlots();

    public abstract PaginationList<T> fillPaged(SkyBlockPlayer player, PaginationList<T> paged);

    @Override
    public void open(SkyBlockPlayer player) {
        open(player, "");
    }

    public void open(SkyBlockPlayer player, String query) {
        PaginationList<T> paged = fillPaged(player, new PaginationList<>(getPaginatedSlots().length));

        if (!query.equals("")) {
            paged.removeIf(type -> shouldFilterFromSearch(query, type));
        }

        this.title = getTitle(player, query, 1, paged);
        latestPaged = paged;

        List<T> thisPage = paged.getPage(1);
        for (int i = 0; i < thisPage.size(); i++) {
            T item = thisPage.get(i);
            set(getItem(item, getPaginatedSlots()[i]));
        }

        search(player, query, 1, paged.getPageCount());

        super.open(player);
    }

    public void updatePagedItems(PaginationList<T> paged) {
        List<T> thisPage = paged.getPage(1);
        for (int i = 0; i < thisPage.size(); i++) {
            T item = thisPage.get(i);
            set(getItem(item, getPaginatedSlots()[i]));
        }
    }

    public abstract boolean shouldFilterFromSearch(String query, T item);

    public abstract void search(SkyBlockPlayer player, String query, int page, int maxPage);

    public abstract String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<T> paged);

    public abstract GUIClickableItem getItem(T item, int slot);

    @Override
    public void setItems(InventoryGUIOpenEvent e) {}

    public static GUIQueryItem getSearchItem(SkyBlockPaginatedGUI<?> gui, int slot) {
        return new GUIQueryItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

            }

            @Override
            public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                gui.items.clear();
                gui.search(player, query, 1, gui.latestPaged.getPageCount());
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

    public static GUIClickableItem getBackButton(SkyBlockPaginatedGUI<?> gui, int slot, String search, int page, int maxPage) {
        return new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                gui.items.clear();
                gui.search(player, search, page - 1, maxPage);
            }

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.ARROW, "&a<-");
            }
        };
    }

    public static GUIClickableItem getForward(SkyBlockPaginatedGUI<?> gui, int slot, String search, int page, int maxPage) {
        return new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
                gui.search(player, search, page + 1, maxPage);
            }

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.ARROW, "&a->");
            }
        };
    }
}
