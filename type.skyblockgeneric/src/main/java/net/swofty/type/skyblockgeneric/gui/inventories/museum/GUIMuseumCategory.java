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
import net.swofty.type.generic.i18n.I18n;
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
import java.util.Map;
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
            player.sendMessage(I18n.string("gui_museum.category.already_in_museum", Map.of("item_name", skyBlockItem.getDisplayName())));
            return;
        }

        if (data.getItemPreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
            UUID previouslyInMuseumUUID = UUID.fromString(
                    data.getItemPreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType())
                            .getAttributeHandler().getUniqueTrackedID()
            );

            if (!trackedItemUUID.equals(previouslyInMuseumUUID)) {
                player.sendMessage(I18n.string("gui_museum.category.can_only_readd"));
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
            player.sendMessage(I18n.string("gui_museum.category.donated", Map.of("item_name", skyBlockItem.getDisplayName())));
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
                lore.add(I18n.string("gui_museum.category.item_created_label"));
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add(" ");
                lore.add(I18n.string("gui_museum.category.click_to_donate"));

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
        return I18n.string("gui_museum.category.title", Map.of("category", category.toString()));
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

                player.sendMessage(I18n.string("gui_museum.category.retrieved_message", Map.of("item_name", item.getDisplayName())));
                player.sendMessage(I18n.string("gui_museum.category.retrieved_return_message"));

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
                    return ItemStackCreator.getStack(I18n.string("gui_museum.category.item_not_in_museum", Map.of("item_name", item.getDisplayName())),
                            Material.GRAY_DYE, 1,
                            I18n.lore("gui_museum.category.item_not_in_museum.lore"));
                }

                UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
                TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(trackedItemUUID);
                TrackedItem trackedItem = ((TrackedItemRetrieveProtocolObject.TrackedItemResponse) new ProxyService(ServiceType.ITEM_TRACKER)
                        .handleRequest(message).join()).trackedItem();

                List<String> lore = new ArrayList<>(skyBlockItem.getLore());
                lore.add("§8§m---------------------");
                lore.add(I18n.string("gui_museum.category.item_donated_label"));
                lore.add("§b" + StringUtility.formatAsDate(data.getInsertionTimes().get(trackedItemUUID)));
                lore.add(" ");
                lore.add(I18n.string("gui_museum.category.item_created_label"));
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedItem.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add(I18n.string("gui_museum.category.item_clean_value_label"));
                lore.add("§6" + StringUtility.commaify(new ItemPriceCalculator(skyBlockItem).calculateCleanPrice())
                        + " Coins");
                lore.add(" ");
                lore.add(I18n.string("gui_museum.category.item_value_label"));
                if (data.getCalculatedPrices().containsKey(trackedItemUUID)) {
                    lore.add("§6" + StringUtility.commaify(data.getCalculatedPrices().get(trackedItemUUID)) + " Coins");
                } else {
                    lore.add(I18n.string("gui_museum.category.uncalculated"));
                }
                lore.add(" ");
                lore.add(I18n.string("gui_museum.category.display_slot_label"));
                DatapointMuseum.DisplayPlacement placement = data.getDisplayHandler().getItemDisplayPlacement(skyBlockItem);
                if (data.getCurrentlyInMuseum().contains(skyBlockItem) && placement != null) {
                    lore.add("§9" + placement.display() + " Slot #" + (placement.slot() + 1));
                } else {
                    lore.add(I18n.string("gui_museum.category.not_in_display"));
                }
                if (hasTakenItOut) {
                    lore.add("§8§m---------------------");
                    lore.addAll(I18n.lore("gui_museum.category.retrieved_from_museum.lore"));
                } else {
                    lore.add(" ");
                    lore.add(I18n.string("gui_museum.category.click_to_retrieve"));
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