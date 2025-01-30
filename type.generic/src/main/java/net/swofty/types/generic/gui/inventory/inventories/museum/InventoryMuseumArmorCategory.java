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
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.ItemPriceCalculator;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryMuseumArmorCategory extends SkyBlockPaginatedInventory<ArmorSetRegistry> {
    private static final String STATE_TRACKER_OFFLINE = "tracker_offline";
    private static final String STATE_TRACKER_ONLINE = "tracker_online";
    private static final String STATE_IN_MUSEUM = "in_museum";
    private static final String STATE_NOT_IN_MUSEUM = "not_in_museum";
    private static final String STATE_TAKEN_OUT = "taken_out";
    private static final String STATE_EMPTY_SLOTS = "empty_slots";
    private static final String STATE_FULL_INVENTORY = "full_inventory";

    public InventoryMuseumArmorCategory() {
        super(InventoryType.CHEST_6_ROW);
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Start service monitoring
        startLoop("service-monitor", 20, () -> {
            boolean isOnline = new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join();
            if (!isOnline) {
                if (!hasState(STATE_TRACKER_OFFLINE)) {
                    doAction(new RemoveStateAction(STATE_TRACKER_ONLINE));
                    doAction(new AddStateAction(STATE_TRACKER_OFFLINE));
                    player.sendMessage("§cThe item tracker is currently offline. Please try again later.");
                    player.closeInventory();
                }
            } else {
                if (!hasState(STATE_TRACKER_ONLINE)) {
                    doAction(new RemoveStateAction(STATE_TRACKER_OFFLINE));
                    doAction(new AddStateAction(STATE_TRACKER_ONLINE));
                }
            }
        });

        // Monitor inventory space
        startLoop("inventory-monitor", 5, () -> {
            if (player.hasEmptySlots(4)) {
                doAction(new AddStateAction(STATE_EMPTY_SLOTS));
                doAction(new RemoveStateAction(STATE_FULL_INVENTORY));
            } else {
                doAction(new RemoveStateAction(STATE_EMPTY_SLOTS));
                doAction(new AddStateAction(STATE_FULL_INVENTORY));
            }
        });

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        updateInventoryItems(player);
    }

    private void updateInventoryItems(SkyBlockPlayer player) {
        for (int i = 0; i < 36; i++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(i));
            if (item.getAttributeHandler().getPotentialType() == null) continue;

            ArmorSetRegistry armorSet = ArmorSetRegistry.getArmorSet(item.getAttributeHandler().getPotentialType());
            if (armorSet == null || !MuseumableItemCategory.ARMOR_SETS.getItems().contains(item.getAttributeHandler().getPotentialType()))
                continue;

            updateInventoryItem(player, item, i);
        }
    }

    private void updateInventoryItem(SkyBlockPlayer player, SkyBlockItem item, int slot) {
        TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message =
                new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                        UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())
                );
        TrackedItem trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER).handleRequest(message).join();

        ItemStack.Builder toReturn = item.getItemStackBuilder();
        toReturn.set(ItemComponent.CUSTOM_DATA, item.getItemStack().get(ItemComponent.CUSTOM_DATA));

        List<String> lore = new ArrayList<>(item.getLore());
        lore.add("§8§m---------------------");
        lore.add("§7Item Created");
        lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
        lore.add(" ");
        lore.add("§eClick to donate armor set!");

        player.getInventory().setItemStack(slot, ItemStackCreator.updateLore(toReturn, lore)
                .set(ItemComponent.CUSTOM_NAME, Component.text(item.getDisplayName())
                        .decoration(TextDecoration.ITALIC, false))
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
    protected PaginationList<ArmorSetRegistry> fillPaged(SkyBlockPlayer player, PaginationList<ArmorSetRegistry> paged) {
        MuseumableItemCategory.ARMOR_SETS.getItems().forEach(item -> {
            ArmorSetRegistry armorSet = ArmorSetRegistry.getArmorSet(item);
            if (!paged.contains(armorSet)) {
                paged.add(armorSet);
            }
        });
        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, ArmorSetRegistry item) {
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

        // Search button
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
    protected Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<ArmorSetRegistry> paged) {
        return Component.text("Museum -> Armor Sets");
    }

    @Override
    protected GUIItem createItemFor(ArmorSetRegistry armorSet, int slot, SkyBlockPlayer player) {
        DatapointMuseum.MuseumData data = player.getMuseumData();

        SkyBlockItem helmet = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getHelmet());
        SkyBlockItem chestplate = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getChestplate());
        SkyBlockItem leggings = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getLeggings());
        SkyBlockItem boots = data.getItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getBoots());

        boolean inMuseum = helmet != null;
        boolean hasTakenItOut = data.getPreviouslyInMuseum().contains(helmet);

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
                        return createEmptyArmorSetItem(armorSet);
                    }
                    return createFullArmorSetItem(armorSet, data, helmet, chestplate, leggings, boots, hasTakenItOut);
                })
                .requireState(STATE_TRACKER_ONLINE)
                .onClick((ctx, clickedItem) -> {
                    if (!inMuseum || hasTakenItOut) return false;

                    if (hasState(STATE_FULL_INVENTORY)) {
                        ctx.player().sendMessage("§cYou need at least 4 empty slots in your inventory to retrieve the set back!");
                        return false;
                    }

                    handleArmorSetRetrieval(ctx.player(), armorSet, data, helmet, chestplate, leggings, boots);
                    return true;
                })
                .build();
    }

    private ItemStack createEmptyArmorSetItem(ArmorSetRegistry armorSet) {
        return ItemStackCreator.getStack("§c" + armorSet.getDisplayName(),
                Material.GRAY_DYE, 1,
                "§7Click on an armor piece in your",
                "§7inventory that belongs to this armor",
                "§7set to donate the full set to your",
                "§7Museum").build();
    }

    private ItemStack createFullArmorSetItem(ArmorSetRegistry armorSet, DatapointMuseum.MuseumData data,
                                             SkyBlockItem helmet, SkyBlockItem chestplate,
                                             SkyBlockItem leggings, SkyBlockItem boots,
                                             boolean hasTakenItOut) {
        ProxyService itemTracker = new ProxyService(ServiceType.ITEM_TRACKER);

        UUID helmetUUID = UUID.fromString(helmet.getAttributeHandler().getUniqueTrackedID());
        UUID chestplateUUID = UUID.fromString(chestplate.getAttributeHandler().getUniqueTrackedID());
        UUID leggingsUUID = UUID.fromString(leggings.getAttributeHandler().getUniqueTrackedID());
        UUID bootsUUID = UUID.fromString(boots.getAttributeHandler().getUniqueTrackedID());

        TrackedItem trackedHelmet = (TrackedItem) itemTracker.handleRequest(new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(helmetUUID)).join();
        TrackedItem trackedChestplate = (TrackedItem) itemTracker.handleRequest(new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(chestplateUUID)).join();
        TrackedItem trackedLeggings = (TrackedItem) itemTracker.handleRequest(new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(leggingsUUID)).join();
        TrackedItem trackedBoots = (TrackedItem) itemTracker.handleRequest(new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(bootsUUID)).join();

        List<String> lore = createArmorSetLore(data, helmet, chestplate, leggings, boots,
                trackedHelmet, trackedChestplate, trackedLeggings, trackedBoots,
                helmetUUID, hasTakenItOut);

        return ItemStackCreator.getStack("§a" + armorSet.getDisplayName(),
                hasTakenItOut ? Material.LIME_DYE : helmet.getMaterial(),
                1, lore).build();
    }

    private List<String> createArmorSetLore(DatapointMuseum.MuseumData data,
                                            SkyBlockItem helmet, SkyBlockItem chestplate,
                                            SkyBlockItem leggings, SkyBlockItem boots,
                                            TrackedItem trackedHelmet, TrackedItem trackedChestplate,
                                            TrackedItem trackedLeggings, TrackedItem trackedBoots,
                                            UUID helmetUUID, boolean hasTakenItOut) {
        List<String> lore = new ArrayList<>();
        int totalValue = calculateTotalValue(helmet, chestplate, leggings, boots);

        lore.add("§8§m---------------------");
        lore.add("§7Set Donated");
        lore.add("§b" + StringUtility.formatAsDate(data.getInsertionTimes().get(helmetUUID)));
        lore.add(" ");

        addPieceData(lore, "Helmet", trackedHelmet);
        addPieceData(lore, "Chestplate", trackedChestplate);
        addPieceData(lore, "Leggings", trackedLeggings);
        addPieceData(lore, "Boots", trackedBoots);

        lore.add("§7Set Clean Value");
        lore.add("§6" + StringUtility.commaify(totalValue) + " Coins");
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

        return lore;
    }

    private void addPieceData(List<String> lore, String pieceName, TrackedItem trackedItem) {
        lore.add("§7" + pieceName + " Data");
        lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
        lore.add("§6  " + StringUtility.commaifyAndTh(trackedItem.getNumberMade()) + " §7created");
        lore.add(" ");
    }

    private int calculateTotalValue(SkyBlockItem helmet, SkyBlockItem chestplate,
                                    SkyBlockItem leggings, SkyBlockItem boots) {
        return new ItemPriceCalculator(helmet).calculateCleanPrice().intValue() +
                new ItemPriceCalculator(chestplate).calculateCleanPrice().intValue() +
                new ItemPriceCalculator(leggings).calculateCleanPrice().intValue() +
                new ItemPriceCalculator(boots).calculateCleanPrice().intValue();
    }

    private void handleArmorSetRetrieval(SkyBlockPlayer player, ArmorSetRegistry armorSet,
                                         DatapointMuseum.MuseumData data,
                                         SkyBlockItem helmet, SkyBlockItem chestplate,
                                         SkyBlockItem leggings, SkyBlockItem boots) {
        player.sendMessage("§aYou retrieved your " + armorSet.getDisplayName() +
                " from the Museum. It still counts towards your Museum progress, but not towards your total item value.");
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
        player.openInventory(new InventoryMuseumArmorCategory());
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
        if (skyBlockItem.getAttributeHandler().getPotentialType() == null) {
            return;
        }

        handleArmorSetDonation((SkyBlockPlayer) event.getPlayer(), skyBlockItem, event.getSlot());
    }

    private void handleArmorSetDonation(SkyBlockPlayer player, SkyBlockItem skyBlockItem, int slot) {
        DatapointMuseum.MuseumData data = player.getMuseumData();

        if (data.getTypeInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            player.sendMessage("§cYou already have a " + skyBlockItem.getAttributeHandler().getPotentialType().getDisplayName() + " in your Museum!");
            return;
        }

        ItemType type = skyBlockItem.getAttributeHandler().getPotentialType();
        ArmorSetRegistry armorSetRegistry = ArmorSetRegistry.getArmorSet(type);
        if (armorSetRegistry == null) return;

        if (checkPreviouslyInMuseum(player, data, skyBlockItem)) return;
        processArmorSetDonation(player, data, armorSetRegistry, skyBlockItem, slot);
    }

    private boolean checkPreviouslyInMuseum(SkyBlockPlayer player, DatapointMuseum.MuseumData data, SkyBlockItem skyBlockItem) {
        if (data.getTypePreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            UUID uuidOfAlreadyInMuseum = UUID.fromString(
                    data.getTypePreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType())
                            .getAttributeHandler().getUniqueTrackedID());
            UUID uuidOfNew = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());

            if (!uuidOfAlreadyInMuseum.equals(uuidOfNew)) {
                player.sendMessage("§cYou can only re-add the set that was already in your Museum!");
                return true;
            }
        }
        return false;
    }

    private void processArmorSetDonation(SkyBlockPlayer player, DatapointMuseum.MuseumData data,
                                         ArmorSetRegistry armorSetRegistry, SkyBlockItem skyBlockItem, int slot) {
        List<ItemType> set = List.of(armorSetRegistry.getHelmet(), armorSetRegistry.getChestplate(),
                armorSetRegistry.getLeggings(), armorSetRegistry.getBoots());

        List<SkyBlockItem> itemsToTake = new ArrayList<>();
        int missing = countMissingPieces(player, set, data, itemsToTake);

        if (missing != 0) {
            player.sendMessage("§cYou are missing some of the items from the " + armorSetRegistry.getDisplayName() +
                    " set in your inventory! (" + (4 - missing) + "/4)");
            return;
        }

        donateArmorSet(player, data, armorSetRegistry, itemsToTake);
    }

    private int countMissingPieces(SkyBlockPlayer player, List<ItemType> set, DatapointMuseum.MuseumData data,
                                   List<SkyBlockItem> itemsToTake) {
        int missing = 0;
        for (ItemType setItem : set) {
            Map<Integer, Integer> allOfTypeInInventory = player.getAllOfTypeInInventory(setItem);
            boolean hasFoundItem = false;

            for (Map.Entry<Integer, Integer> entry : allOfTypeInInventory.entrySet()) {
                SkyBlockItem potentialItem = new SkyBlockItem(player.getInventory().getItemStack(entry.getKey()));

                if (data.getTypePreviouslyInMuseum(potentialItem.getAttributeHandler().getPotentialType()) != null) {
                    UUID uuidOfPotentialItem = UUID.fromString(potentialItem.getAttributeHandler().getUniqueTrackedID());
                    UUID uuidOfAlreadyInMuseum = UUID.fromString(
                            data.getTypePreviouslyInMuseum(potentialItem.getAttributeHandler().getPotentialType())
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

            if (!hasFoundItem) missing++;
        }
        return missing;
    }

    private void donateArmorSet(SkyBlockPlayer player, DatapointMuseum.MuseumData data,
                                ArmorSetRegistry armorSetRegistry, List<SkyBlockItem> itemsToTake) {
        for (SkyBlockItem itemToTake : itemsToTake) {
            player.takeItem(itemToTake);
            itemToTake.getAttributeHandler().setSoulBound(true);
            data.add(itemToTake);
        }

        player.setMuseumData(data);
        player.closeInventory();
        MuseumDisplays.updateDisplay(player);

        player.openInventory(new InventoryMuseumArmorCategory());
        player.sendMessage("§aYou donated your " + armorSetRegistry.getDisplayName() + " Set to the Museum!");
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}