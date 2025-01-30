package net.swofty.types.generic.gui.inventory.inventories.museum;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.TrackedItem;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.datapoints.DatapointMuseum;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.ItemPriceCalculator;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;

public class InventoryMuseumEmptyDisplay extends SkyBlockPaginatedInventory<SkyBlockItem> {
    private static final String STATE_EMPTY = "empty";
    private static final String STATE_HAS_ITEMS = "has_items";
    private static final String STATE_TRACKER_OFFLINE = "tracker_offline";
    private static final String STATE_TRACKER_ONLINE = "tracker_online";

    private final MuseumDisplays display;
    private final int position;

    public InventoryMuseumEmptyDisplay(MuseumDisplays display, int position) {
        super(InventoryType.CHEST_6_ROW);
        this.display = display;
        this.position = position;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Start service monitoring loop
        startLoop("service-monitor", 20, () -> {
            boolean isOnline = new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join();

            if (!isOnline) {
                if (!hasState(STATE_TRACKER_OFFLINE)) {
                    doAction(new RemoveStateAction(STATE_TRACKER_ONLINE));
                    doAction(new AddStateAction(STATE_TRACKER_OFFLINE));
                    player.sendMessage("§cThe item tracker has gone offline. Please try again later.");
                    player.closeInventory();
                }
            } else {
                if (!hasState(STATE_TRACKER_ONLINE)) {
                    doAction(new RemoveStateAction(STATE_TRACKER_OFFLINE));
                    doAction(new AddStateAction(STATE_TRACKER_ONLINE));
                }
            }
        });

        // Add offline warning message
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§cTracker Offline",
                        Material.BARRIER, 1,
                        "§7The item tracker is currently offline.",
                        "§7Please try again later.").build())
                .requireState(STATE_TRACKER_OFFLINE)
                .build());
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
    protected PaginationList<SkyBlockItem> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockItem> paged) {
        if (hasState(STATE_TRACKER_OFFLINE)) {
            return paged;
        }

        DatapointMuseum.MuseumData data = player.getMuseumData();
        List<SkyBlockItem> items = data.getInMuseumThatAreNotInDisplay();

        items.removeIf(item -> {
            MuseumableItemCategory itemCategory = MuseumableItemCategory.getFromItem(item.getAttributeHandler().getPotentialType());
            return !display.getAllowedItemCategories().contains(itemCategory);
        });

        paged.addAll(items);

        if (paged.isEmpty()) {
            doAction(new RemoveStateAction(STATE_HAS_ITEMS));
            doAction(new AddStateAction(STATE_EMPTY));
        } else {
            doAction(new RemoveStateAction(STATE_EMPTY));
            doAction(new AddStateAction(STATE_HAS_ITEMS));
        }

        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return !item.getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    protected void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Search item
        attachItem(createSearchItem(48, query));

        // Navigation buttons
        if (page > 1) {
            attachItem(createNavigationButton(45, query, page, false));
        }
        if (page < maxPage) {
            attachItem(createNavigationButton(53, query, page, true));
        }

        // Empty display message
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§a" + display.toString() + " Slot #" + (position + 1),
                        Material.BARRIER, 1,
                        "§7You don't have any displayable items",
                        "§7for this slot. Donate more items to",
                        "§7your §9Museum§7!").build())
                .requireStates(new String[]{STATE_EMPTY, STATE_TRACKER_ONLINE})
                .build());
    }

    @Override
    protected Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        return Component.text(display.toString() + " Slot #" + (position + 1) + " (" + page + "/" + paged.getPageCount() + ")");
    }

    @Override
    protected GUIItem createItemFor(SkyBlockItem item, int slot, SkyBlockPlayer player) {
        TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                item.getAttributeHandler().getUniqueTrackedID()
        );
        TrackedItem trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER).handleRequest(message).join();

        return GUIItem.builder(slot)
                .item(() -> {
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

                    return ItemStackCreator.updateLore(stack, lore).build();
                })
                .requireStates(new String[]{STATE_HAS_ITEMS, STATE_TRACKER_ONLINE})
                .onClick((ctx, clickedItem) -> {
                    ctx.player().closeInventory();
                    ctx.player().sendMessage("§aYou set " + display.toString() + " Slot #" + (position + 1) + " to display " + item.getDisplayName() + "§a!");
                    DatapointMuseum.MuseumData data = ctx.player().getMuseumData();
                    data.addToDisplay(item, display, position);
                    ctx.player().setMuseumData(data);
                    MuseumDisplays.updateDisplay(ctx.player());
                    return true;
                })
                .build();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}