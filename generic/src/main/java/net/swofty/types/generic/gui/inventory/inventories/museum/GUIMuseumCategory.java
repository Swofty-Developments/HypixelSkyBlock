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

public class GUIMuseumCategory extends SkyBlockPaginatedGUI<ItemType> {
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

        if (skyBlockItem.getAttributeHandler().getItemTypeAsType() == null) {
            return;
        }

        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
        DatapointMuseum.MuseumData data = player.getMuseumData();

        if (data.getTypeInMuseum(skyBlockItem.getAttributeHandler().getItemTypeAsType()) != null) {
            player.sendMessage("§cYou already have a " + skyBlockItem.getAttributeHandler().getItemTypeAsType().getDisplayName(null) + " in your Museum!");
            return;
        }

        if (data.getTypePreviouslyInMuseum(skyBlockItem.getAttributeHandler().getItemTypeAsType()) != null) {
            UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
            UUID previouslyInMuseumUUID = UUID.fromString(
                    data.getTypePreviouslyInMuseum(skyBlockItem.getAttributeHandler().getItemTypeAsType())
                            .getAttributeHandler().getUniqueTrackedID()
            );

            if (!trackedItemUUID.equals(previouslyInMuseumUUID)) {
                player.sendMessage("§cYou can only re-add the item that was already in your Museum!");
                return;
            }
        }

        if (category.contains(skyBlockItem.getAttributeHandler().getItemTypeAsType())) {
            skyBlockItem.getAttributeHandler().setSoulBound(true);
            data.add(skyBlockItem);
            player.setMuseumData(data);
            player.getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
            player.closeInventory();
            MuseumDisplays.updateDisplay(player);

            new GUIMuseumCategory(category).open(player);
            player.sendMessage("§aYou donated your " + skyBlockItem.getAttributeHandler().getItemTypeAsType().getDisplayName(null) + " to the Museum!");
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
    public PaginationList<ItemType> fillPaged(SkyBlockPlayer player, PaginationList<ItemType> paged) {
        paged.addAll(category.getItems());
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, ItemType item) {
        return !item.getDisplayName(null).toLowerCase().contains(query.toLowerCase());
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

        for (int i = 0; i < 36; i++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(i));
            if (item.getGenericInstance() == null) {
                continue;
            }

            if (category.contains(item.getAttributeHandler().getItemTypeAsType())) {
                TrackedItem trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER)
                        .callEndpoint(new ProtocolGetTrackedItem(),
                                Map.of("item-uuid", UUID.fromString(item.getAttributeHandler().getUniqueTrackedID()))
                        ).join().get("tracked-item");

                ItemStack.Builder toReturn = item.getItemStackBuilder();
                List<String> lore = new ArrayList<>(item.getLore());
                lore.add("§8§m---------------------");
                lore.add("§7Item Created");
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add(" ");
                lore.add("§eClick to donate item!");

                player.getInventory().setItemStack(i, ItemStackCreator.updateLore(toReturn, lore)
                        .displayName(Component.text(item.getDisplayName()).decoration(
                                TextDecoration.ITALIC, false
                        ))
                        .build());
            }
        }
    }

    @Override
    public String getTitle(SkyBlockPlayer player, String query, int page, PaginationList<ItemType> paged) {
        return "Museum -> " + category.toString();
    }

    @Override
    public GUIClickableItem createItemFor(ItemType item, int slot, SkyBlockPlayer player) {
        DatapointMuseum.MuseumData data = player.getMuseumData();
        SkyBlockItem skyBlockItem = data.getItem(category, item);
        boolean inMuseum = skyBlockItem != null;
        boolean hasTakenItOut = data.getPreviouslyInMuseum().contains(skyBlockItem);

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (!inMuseum || hasTakenItOut) {
                    return;
                }

                player.sendMessage("§aYou retrieved your " + item.getDisplayName(null) + " from the Museum. It still counts towards your Museum progress, but not towards your total item value.");
                player.sendMessage("§aYou can return or replace the item in your Museum at any time!");

                data.getPreviouslyInMuseum().add(skyBlockItem);
                data.getCurrentlyInMuseum().remove(skyBlockItem);
                data.getMuseumDisplay().remove(UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID()));
                player.setMuseumData(data);
                MuseumDisplays.updateDisplay(player);

                player.closeInventory();
                player.addAndUpdateItem(skyBlockItem.getItemStack());
                new GUIMuseumCategory(category).open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!inMuseum) {
                    return ItemStackCreator.getStack("§c" + item.getDisplayName(null),
                            Material.GRAY_DYE, 1,
                            "§7Click on this item in your inventory to",
                            "§7add it to your §9Museum§7!");
                }

                UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
                TrackedItem trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER)
                        .callEndpoint(new ProtocolGetTrackedItem(),
                                Map.of("item-uuid", trackedItemUUID))
                        .join().get("tracked-item");

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

                return ItemStackCreator.getStack("§a" + item.getDisplayName(null),
                       hasTakenItOut ? Material.LIME_DYE : item.material, 1, lore);
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
