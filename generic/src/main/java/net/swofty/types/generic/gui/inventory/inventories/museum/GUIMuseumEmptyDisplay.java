package net.swofty.types.generic.gui.inventory.inventories.museum;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

public class GUIMuseumEmptyDisplay extends SkyBlockPaginatedGUI<SkyBlockItem> {
    public GUIMuseumEmptyDisplay(MuseumDisplays display, int position) {
        super(InventoryType.CHEST_6_ROW);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }

    @Override
    public int[] getPaginatedSlots() {
        return new int[0];
    }

    @Override
    public PaginationList<SkyBlockItem> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockItem> paged) {
        return null;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return false;
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {

    }

    @Override
    public String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        return null;
    }

    @Override
    public GUIClickableItem createItemFor(SkyBlockItem item, int slot, SkyBlockPlayer player) {
        return null;
    }
}
