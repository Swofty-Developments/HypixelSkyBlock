package net.swofty.types.generic.gui.inventory.inventories.museum;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.TrackedItem;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.datapoints.DatapointMuseum;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.protocol.itemtracker.ProtocolGetTrackedItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.ItemPriceCalculator;
import net.swofty.types.generic.utility.PaginationList;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIMuseumEmptyDisplay extends SkyBlockPaginatedGUI<SkyBlockItem> {
    private final MuseumDisplays display;
    private final int position;

    public GUIMuseumEmptyDisplay(MuseumDisplays display, int position) {
        super(InventoryType.CHEST_6_ROW);

        this.display = display;
        this.position = position;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
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
    public PaginationList<SkyBlockItem> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockItem> paged) {
        DatapointMuseum.MuseumData data = player.getMuseumData();
        List<SkyBlockItem> items = data.getInMuseumThatAreNotInDisplay();

        items.removeIf(item -> {
            MuseumableItemCategory itemCategory = MuseumableItemCategory.getFromItem(item.getAttributeHandler().getItemTypeAsType());
            return !display.getAllowedItemCategories().contains(itemCategory);
        });

        paged.addAll(items);

        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return item.getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        if (!new ProxyService(ServiceType.ITEM_TRACKER).isOnline(new ProtocolPingSpecification()).join()) {
            player.sendMessage("§cThe item tracker is currently offline. Please try again later.");
            player.closeInventory();
            return;
        }

        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(createSearchItem(this, 48, query));

        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }

        if (maxPage == 0) {
            // GUI is empty
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§a" + display.toString() + " Slot #" + (position + 1),
                            Material.BARRIER, 1,
                            "§7You don't have any displayable items",
                            "§7for this slot. Donate more items to",
                            "§7your §9Museum§7!");
                }
            });
        }
    }

    @Override
    public String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        return display.toString() + " Slot #" + (position + 1) + " (" + page + "/" + paged.getPageCount() + ")";
    }

    @Override
    public GUIClickableItem createItemFor(SkyBlockItem item, int slot, SkyBlockPlayer player) {
        TrackedItem trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER).callEndpoint(
                new ProtocolGetTrackedItem(),
                Map.of("item-uuid", item.getAttributeHandler().getUniqueTrackedID()
        )).join().get("tracked-item");

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
                player.sendMessage("§aYou set " + display.toString() + " Slot #" + (position + 1) + " to display " + item.getDisplayName() + "§a!");
                DatapointMuseum.MuseumData data = player.getMuseumData();
                data.addToDisplay(item, display, position);
                player.setMuseumData(data);
                MuseumDisplays.updateDisplay(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                ItemStack.Builder stack = new NonPlayerItemUpdater(item).getUpdatedItem();
                ArrayList<String> lore = new ArrayList<>(item.getLore());

                lore.add("§8§m---------------------");
                lore.add("§7Item Created");
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedItem.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add("§7Item Clean Value");
                lore.add("§6" + StringUtility.commaify(new ItemPriceCalculator(item).calculateCleanPrice()) + " Coins");
                lore.add(" ");
                lore.add("§7Item Value");
                if (data.getCalculatedPrices().containsKey(item.getAttributeHandler().getUniqueTrackedID())) {
                    lore.add("§6" + StringUtility.commaify(data.getCalculatedPrices().get(item.getAttributeHandler().getUniqueTrackedID())) + " Coins");
                } else {
                    lore.add("§cUncalculated");
                }
                lore.add(" ");
                lore.add("§eClick to display!");

                return ItemStackCreator.updateLore(stack, lore);
            }
        };
    }
}
