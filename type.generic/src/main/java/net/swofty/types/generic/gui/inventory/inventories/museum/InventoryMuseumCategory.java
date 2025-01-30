package net.swofty.types.generic.gui.inventory.inventories.museum;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.TrackedItem;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMuseum;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.ItemPriceCalculator;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryMuseumCategory extends SkyBlockPaginatedInventory<ItemType> {
    private static final String STATE_TRACKER_OFFLINE = "tracker_offline";
    private static final String STATE_TRACKER_ONLINE = "tracker_online";
    private static final String STATE_IN_MUSEUM = "in_museum";
    private static final String STATE_NOT_IN_MUSEUM = "not_in_museum";
    private static final String STATE_TAKEN_OUT = "taken_out";

    private final MuseumableItemCategory category;

    public InventoryMuseumCategory(MuseumableItemCategory category) {
        super(InventoryType.CHEST_6_ROW);
        this.category = category;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Start service monitoring
        startLoop("service-monitor", 20, () -> {
            boolean isOnline = new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join();
            if (!isOnline) {
                if (!hasState(STATE_TRACKER_OFFLINE)) {
                    doAction(new AddStateAction(STATE_TRACKER_OFFLINE));
                    player.sendMessage("§cThe item tracker is currently offline. Please try again later.");
                    player.closeInventory();
                }
            } else {
                if (!hasState(STATE_TRACKER_ONLINE)) {
                    doAction(new AddStateAction(STATE_TRACKER_ONLINE));
                }
            }
        });

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Update inventory items with museum info
        updateInventoryItems(player);
    }

    private void updateInventoryItems(SkyBlockPlayer player) {
        for (int i = 0; i < 36; i++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(i));
            if (item.getAttributeHandler().getPotentialType() == null) continue;

            if (category.contains(item.getAttributeHandler().getPotentialType())) {
                TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                        UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())
                );
                TrackedItem trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER).handleRequest(message).join();

                ItemStack.Builder toReturn = item.getItemStackBuilder();
                List<String> lore = new ArrayList<>(item.getLore(player));
                lore.add("§8§m---------------------");
                lore.add("§7Item Created");
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add(" ");
                lore.add("§eClick to donate item!");

                player.getInventory().setItemStack(i, ItemStackCreator.updateLore(toReturn, lore)
                        .set(ItemComponent.CUSTOM_NAME, Component.text(item.getDisplayName())
                                .decoration(TextDecoration.ITALIC, false))
                        .build());
            }
        }
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
    protected PaginationList<ItemType> fillPaged(SkyBlockPlayer player, PaginationList<ItemType> paged) {
        paged.addAll(category.getItems());
        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, ItemType item) {
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

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Your Museum").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIYourMuseum());
                    return true;
                })
                .build());

        // Search item
        attachItem(createSearchItem(50, query));

        // Navigation buttons
        if (page > 1) {
            attachItem(createNavigationButton(45, query, page, false));
        }
        if (page < maxPage) {
            attachItem(createNavigationButton(53, query, page, true));
        }
    }

    @Override
    protected Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<ItemType> paged) {
        return Component.text("Museum -> " + category.toString());
    }

    @Override
    protected GUIItem createItemFor(ItemType item, int slot, SkyBlockPlayer player) {
        DatapointMuseum.MuseumData data = player.getMuseumData();
        SkyBlockItem skyBlockItem = data.getItem(category, item);
        boolean inMuseum = skyBlockItem != null;
        boolean hasTakenItOut = data.getPreviouslyInMuseum().contains(skyBlockItem);

        if (inMuseum) {
            doAction(new AddStateAction(STATE_IN_MUSEUM + "_" + slot));
        } else {
            doAction(new AddStateAction(STATE_NOT_IN_MUSEUM + "_" + slot));
        }

        if (hasTakenItOut) {
            doAction(new AddStateAction(STATE_TAKEN_OUT + "_" + slot));
        }

        return GUIItem.builder(slot)
                .item(() -> {
                    if (!inMuseum) {
                        return ItemStackCreator.getStack("§c" + item.getDisplayName(),
                                Material.GRAY_DYE, 1,
                                "§7Click on this item in your inventory to",
                                "§7add it to your §9Museum§7!").build();
                    }

                    return createMuseumItemStack(skyBlockItem, data, hasTakenItOut, item);
                })
                .requireState(STATE_TRACKER_ONLINE)
                .onClick((ctx, clickedItem) -> {
                    if (!inMuseum || hasTakenItOut) return false;

                    handleItemRetrieval(player, item, skyBlockItem, data);
                    return true;
                })
                .build();
    }

    private ItemStack createMuseumItemStack(SkyBlockItem skyBlockItem, DatapointMuseum.MuseumData data,
                                            boolean hasTakenItOut, ItemType item) {
        UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
        TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(trackedItemUUID);
        TrackedItem trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER)
                .handleRequest(message).join();

        List<String> lore = new ArrayList<>(skyBlockItem.getLore());
        lore.add("§8§m---------------------");
        lore.add("§7Item Donated");
        lore.add("§b" + StringUtility.formatAsDate(data.getInsertionTimes().get(trackedItemUUID)));
        lore.add(" ");
        lore.add("§7Item Created");
        lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
        lore.add("§6  " + StringUtility.commaifyAndTh(trackedItem.getNumberMade()) + " §7created");
        lore.add(" ");
        lore.add("§7Item Clean Value");
        lore.add("§6" + StringUtility.commaify(new ItemPriceCalculator(skyBlockItem).calculateCleanPrice())
                + " Coins");
        lore.add(" ");
        lore.add("§7Item Value");
        if (data.getCalculatedPrices().containsKey(trackedItemUUID)) {
            lore.add("§6" + StringUtility.commaify(data.getCalculatedPrices().get(trackedItemUUID)) + " Coins");
        } else {
            lore.add("§cUncalculated");
        }
        lore.add(" ");
        lore.add("§7Display Slot");
        if (data.getCurrentlyInMuseum().contains(skyBlockItem)
                && data.getMuseumDisplay().get(trackedItemUUID) != null) {
            lore.add("§9" + data.getMuseumDisplay().get(trackedItemUUID).getKey()
                    + " Slot #" + (data.getMuseumDisplay().get(trackedItemUUID).getValue() + 1));
        } else {
            lore.add("§cNot In Display");
        }
        if (hasTakenItOut) {
            lore.add("§8§m---------------------");
            lore.add("§7You have retrieved this from your");
            lore.add("§7Museum but can add it back at any");
            lore.add("§7time.");
        } else {
            lore.add(" ");
            lore.add("§eClick to retrieve item!");
        }

        return ItemStackCreator.getStack("§a" + item.getDisplayName(),
                hasTakenItOut ? Material.LIME_DYE : item.material, 1, lore).build();
    }

    private void handleItemRetrieval(SkyBlockPlayer player, ItemType item, SkyBlockItem skyBlockItem,
                                     DatapointMuseum.MuseumData data) {
        player.sendMessage("§aYou retrieved your " + item.getDisplayName() +
                " from the Museum. It still counts towards your Museum progress, but not towards your total item value.");
        player.sendMessage("§aYou can return or replace the item in your Museum at any time!");

        data.getPreviouslyInMuseum().add(skyBlockItem);
        data.getCurrentlyInMuseum().remove(skyBlockItem);
        data.getMuseumDisplay().remove(UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID()));
        player.setMuseumData(data);
        MuseumDisplays.updateDisplay(player);

        player.closeInventory();
        player.addAndUpdateItem(skyBlockItem.getItemStack());
        player.openInventory(new InventoryMuseumCategory(category));
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        DataHandler.Data.INVENTORY.onLoad.accept(
                (SkyBlockPlayer) event.getPlayer(),
                DataHandler.Data.INVENTORY.onQuit.apply((SkyBlockPlayer) event.getPlayer())
        );
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);

        ItemStack item = event.getClickedItem();
        SkyBlockItem skyBlockItem = new SkyBlockItem(item);
        if (skyBlockItem.getAttributeHandler().getPotentialType() == null) return;

        handleBottomClickItem((SkyBlockPlayer) event.getPlayer(), skyBlockItem, event.getSlot());
    }

    private void handleBottomClickItem(SkyBlockPlayer player, SkyBlockItem skyBlockItem, int slot) {
        DatapointMuseum.MuseumData data = player.getMuseumData();

        if (data.getTypeInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            player.sendMessage("§cYou already have a " + skyBlockItem.getDisplayName() + " in your Museum!");
            return;
        }

        if (data.getTypePreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
            UUID previouslyInMuseumUUID = UUID.fromString(
                    data.getTypePreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType())
                            .getAttributeHandler().getUniqueTrackedID()
            );

            if (!trackedItemUUID.equals(previouslyInMuseumUUID)) {
                player.sendMessage("§cYou can only re-add the item that was already in your Museum!");
                return;
            }
        }

        if (category.contains(skyBlockItem.getAttributeHandler().getPotentialType())) {
            skyBlockItem.getAttributeHandler().setSoulBound(true);
            data.add(skyBlockItem);
            player.setMuseumData(data);
            player.getInventory().setItemStack(slot, ItemStack.AIR);
            player.closeInventory();
            MuseumDisplays.updateDisplay(player);

            player.openInventory(new InventoryMuseumCategory(category));
            player.sendMessage("§aYou donated your " + skyBlockItem.getDisplayName() + " to the Museum!");
        }
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}