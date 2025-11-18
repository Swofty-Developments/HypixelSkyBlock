package net.swofty.type.skyblockgeneric.gui.inventories.museum;

import lombok.SneakyThrows;
import org.tinylog.Logger;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import org.tinylog.Logger;
import net.minestom.server.inventory.InventoryType;
import org.tinylog.Logger;
import net.minestom.server.item.ItemStack;
import org.tinylog.Logger;
import net.minestom.server.item.Material;
import org.tinylog.Logger;
import net.swofty.commons.ServiceType;
import org.tinylog.Logger;
import net.swofty.commons.StringUtility;
import org.tinylog.Logger;
import net.swofty.commons.TrackedItem;
import org.tinylog.Logger;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import org.tinylog.Logger;
import net.swofty.proxyapi.ProxyService;
import org.tinylog.Logger;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import org.tinylog.Logger;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import org.tinylog.Logger;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import org.tinylog.Logger;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import org.tinylog.Logger;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.museum.MuseumDisplays;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;
import net.swofty.type.skyblockgeneric.utility.ItemPriceCalculator;
import org.tinylog.Logger;

import java.util.ArrayList;
import org.tinylog.Logger;
import java.util.List;
import org.tinylog.Logger;
import java.util.Map;
import org.tinylog.Logger;
import java.util.UUID;
import org.tinylog.Logger;

public class GUIMuseumNonEmptyDisplay extends HypixelInventoryGUI {
    private static final Map<Integer, int[]> SLOTS = Map.of(
            1, new int[]{13},
            2, new int[]{12, 14},
            3, new int[]{11, 13, 15},
            4, new int[]{10, 12, 14, 16},
            5, new int[]{11, 12, 13, 14, 15},
            6, new int[]{11, 12, 13, 14, 15, 22},
            7, new int[]{10, 11, 12, 13, 14, 15, 16},
            8, new int[]{11, 12, 13, 14, 15, 21, 22, 23},
            9, new int[]{10, 11, 12, 13, 14, 15, 16, 21, 23},
            10, new int[]{11, 12, 13, 14, 15, 20, 21, 22, 23, 24}
    );

    private final MuseumDisplays display;
    private final int position;
    private List<SkyBlockItem> items;

    public GUIMuseumNonEmptyDisplay(MuseumDisplays display, int position) {
        super("Museum Display", InventoryType.CHEST_4_ROW);

        this.display = display;
        this.position = position;

        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(31));
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        DatapointMuseum.MuseumData data = player.getMuseumData();

        // Get all items at this display position
        items = data.getDisplayHandler().getItemsAtSlot(display, position);

        if (items.isEmpty()) {
            player.sendMessage("§cNo items found at this display position.");
            player.closeInventory();
            return;
        }

        // Update title based on items
        if (items.size() == 1) {
            setTitle(items.getFirst().getAttributeHandler().getPotentialType().getDisplayName());
        } else {
            setTitle("Display with " + items.size() + " items");
        }

        if (!new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join()) {
            player.sendMessage("§cThe item tracker is currently offline. Please try again later.");
            player.closeInventory();
            return;
        }

        set(new GUIClickableItem(35) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointMuseum.MuseumData data = player.getMuseumData();
                player.closeInventory();
                data.getDisplayHandler().removeAllFromSlot(display, position);
                MuseumDisplays.updateDisplay(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§cRemove From Display",
                        Material.BEDROCK, 1,
                        "§7Removes " + (items.size() == 1 ? "this item" : "these " + items.size() + " items") + " from being",
                        "§7displayed in your §9Museum§7.",
                        " ",
                        "§eClick to remove!");
            }
        });

        // Display items using the slot mapping
        int itemCount = Math.min(items.size(), 10); // Cap at 10 items max
        int[] slotsToUse = SLOTS.get(itemCount);

        for (int i = 0; i < itemCount; i++) {
            final SkyBlockItem item = items.get(i);
            final int slotIndex = slotsToUse[i];

            set(new GUIItem(slotIndex) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return createItemDisplay(item, player);
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    private ItemStack.Builder createItemDisplay(SkyBlockItem item, SkyBlockPlayer player) {
        DatapointMuseum.MuseumData data = player.getMuseumData();

        // Get tracked item info
        TrackedItem trackedItem = null;
        try {
            TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message =
                    new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                            UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())
                    );
            ProxyService proxyService = new ProxyService(ServiceType.ITEM_TRACKER);
            TrackedItemRetrieveProtocolObject.TrackedItemResponse response =
                    (TrackedItemRetrieveProtocolObject.TrackedItemResponse) proxyService.handleRequest(message).join();
            trackedItem = response.trackedItem();
        } catch (Exception e) {
            Logger.error(e, "Error displaying museum items");
        }

        ItemStack.Builder stack = new NonPlayerItemUpdater(item).getUpdatedItem();
        ArrayList<String> lore = new ArrayList<>(item.getLore());
        UUID trackedItemUUID = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());

        lore.add("§8§m---------------------");
        lore.add("§7Item Donated");
        lore.add("§b" + StringUtility.formatAsDate(data.getInsertionTimes().get(trackedItemUUID)));
        lore.add(" ");

        if (trackedItem != null) {
            lore.add("§7Item Created");
            lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
            lore.add("§6  " + StringUtility.commaifyAndTh(trackedItem.getNumberMade()) + " §7created");
            lore.add(" ");
        }

        lore.add("§7Item Clean Value");
        lore.add("§6" + StringUtility.commaify(new ItemPriceCalculator(item).calculateCleanPrice()) + " Coins");
        lore.add(" ");
        lore.add("§7Item Value");
        if (data.getCalculatedPrices().containsKey(trackedItemUUID)) {
            lore.add("§6" + StringUtility.commaify(data.getCalculatedPrices().get(trackedItemUUID)) + " Coins");
        } else {
            lore.add("§cUncalculated");
        }

        return ItemStackCreator.updateLore(stack, lore);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}