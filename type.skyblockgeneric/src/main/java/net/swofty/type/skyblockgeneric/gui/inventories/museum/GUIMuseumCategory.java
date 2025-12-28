package net.swofty.type.skyblockgeneric.gui.inventories.museum;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.TrackedItem;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplays;
import net.swofty.type.skyblockgeneric.museum.MuseumableItemCategory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.ItemPriceCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GUIMuseumCategory extends HypixelPaginatedGUI<ItemType> {
    private final MuseumableItemCategory category;

    public GUIMuseumCategory(MuseumableItemCategory category) {
        super(InventoryType.CHEST_6_ROW);

        this.category = category;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);

        ItemStack item = e.getClickedItem();
        SkyBlockItem skyBlockItem = new SkyBlockItem(item);

        if (skyBlockItem.getAttributeHandler().getPotentialType() == null) {
            return;
        }

        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
        DatapointMuseum.MuseumData data = player.getMuseumData();

        if (data.getItemInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            player.sendMessage("§cYou already have a " + skyBlockItem.getDisplayName() + " §cin your Museum!");
            return;
        }

        if (data.getItemPreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
            UUID previouslyInMuseumUUID = UUID.fromString(
                    data.getItemPreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType())
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
            player.getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
            player.closeInventory();
            MuseumDisplays.updateDisplay(player);

            new GUIMuseumCategory(category).open(player);
            player.sendMessage("§aYou donated your " + skyBlockItem.getDisplayName() + " to the Museum!");
        }
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
    public PaginationList<ItemType> fillPaged(HypixelPlayer player, PaginationList<ItemType> paged) {
        paged.addAll(category.getItems());
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, ItemType item) {
        return !item.getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(HypixelPlayer p, String query, int page, int maxPage) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(createSearchItem(this, 50, query));
        set(GUIClickableItem.getGoBackItem(48, new GUIYourMuseum()));

        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }

        for (int i = 0; i < 36; i++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(i));
            if (item.getAttributeHandler().getPotentialType() == null) {
                continue;
            }

            if (category.contains(item.getAttributeHandler().getPotentialType())) {
                TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                        UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())
                );
                TrackedItem trackedItem = ((TrackedItemRetrieveProtocolObject.TrackedItemResponse) new ProxyService(ServiceType.ITEM_TRACKER).handleRequest(message).join()).trackedItem();

                ItemStack.Builder toReturn = item.getItemStackBuilder();
                List<String> lore = new ArrayList<>(item.getLore(player));
                lore.add("§8§m---------------------");
                lore.add("§7Item Created");
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add(" ");
                lore.add("§eClick to donate item!");

                player.getInventory().setItemStack(i, ItemStackCreator.updateLore(toReturn, lore)
                        .set(DataComponents.CUSTOM_NAME, Component.text(item.getDisplayName()).decoration(
                                TextDecoration.ITALIC, false
                        ))
                        .build());
            }
        }
    }

    @Override
    public String getTitle(HypixelPlayer player, String query, int page, PaginationList<ItemType> paged) {
        return "Museum -> " + category.toString();
    }

    @Override
    public GUIClickableItem createItemFor(ItemType item, int slot, HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        DatapointMuseum.MuseumData data = player.getMuseumData();
        SkyBlockItem skyBlockItem = data.getItem(category, item);
        boolean inMuseum = skyBlockItem != null;
        boolean hasTakenItOut = data.getPreviouslyInMuseum().contains(skyBlockItem);

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!inMuseum || hasTakenItOut) {
                    return;
                }

                player.sendMessage("§aYou retrieved your " + item.getDisplayName() + " from the Museum. It still counts towards your Museum progress, but not towards your total item value.");
                player.sendMessage("§aYou can return or replace the item in your Museum at any time!");

                data.moveToRetrieved(skyBlockItem);
                player.setMuseumData(data);
                MuseumDisplays.updateDisplay(player);

                player.closeInventory();
                player.addAndUpdateItem(skyBlockItem.getItemStack());
                new GUIMuseumCategory(category).open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!inMuseum) {
                    return ItemStackCreator.getStack("§c" + item.getDisplayName(),
                            Material.GRAY_DYE, 1,
                            "§7Click on this item in your inventory to",
                            "§7add it to your §9Museum§7!");
                }

                UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
                TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(trackedItemUUID);
                TrackedItem trackedItem = ((TrackedItemRetrieveProtocolObject.TrackedItemResponse) new ProxyService(ServiceType.ITEM_TRACKER)
                        .handleRequest(message).join()).trackedItem();

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
                DatapointMuseum.DisplayPlacement placement = data.getDisplayHandler().getItemDisplayPlacement(skyBlockItem);
                if (data.getCurrentlyInMuseum().contains(skyBlockItem) && placement != null) {
                    lore.add("§9" + placement.display() + " Slot #" + (placement.slot() + 1));
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
                        hasTakenItOut ? Material.LIME_DYE : item.material, 1, lore);
            }
        };
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();

        SkyBlockDataHandler.Data.INVENTORY.onLoad.accept(
                player, SkyBlockDataHandler.Data.INVENTORY.onQuit.apply(player)
        );
    }
}