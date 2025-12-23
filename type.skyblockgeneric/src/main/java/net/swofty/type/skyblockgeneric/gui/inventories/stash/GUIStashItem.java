package net.swofty.type.skyblockgeneric.gui.inventories.stash;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointStash;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class GUIStashItem extends HypixelPaginatedGUI<SkyBlockItem> {

    public GUIStashItem() {
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
    public PaginationList<SkyBlockItem> fillPaged(HypixelPlayer player, PaginationList<SkyBlockItem> paged) {
        SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
        List<SkyBlockItem> itemStash = skyBlockPlayer.getStash().getItemStash();
        paged.addAll(itemStash);
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return !item.getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(HypixelPlayer p, String query, int page, int maxPage) {
        // Border
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // Close button (slot 49)
        set(GUIClickableItem.getCloseItem(49));

        // Fill Inventory button (slot 48 - emerald)
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                e.setCancelled(true);

                DatapointStash.PlayerStash stash = player.getStash();
                int pickedUp = 0;

                if (stash.getItemStashCount() == 0) {
                    player.sendMessage("§cYour item stash is already empty!");
                    return;
                }

                while (player.hasEmptySlots(1) && stash.getItemStashCount() > 0) {
                    SkyBlockItem removed = stash.removeFromItemStash(0);
                    if (removed != null) {
                        player.addAndUpdateItem(removed);
                        player.sendMessage("§eFrom stash: §7" + removed.getDisplayName());
                        pickedUp++;
                    }
                }

                if (pickedUp == 0) {
                    player.sendMessage("§cCouldn't unstash your item stash! Your inventory is full!");
                } else if (stash.getItemStashCount() == 0) {
                    player.sendMessage("§eYou picked up §aall §eitems from your item stash!");
                } else {
                    player.sendMessage("§eYou still have §b" + stash.getItemStashCount() + " §eitems in there!");
                }

                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int count = player.getStash().getItemStashCount();
                return ItemStackCreator.getStack("§aFill Inventory", Material.EMERALD, 1,
                        "§7Pick up as many stashed items",
                        "§7as can fit in your inventory.",
                        "",
                        "§7Items in stash: §e" + count + "/720",
                        "",
                        "§eClick to fill inventory!");
            }
        });

        // Navigation buttons
        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }
    }

    @Override
    public String getTitle(HypixelPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        int maxPage = paged.getPageCount();
        if (maxPage <= 1) {
            return "Item Stash";
        }
        return "Item Stash (" + page + "/" + maxPage + ")";
    }

    @Override
    public GUIClickableItem createItemFor(SkyBlockItem item, int slot, HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        List<SkyBlockItem> itemStash = player.getStash().getItemStash();
        int itemIndex = itemStash.indexOf(item);

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                e.setCancelled(true);

                // Check if player has inventory space
                if (!player.hasEmptySlots(1)) {
                    player.sendMessage("§cCouldn't unstash your item stash! Your inventory is full!");
                    return;
                }

                // Find current index of this item (it may have shifted)
                List<SkyBlockItem> currentStash = player.getStash().getItemStash();
                int currentIndex = currentStash.indexOf(item);
                if (currentIndex == -1) {
                    // Item no longer in stash
                    new GUIStashItem().open(player);
                    return;
                }

                // Remove from stash and add to inventory
                SkyBlockItem removed = player.getStash().removeFromItemStash(currentIndex);
                if (removed != null) {
                    player.addAndUpdateItem(removed);
                    player.sendMessage("§eFrom stash: §7" + removed.getDisplayName());

                    // Check if stash is now empty
                    if (player.getStash().getItemStashCount() == 0) {
                        player.sendMessage("§eYou picked up §aall §eitems from your item stash!");
                    } else {
                        player.sendMessage("§eYou still have §b" + player.getStash().getItemStashCount() +
                                " §eitems in there!");
                    }
                }

                // Refresh the GUI
                new GUIStashItem().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        // No special handling needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
