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
import net.swofty.type.skyblockgeneric.item.set.ArmorSetRegistry;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplays;
import net.swofty.type.skyblockgeneric.museum.MuseumableItemCategory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.ItemPriceCalculator;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
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
            player.sendMessage("§cYou already have a " + skyBlockItem.getAttributeHandler().getPotentialType().getDisplayName() + " in your Museum!");
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
                player.sendMessage("§cYou can only re-add the set that was already in your Museum!");
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
                player.sendMessage("§cYou are missing some of the items from the " + armorSetRegistry.getDisplayName() + " set in your inventory! (" + (4 - missing) + "/4)");
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
            player.sendMessage("§aYou donated your " + armorSetRegistry.getDisplayName() + " Set to the Museum!");
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
                lore.add("§7Item Created");
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add(" ");
                lore.add("§eClick to donate armor set!");

                player.getInventory().setItemStack(i, ItemStackCreator.updateLore(toReturn, lore)
                        .set(DataComponents.CUSTOM_NAME, Component.text(item.getDisplayName()).decoration(
                                TextDecoration.ITALIC, false
                        )).build());
            }
        }
    }

    @Override
    public String getTitle(HypixelPlayer player, String query, int page, PaginationList<ArmorSetRegistry> paged) {
        return "Museum -> Armor Sets";
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
                    player.sendMessage("§cYou need at least 4 empty slots in your inventory to retrieve the set back!");
                    return;
                }

                player.sendMessage("§aYou retrieved your " + armorSet.getDisplayName() + " from the Museum. It still counts towards your Museum progress, but not towards your total item value.");
                player.sendMessage("§aYou can return or replace the set in your Museum at any time!");

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
                    return ItemStackCreator.getStack("§c" + armorSet.getDisplayName(),
                            Material.GRAY_DYE, 1,
                            "§7Click on an armor piece in your",
                            "§7inventory that belongs to this armor",
                            "§7set to donate the full set to your",
                            "§7Museum");
                }

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

                List<String> lore = new ArrayList<>();
                lore.add("§8§m---------------------");
                lore.add("§7Set Donated");
                lore.add("§b" + StringUtility.formatAsDate(data.getInsertionTimes().get(helmetUUID)));
                lore.add(" ");
                lore.add("§7Helmet Data");
                lore.add("§a" + StringUtility.formatAsDate(trackedHelmet.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedHelmet.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add("§7Chestplate Data");
                lore.add("§a" + StringUtility.formatAsDate(trackedChestplate.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedChestplate.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add("§7Leggings Data");
                lore.add("§a" + StringUtility.formatAsDate(trackedLeggings.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedLeggings.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add("§7Boots Data");
                lore.add("§a" + StringUtility.formatAsDate(trackedBoots.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedBoots.getNumberMade()) + " §7created");
                lore.add(" ");
                lore.add("§7Set Clean Value");
                lore.add("§6" + StringUtility.commaify(helmetValue + chestplateValue + leggingsValue + bootsValue)
                        + " Coins");
                lore.add(" ");
                lore.add("§7Set Value");
                if (data.getCalculatedPrices().containsKey(helmetUUID)) {
                    lore.add("§6" + StringUtility.commaify(data.getCalculatedPrices().get(helmetUUID)) + " Coins");
                } else {
                    lore.add("§cUncalculated");
                }

                if (hasTakenItOut) {
                    lore.add("§8§m---------------------");
                    lore.add("§7You have retrieved this from your");
                    lore.add("§7Museum but can add it back at any");
                    lore.add("§7time.");
                } else {
                    lore.add(" ");
                    lore.add("§eClick to retrieve set!");
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