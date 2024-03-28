package net.swofty.types.generic.gui.inventory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    protected abstract GUIClickableItem createItemFor(T item, int slot, SkyBlockPlayer player);

    @Override
    public void open(SkyBlockPlayer player) {
        open(player, "", 1);
    }

    public void open(SkyBlockPlayer player, String query, int page) {
        Thread.startVirtualThread(() -> {
            PaginationList<T> paged = fillPaged(player, new PaginationList<>(getPaginatedSlots().length));

            if (!query.isEmpty()) {
                paged.removeIf(type -> shouldFilterFromSearch(query, type));
            }

            this.title = getTitle(player, query, page, paged);
            latestPaged = paged;

            try {
                updatePagedItems(paged, page, player);
            } catch (IllegalStateException ex) {}
            performSearch(player, query, page, paged.getPageCount());
            super.open(player);
        });
    }

    private void updatePagedItems(PaginationList<?> paged, int page, SkyBlockPlayer player) {
        List<?> thisPage = paged.getPage(page);
        if (thisPage == null) throw new IllegalStateException();
        Arrays.stream(getPaginatedSlots()).forEach(slot -> set(new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStack.builder(Material.AIR);
            }
        }));

        for (int i = 0; i < thisPage.size(); i++) {
            set(createItemFor((T) thisPage.get(i), getPaginatedSlots()[i], player));
        }
    }

    public static GUIQueryItem createSearchItem(SkyBlockPaginatedGUI<?> gui, int slot, String search) {
        return new GUIQueryItem(slot) {

            @Override
            public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                gui.items.clear();
                gui.performSearch(player, query, 1, gui.latestPaged.getPageCount());
                MinecraftServer.getSchedulerManager().scheduleTask(() -> gui.open(player, query, 1), TaskSchedule.tick(1), TaskSchedule.stop());
                return null;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aSearch", Material.BIRCH_SIGN, (short) 0, 1, "§7Query: §e" +
                        (Objects.equals(search, "") ? "None" : search));
            }
        };
    }

    public static GUIClickableItem createNavigationButton(SkyBlockPaginatedGUI<?> gui, int slot, String search, int page, boolean forward) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                int newPage = forward ? page + 1 : page - 1;
                gui.open(player, search, newPage);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                String name = forward ? "§a->" : "§a<-";
                return ItemStackCreator.createNamedItemStack(Material.ARROW, name);
            }
        };
    }

}
