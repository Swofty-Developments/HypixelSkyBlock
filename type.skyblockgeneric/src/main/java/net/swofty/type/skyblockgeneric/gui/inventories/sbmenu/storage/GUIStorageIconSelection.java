package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.storage;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointStorage;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;

public class GUIStorageIconSelection extends HypixelPaginatedGUI<Material> {
    private int page;
    private HypixelInventoryGUI previous;

    protected GUIStorageIconSelection(int page, HypixelInventoryGUI previous) {
        super(InventoryType.CHEST_6_ROW);

        this.page = page;
        this.previous = previous;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
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
    protected PaginationList<Material> fillPaged(HypixelPlayer player, PaginationList<Material> paged) {
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
    protected void performSearch(HypixelPlayer player, String query, int page, int maxPage) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getCloseItem(49));
        if (previous != null)
            set(GUIClickableItem.getGoBackItem(48, previous));
        set(createSearchItem(this, 50, query));

        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }
    }

    @Override
    protected String getTitle(HypixelPlayer player, String query, int page, PaginationList<Material> paged) {
        return "Choose an Icon (" + page + "/" + paged.getPageCount() + ")";
    }

    @Override
    protected GUIClickableItem createItemFor(Material item, int slot, HypixelPlayer player) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointStorage.PlayerStorage storage = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.STORAGE, DatapointStorage.class).getValue();

                if (item == Material.BARRIER) {
                    storage.setDisplay(page, Material.PURPLE_STAINED_GLASS_PANE);
                    new GUIStorage().open(player);
                    return;
                }

                storage.setDisplay(page, item);
                new GUIStorage().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        (item == Material.BARRIER ? "§cReset" :
                                StringUtility.toNormalCase(item.name().replace("minecraft:", ""))),
                        item, 1,
                        "§7Ender Chest icons replace the glass",
                        "§7panes in the navigation bar.",
                        " ",
                        "§eClick to select!");
            }
        };
    }
}
