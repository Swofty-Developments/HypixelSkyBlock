package net.swofty.types.generic.gui.inventory.inventories.museum;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.TrackedItem;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMuseum;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.protocol.itemtracker.ProtocolGetTrackedItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.ItemPriceCalculator;
import net.swofty.types.generic.utility.PaginationList;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GUIMuseumArmorCategory extends SkyBlockPaginatedGUI<ArmorSetRegistry> {
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

        if (skyBlockItem.getAttributeHandler().getItemTypeAsType() == null) {
            return;
        }

        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
        DatapointMuseum.MuseumData data = player.getMuseumData();

        if (data.getTypeInMuseum(skyBlockItem.getAttributeHandler().getItemTypeAsType()) != null) {
            player.sendMessage("§cYou already have a " + skyBlockItem.getAttributeHandler().getItemTypeAsType().getDisplayName(null) + " in your Museum!");
            return;
        }

        if (skyBlockItem.getGenericInstance() == null)
            return;
        ItemType type = skyBlockItem.getAttributeHandler().getItemTypeAsType();
        ArmorSetRegistry armorSetRegistry = ArmorSetRegistry.getArmorSet(type);
        if (armorSetRegistry == null)
            return;

        boolean hasTakenItOut = data.getTypePreviouslyInMuseum(skyBlockItem.getAttributeHandler().getItemTypeAsType()) != null;
        if (hasTakenItOut) {
            UUID uuidOfAlreadyInMuseum = UUID.fromString(
                    data.getTypePreviouslyInMuseum(skyBlockItem.getAttributeHandler().getItemTypeAsType())
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
                                data.getTypePreviouslyInMuseum(potentialItem.getAttributeHandler().getItemTypeAsType())
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
    public PaginationList<ArmorSetRegistry> fillPaged(SkyBlockPlayer player, PaginationList<ArmorSetRegistry> paged) {
        MuseumableItemCategory.ARMOR_SETS.getItems().forEach(item -> {
            ArmorSetRegistry armorSet = ArmorSetRegistry.getArmorSet(item);
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
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
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
            if (item.getGenericInstance() == null) {
                continue;
            }

            if (ArmorSetRegistry.getArmorSet(item.getAttributeHandler().getItemTypeAsType()) == null)
                continue;

            if (armorSets.contains(ArmorSetRegistry.getArmorSet(item.getAttributeHandler().getItemTypeAsType()))) {
                TrackedItem trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER)
                        .callEndpoint(new ProtocolGetTrackedItem(),
                                Map.of("item-uuid", UUID.fromString(item.getAttributeHandler().getUniqueTrackedID()))
                        ).join().get("tracked-item");

                ItemStack.Builder toReturn = item.getItemStackBuilder();
                toReturn.meta(item.getItemStack().meta());

                List<String> lore = new ArrayList<>(item.getLore());
                lore.add("§8§m---------------------");
                lore.add("§7Item Created");
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add(" ");
                lore.add("§eClick to donate armor set!");

                player.getInventory().setItemStack(i, ItemStackCreator.updateLore(toReturn, lore)
                        .displayName(Component.text(item.getDisplayName()).decoration(
                                TextDecoration.ITALIC, false
                        ))
                        .build());
            }
        }
    }

    @Override
    public String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<ArmorSetRegistry> paged) {
        return "Museum -> Armor Sets";
    }

    @Override
    public GUIClickableItem createItemFor(ArmorSetRegistry armorSet, int slot, SkyBlockPlayer player) {
        DatapointMuseum.MuseumData data = player.getMuseumData();

        SkyBlockItem helmet = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getHelmet());
        SkyBlockItem chestplate = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getChestplate());
        SkyBlockItem leggings = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getLeggings());
        SkyBlockItem boots = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getBoots());

        boolean inMuseum = helmet != null;
        boolean hasTakenItOut = data.getPreviouslyInMuseum().contains(helmet);

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
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
                    data.getPreviouslyInMuseum().add(item);
                    data.getCurrentlyInMuseum().remove(item);
                    data.getMuseumDisplay().remove(UUID.fromString(item.getAttributeHandler().getUniqueTrackedID()));
                    player.addAndUpdateItem(item);
                });

                player.setMuseumData(data);
                MuseumDisplays.updateDisplay(player);

                player.closeInventory();
                new GUIMuseumArmorCategory().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!inMuseum) {
                    return ItemStackCreator.getStack("§c" + armorSet.getDisplayName(),
                            Material.GRAY_DYE, 1,
                            "§7Click on an armor piece in your",
                            "§7inventory that belongs to this armor",
                            "§7set to donate the full set to your",
                            "§7Museum");
                }

                UUID helmetUUID = UUID.fromString(helmet.getAttributeHandler().getUniqueTrackedID());

                TrackedItem trackedHelmet = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER)
                        .callEndpoint(new ProtocolGetTrackedItem(),
                                Map.of("item-uuid", UUID.fromString(helmet.getAttributeHandler().getUniqueTrackedID())))
                        .join().get("tracked-item");
                TrackedItem trackedChestplate = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER)
                        .callEndpoint(new ProtocolGetTrackedItem(),
                                Map.of("item-uuid", UUID.fromString(chestplate.getAttributeHandler().getUniqueTrackedID())))
                        .join().get("tracked-item");
                TrackedItem trackedLeggings = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER)
                        .callEndpoint(new ProtocolGetTrackedItem(),
                                Map.of("item-uuid", UUID.fromString(leggings.getAttributeHandler().getUniqueTrackedID())))
                        .join().get("tracked-item");
                TrackedItem trackedBoots = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER)
                        .callEndpoint(new ProtocolGetTrackedItem(),
                                Map.of("item-uuid", UUID.fromString(boots.getAttributeHandler().getUniqueTrackedID())))
                        .join().get("tracked-item");

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

        DataHandler.Data.INVENTORY.onLoad.accept(
                player, DataHandler.Data.INVENTORY.onQuit.apply(player)
        );
    }
}
