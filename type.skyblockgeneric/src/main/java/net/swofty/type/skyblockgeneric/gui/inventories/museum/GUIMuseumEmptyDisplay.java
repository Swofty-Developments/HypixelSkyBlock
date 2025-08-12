package net.swofty.type.skyblockgeneric.gui.inventories.museum;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.TrackedItem;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.set.ArmorSetRegistry;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplays;
import net.swofty.type.skyblockgeneric.museum.MuseumableItemCategory;
import SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.ItemPriceCalculator;
import net.swofty.type.generic.utility.PaginationList;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GUIMuseumEmptyDisplay extends HypixelPaginatedGUI<Object> {
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
    public PaginationList<Object> fillPaged(SkyBlockPlayer player, PaginationList<Object> paged) {
        DatapointMuseum.MuseumData data = player.getMuseumData();

        for (MuseumableItemCategory category : display.getAllowedItemCategories()) {
            List<SkyBlockItem> categoryItems = data.getNotInDisplayByCategory(category);

            if (category == MuseumableItemCategory.ARMOR_SETS) {
                // Group armor pieces by armor set
                Map<ArmorSetRegistry, List<SkyBlockItem>> armorSetGroups = categoryItems.stream()
                        .filter(item -> ArmorSetRegistry.getArmorSet(item.getAttributeHandler().getPotentialType()) != null)
                        .collect(Collectors.groupingBy(item ->
                                ArmorSetRegistry.getArmorSet(item.getAttributeHandler().getPotentialType())));

                // Only add complete armor sets (4 pieces)
                armorSetGroups.entrySet().stream()
                        .filter(entry -> entry.getValue().size() == 4)
                        .forEach(paged::add);
            } else {
                // Add individual items for non-armor categories
                paged.addAll(categoryItems);
            }
        }

        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, Object item) {
        if (item instanceof SkyBlockItem skyBlockItem) {
            return skyBlockItem.getDisplayName().toLowerCase().contains(query.toLowerCase());
        } else if (item instanceof Map.Entry<?, ?> entry && entry.getKey() instanceof ArmorSetRegistry armorSet) {
            return armorSet.getDisplayName().toLowerCase().contains(query.toLowerCase());
        }
        return false;
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        if (!new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join()) {
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
                public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
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
    public String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<Object> paged) {
        return display.toString() + " Slot #" + (position + 1) + " (" + page + "/" + paged.getPageCount() + ")";
    }

    @Override
    @SuppressWarnings("unchecked")
    public GUIClickableItem createItemFor(Object item, int slot, SkyBlockPlayer player) {
        if (item instanceof SkyBlockItem skyBlockItem) {
            return createIndividualItemDisplay(skyBlockItem, slot, player);
        } else if (item instanceof Map.Entry<?, ?> entry && entry.getKey() instanceof ArmorSetRegistry armorSet) {
            return createArmorSetDisplay((Map.Entry<ArmorSetRegistry, List<SkyBlockItem>>) entry, slot, player);
        }

        // Fallback
        Logger.error("Unknown item type: " + item.getClass().getName());
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                return ItemStackCreator.getStack("§cError", Material.BARRIER, 1, "§7Unknown item type");
            }
        };
    }

    private GUIClickableItem createIndividualItemDisplay(SkyBlockItem item, int slot, SkyBlockPlayer player) {
        TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())
        );
        ProxyService proxyService = new ProxyService(ServiceType.ITEM_TRACKER);
        TrackedItemRetrieveProtocolObject.TrackedItemResponse trackedItemResponse = (TrackedItemRetrieveProtocolObject.TrackedItemResponse) proxyService.handleRequest(message).join();
        TrackedItem trackedItem = trackedItemResponse.trackedItem();

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                player.closeInventory();
                player.sendMessage("§aYou set " + display.toString() + " Slot #" + (position + 1) + " to display " + item.getDisplayName() + "§a!");
                DatapointMuseum.MuseumData data = player.getMuseumData();
                data.getDisplayHandler().addToDisplay(item, display, position);
                player.setMuseumData(data);
                MuseumDisplays.updateDisplay(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
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
                UUID itemUuid = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());
                if (data.getCalculatedPrices().containsKey(itemUuid)) {
                    lore.add("§6" + StringUtility.commaify(data.getCalculatedPrices().get(itemUuid)) + " Coins");
                } else {
                    lore.add("§cUncalculated");
                }
                lore.add(" ");
                lore.add("§eClick to display!");

                return ItemStackCreator.updateLore(stack, lore);
            }
        };
    }

    private GUIClickableItem createArmorSetDisplay(Map.Entry<ArmorSetRegistry, List<SkyBlockItem>> armorSetEntry, int slot, SkyBlockPlayer player) {
        ArmorSetRegistry armorSet = armorSetEntry.getKey();
        List<SkyBlockItem> armorPieces = armorSetEntry.getValue();

        // Get the helmet for display purposes
        SkyBlockItem displayItem = armorPieces.stream()
                .filter(item -> item.getAttributeHandler().getPotentialType().equals(armorSet.getHelmet()))
                .findFirst()
                .orElse(armorPieces.getFirst());

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                player.closeInventory();
                player.sendMessage("§aYou set " + display.toString() + " Slot #" + (position + 1) + " to display " + armorSet.getDisplayName() + " Set§a!");
                DatapointMuseum.MuseumData data = player.getMuseumData();

                // Add all armor pieces to the same display slot
                data.getDisplayHandler().addToDisplay(armorPieces, display, position);
                player.setMuseumData(data);
                MuseumDisplays.updateDisplay(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                DatapointMuseum.MuseumData data = player.getMuseumData();
                ArrayList<String> lore = new ArrayList<>();

                lore.add("§7Complete armor set with 4 pieces:");
                for (SkyBlockItem piece : armorPieces) {
                    lore.add("§8• " + piece.getDisplayName());
                }

                lore.add("§8§m---------------------");

                // Calculate total value
                int totalCleanValue = armorPieces.stream()
                        .mapToInt(piece -> new ItemPriceCalculator(piece).calculateCleanPrice().intValue())
                        .sum();

                lore.add("§7Set Clean Value");
                lore.add("§6" + StringUtility.commaify(totalCleanValue) + " Coins");
                lore.add(" ");
                lore.add("§eClick to display armor set!");

                return ItemStackCreator.getStack("§a" + armorSet.getDisplayName() + " Set",
                        displayItem.getMaterial(), 1, lore);
            }
        };
    }
}