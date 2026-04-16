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
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import net.swofty.commons.skyblock.item.ItemType;
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
import net.swofty.type.skyblockgeneric.item.set.ArmorSetRegistry;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplays;
import net.swofty.type.skyblockgeneric.museum.MuseumableItemCategory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.ItemPriceCalculator;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GUIMuseumArmorCategory extends HypixelPaginatedGUI<ArmorSetRegistry> {
    public GUIMuseumArmorCategory() {
        super(InventoryType.CHEST_6_ROW);
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
            player.sendMessage(I18n.t("gui_museum.armor_category.already_in_museum", Component.text(skyBlockItem.getAttributeHandler().getPotentialType().getDisplayName())));
            return;
        }

        ItemType type = skyBlockItem.getAttributeHandler().getPotentialType();
        ArmorSetRegistry armorSetRegistry = ArmorSetRegistry.getArmorSet(type);
        if (armorSetRegistry == null)
            return;

        boolean hasTakenItOut = data.getItemPreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null;
        if (hasTakenItOut) {
            UUID uuidOfAlreadyInMuseum = UUID.fromString(
                    data.getItemPreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType())
                            .getAttributeHandler().getUniqueTrackedID());
            UUID uuidOfNew = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());

            if (!uuidOfAlreadyInMuseum.equals(uuidOfNew)) {
                player.sendMessage(I18n.t("gui_museum.armor_category.can_only_read"));
                return;
            }
        }

        List<ArmorSetRegistry> armorSets = new ArrayList<>();
        MuseumableItemCategory.ARMOR_SETS.getItems().forEach(armorSetItem -> {
            armorSets.add(ArmorSetRegistry.getArmorSet(armorSetItem));
        });

        if (armorSets.contains(armorSetRegistry)) {
            ItemType helmet = armorSetRegistry.getHelmet();
            ItemType chestplate = armorSetRegistry.getChestplate();
            ItemType leggings = armorSetRegistry.getLeggings();
            ItemType boots = armorSetRegistry.getBoots();

            int missing = 0;
            List<ItemType> set = List.of(helmet, chestplate, leggings, boots);
            List<SkyBlockItem> itemsToTake = new ArrayList<>();
            for (ItemType setItem : set) {
                Map<Integer, Integer> allOfTypeInInventory = player.getAllOfTypeInInventory(setItem);

                boolean hasFoundItem = false;

                for (Map.Entry<Integer, Integer> entry : allOfTypeInInventory.entrySet()) {
                    SkyBlockItem potentialItem = new SkyBlockItem(player.getInventory().getItemStack(entry.getKey()));

                    if (hasTakenItOut) {
                        // Make sure that the item is the same as the one that was taken out
                        UUID uuidOfPotentialItem = UUID.fromString(potentialItem.getAttributeHandler().getUniqueTrackedID());
                        UUID uuidOfAlreadyInMuseum = UUID.fromString(
                                data.getItemPreviouslyInMuseum(potentialItem.getAttributeHandler().getPotentialType())
                                        .getAttributeHandler().getUniqueTrackedID());

                        if (uuidOfPotentialItem.equals(uuidOfAlreadyInMuseum)) {
                            hasFoundItem = true;
                            itemsToTake.add(potentialItem);
                            break;
                        }
                    } else {
                        hasFoundItem = true;
                        itemsToTake.add(potentialItem);
                        break;
                    }
                }

                if (!hasFoundItem)
                    missing++;
            }

            if (missing != 0) {
                player.sendMessage(I18n.t("gui_museum.armor_category.missing_items", Component.text(armorSetRegistry.getDisplayName()), Component.text(String.valueOf(4 - missing))));
                return;
            }

            for (SkyBlockItem itemToTake : itemsToTake) {
                player.takeItem(itemToTake);
                itemToTake.getAttributeHandler().setSoulBound(true);
                data.add(itemToTake);
            }

            player.setMuseumData(data);
            player.closeInventory();
            MuseumDisplays.updateDisplay(player);

            new GUIMuseumArmorCategory().open(player);
            player.sendMessage(I18n.t("gui_museum.armor_category.donated", Component.text(armorSetRegistry.getDisplayName())));
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
    public PaginationList<ArmorSetRegistry> fillPaged(HypixelPlayer player, PaginationList<ArmorSetRegistry> paged) {
        MuseumableItemCategory.ARMOR_SETS.getItems().forEach(item -> {
            ArmorSetRegistry armorSet = ArmorSetRegistry.getArmorSet(item);
            if (armorSet == null) {
                Logger.error("ArmorSetRegistry is null! Add the armor set of " + item.getDisplayName() + " to the registry!");
            }
            if (paged.contains(armorSet))
                return;
            paged.add(armorSet);
        });
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, ArmorSetRegistry item) {
        return !item.getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(HypixelPlayer player, String query, int page, int maxPage) {
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

        List<ArmorSetRegistry> armorSets = new ArrayList<>();
        MuseumableItemCategory.ARMOR_SETS.getItems().forEach(item -> {
            armorSets.add(ArmorSetRegistry.getArmorSet(item));
        });

        for (int i = 0; i < 36; i++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(i));
            if (item.getAttributeHandler().getPotentialType() == null) {
                continue;
            }

            if (ArmorSetRegistry.getArmorSet(item.getAttributeHandler().getPotentialType()) == null)
                continue;

            if (armorSets.contains(ArmorSetRegistry.getArmorSet(item.getAttributeHandler().getPotentialType()))) {
                TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                        UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())
                );
                ProxyService proxyService = new ProxyService(ServiceType.ITEM_TRACKER);
                TrackedItemRetrieveProtocolObject.TrackedItemResponse trackedItemResponse = (TrackedItemRetrieveProtocolObject.TrackedItemResponse) proxyService.handleRequest(message).join();
                TrackedItem trackedItem = trackedItemResponse.trackedItem();

                ItemStack.Builder toReturn = item.getItemStackBuilder();
                toReturn.set(DataComponents.CUSTOM_DATA, item.getItemStack().get(DataComponents.CUSTOM_DATA));

                List<String> lore = new ArrayList<>(item.getLore());
                lore.add("§8§m---------------------");
                lore.add(I18n.string("gui_museum.category.item_created_label", player.getLocale()));
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add(" ");
                lore.add(I18n.string("gui_museum.armor_category.click_to_donate_armor", player.getLocale()));

                player.getInventory().setItemStack(i, ItemStackCreator.updateLore(toReturn, lore)
                        .set(DataComponents.CUSTOM_NAME, Component.text(item.getDisplayName()).decoration(
                                TextDecoration.ITALIC, false
                        )).build());
            }
        }
    }

    @Override
    public String getTitle(HypixelPlayer player, String query, int page, PaginationList<ArmorSetRegistry> paged) {
        return I18n.string("gui_museum.armor_category.title", player.getLocale());
    }

    @Override
    public GUIClickableItem createItemFor(ArmorSetRegistry armorSet, int slot, HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        DatapointMuseum.MuseumData data = player.getMuseumData();

        SkyBlockItem helmet = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getHelmet());
        SkyBlockItem chestplate = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getChestplate());
        SkyBlockItem leggings = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getLeggings());
        SkyBlockItem boots = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getBoots());

        boolean inMuseum = helmet != null;
        boolean hasTakenItOut = data.getPreviouslyInMuseum().contains(helmet);

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!inMuseum || hasTakenItOut) {
                    return;
                }

                if (!player.hasEmptySlots(4)) {
                    player.sendMessage(I18n.t("gui_museum.armor_category.need_empty_slots"));
                    return;
                }

                player.sendMessage(I18n.t("gui_museum.armor_category.retrieved_message", Component.text(armorSet.getDisplayName())));
                player.sendMessage(I18n.t("gui_museum.armor_category.retrieved_return_message"));

                List<SkyBlockItem> set = List.of(helmet, chestplate, leggings, boots);
                set.forEach(item -> {
                    data.moveToRetrieved(item);
                    player.addAndUpdateItem(item);
                });

                player.setMuseumData(data);
                MuseumDisplays.updateDisplay(player);

                player.closeInventory();
                new GUIMuseumArmorCategory().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!inMuseum) {
                    Locale l = player.getLocale();
                    return ItemStackCreator.getStack(I18n.string("gui_museum.armor_category.not_in_museum", l, Component.text(armorSet.getDisplayName())),
                            Material.GRAY_DYE, 1,
                        I18n.iterable("gui_museum.armor_category.not_in_museum.lore"));
                }

                Locale l = player.getLocale();
                ProxyService itemTracker = new ProxyService(ServiceType.ITEM_TRACKER);
                UUID helmetUUID = UUID.fromString(helmet.getAttributeHandler().getUniqueTrackedID());
                UUID chestplateUUID = UUID.fromString(chestplate.getAttributeHandler().getUniqueTrackedID());
                UUID leggingsUUID = UUID.fromString(leggings.getAttributeHandler().getUniqueTrackedID());
                UUID bootsUUID = UUID.fromString(boots.getAttributeHandler().getUniqueTrackedID());

                TrackedItemRetrieveProtocolObject.TrackedItemResponse helmetResponse = (TrackedItemRetrieveProtocolObject.TrackedItemResponse) itemTracker.handleRequest(new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(helmetUUID)).join();
                TrackedItem trackedHelmet = helmetResponse.trackedItem();
                TrackedItemRetrieveProtocolObject.TrackedItemResponse chestplateResponse = (TrackedItemRetrieveProtocolObject.TrackedItemResponse) itemTracker.handleRequest(new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(chestplateUUID)).join();
                TrackedItem trackedChestplate = chestplateResponse.trackedItem();
                TrackedItemRetrieveProtocolObject.TrackedItemResponse leggingsResponse = (TrackedItemRetrieveProtocolObject.TrackedItemResponse) itemTracker.handleRequest(new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(leggingsUUID)).join();
                TrackedItem trackedLeggings = leggingsResponse.trackedItem();
                TrackedItemRetrieveProtocolObject.TrackedItemResponse bootsResponse = (TrackedItemRetrieveProtocolObject.TrackedItemResponse) itemTracker.handleRequest(new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(bootsUUID)).join();
                TrackedItem trackedBoots = bootsResponse.trackedItem();

                int helmetValue = new ItemPriceCalculator(helmet).calculateCleanPrice().intValue();
                int chestplateValue = new ItemPriceCalculator(chestplate).calculateCleanPrice().intValue();
                int leggingsValue = new ItemPriceCalculator(leggings).calculateCleanPrice().intValue();
                int bootsValue = new ItemPriceCalculator(boots).calculateCleanPrice().intValue();

                List<Object> lore = new ArrayList<>();
                lore.add("§8§m---------------------");
                lore.add(I18n.string("gui_museum.armor_category.set_donated_label", l));
                lore.add("§b" + StringUtility.formatAsDate(data.getInsertionTimes().get(helmetUUID)));
                lore.add(" ");
                lore.add(I18n.string("gui_museum.armor_category.helmet_data", l));
                lore.add("§a" + StringUtility.formatAsDate(trackedHelmet.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedHelmet.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add(I18n.string("gui_museum.armor_category.chestplate_data", l));
                lore.add("§a" + StringUtility.formatAsDate(trackedChestplate.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedChestplate.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add(I18n.string("gui_museum.armor_category.leggings_data", l));
                lore.add("§a" + StringUtility.formatAsDate(trackedLeggings.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedLeggings.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add(I18n.string("gui_museum.armor_category.boots_data", l));
                lore.add("§a" + StringUtility.formatAsDate(trackedBoots.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedBoots.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add(I18n.string("gui_museum.armor_category.set_clean_value_label", l));
                lore.add("§6" + StringUtility.commaify(helmetValue + chestplateValue + leggingsValue + bootsValue)
                        + " Coins");
                lore.add(" ");
                lore.add(I18n.string("gui_museum.armor_category.set_value_label", l));
                if (data.getCalculatedPrices().containsKey(helmetUUID)) {
                    lore.add("§6" + StringUtility.commaify(data.getCalculatedPrices().get(helmetUUID)) + " Coins");
                } else {
                    lore.add(I18n.string("gui_museum.category.uncalculated", l));
                }

                if (hasTakenItOut) {
                    lore.add("§8§m---------------------");
                    lore.addAll(List.of(I18n.iterable("gui_museum.armor_category.retrieved_from_museum.lore")));
                } else {
                    lore.add(" ");
                    lore.add(I18n.string("gui_museum.armor_category.click_to_retrieve", l));
                }

                return ItemStackCreator.getStack("§a" + armorSet.getDisplayName(),
                        hasTakenItOut ? Material.LIME_DYE : helmet.getMaterial(), 1, lore);
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
