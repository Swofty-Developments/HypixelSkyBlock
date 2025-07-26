package net.swofty.types.generic.gui.inventory.inventories.museum;

import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.TrackedItem;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.datapoints.DatapointMuseum;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.ItemPriceCalculator;

import java.util.ArrayList;
import java.util.UUID;

public class GUIMuseumNonEmptyDisplay extends SkyBlockInventoryGUI {
    private final SkyBlockItem item;
    private final MuseumDisplays display;
    private final int position;

    public GUIMuseumNonEmptyDisplay(SkyBlockItem item, MuseumDisplays display, int position) {
        super(item.getAttributeHandler().getPotentialType().getDisplayName(),
                InventoryType.CHEST_4_ROW);

        this.item = item;
        this.display = display;
        this.position = position;

        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(31));
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        if (!new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join()) {
            e.player().sendMessage("§cThe item tracker is currently offline. Please try again later.");
            e.player().closeInventory();
            return;
        }

        TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                item.getAttributeHandler().getUniqueTrackedID()
        );
        ProxyService proxyService = new ProxyService(ServiceType.ITEM_TRACKER);

        TrackedItemRetrieveProtocolObject.TrackedItemResponse trackedItemResponse = (TrackedItemRetrieveProtocolObject.TrackedItemResponse) proxyService.handleRequest(message).join();
        TrackedItem trackedItem = trackedItemResponse.trackedItem();

        set(new GUIClickableItem(35) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                player.closeInventory();
                data.removeFromDisplay(display, position);
                MuseumDisplays.updateDisplay(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§cRemove From Display",
                        Material.BEDROCK, 1,
                        "§7Removes this item from being",
                        "§7displayed in your §9Museum§7.",
                        " ",
                        "§eClick to remove!");
            }
        });
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                DatapointMuseum.MuseumData data = player.getMuseumData();
                ItemStack.Builder stack = new NonPlayerItemUpdater(item).getUpdatedItem();
                ArrayList<String> lore = new ArrayList<>(item.getLore());
                UUID trackedItemUUID = UUID.fromString(item.getAttributeHandler().getUniqueTrackedID());

                lore.add("§8§m---------------------");
                lore.add("§7Item Donated");
                lore.add("§b" + StringUtility.formatAsDate(data.getInsertionTimes().get(trackedItemUUID)));
                lore.add(" ");
                lore.add("§7Item Created");
                lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                lore.add("§6  " + StringUtility.commaifyAndTh(trackedItem.getNumberMade()) + " §7created");
                lore.add(" ");
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
        });

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
