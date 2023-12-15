package net.swofty.gui.inventory.inventories;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.updater.NonPlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.PaginationList;

public class GUICreative extends SkyBlockPaginatedGUI<ItemType> {

    public GUICreative() {
        super(InventoryType.CHEST_6_ROW);
    }

    @Override
    public int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    public PaginationList<ItemType> fillPaged(SkyBlockPlayer player, PaginationList<ItemType> paged) {
        return paged.addAll(ItemType.values());
    }

    @Override
    public boolean shouldFilterFromSearch(String query, ItemType item) {
        return !item.name().toLowerCase().contains(query.replaceAll(" ", "_").toLowerCase());
    }

    @Override
    public void search(SkyBlockPlayer player, String query, int page, int maxPage) {
        if (page > 1)
            set(getBackButton(this, 45, query, page, maxPage));
        if (page < maxPage)
            set(getForward(this, 53, query, page, maxPage));

        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getCloseItem(50));
        set(getSearchItem(this, 48));
    }

    @Override
    public String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<ItemType> paged) {
        return "Creative Menu | Page " + page + "/" + paged.getPageCount();
    }

    @Override
    public GUIClickableItem getItem(ItemType item, int slot) {
        SkyBlockItem skyBlockItem = new SkyBlockItem(item);
        ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();

        return new GUIClickableItem()
        {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));
                player.sendMessage("§aGave you a §e" + item.name() + "§a.");
                player.getInventory().addItemStack(itemStack.build());
            }

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return itemStack;
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
