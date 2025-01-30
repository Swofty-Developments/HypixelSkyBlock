package net.swofty.types.generic.gui.inventory.inventories.museum;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
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
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.museum.MuseumDisplays;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.ItemPriceCalculator;

import java.util.ArrayList;

public class GUIMuseumNonEmptyDisplay extends SkyBlockAbstractInventory {
    private static final String STATE_SERVICE_ONLINE = "service_online";
    private static final String STATE_ITEM_TRACKED = "item_tracked";

    private final SkyBlockItem item;
    private final MuseumDisplays display;
    private final int position;
    private TrackedItem trackedItem;

    public GUIMuseumNonEmptyDisplay(SkyBlockItem item, MuseumDisplays display, int position) {
        super(InventoryType.CHEST_4_ROW);
        this.item = item;
        this.display = display;
        this.position = position;

        doAction(new SetTitleAction(Component.text(item.getAttributeHandler().getPotentialType().getDisplayName())));
    }

    @SneakyThrows
    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Check service availability
        if (!new ProxyService(ServiceType.ITEM_TRACKER).isOnline().join()) {
            player.sendMessage("§cThe item tracker is currently offline. Please try again later.");
            player.closeInventory();
            return;
        }
        doAction(new AddStateAction(STATE_SERVICE_ONLINE));

        // Get tracked item data
        TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                item.getAttributeHandler().getUniqueTrackedID()
        );
        trackedItem = (TrackedItem) new ProxyService(ServiceType.ITEM_TRACKER).handleRequest(message).join();
        doAction(new AddStateAction(STATE_ITEM_TRACKED));

        setupCloseButton();
        setupRemoveButton();
        setupItemDisplay(player);
    }

    private void setupCloseButton() {
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .requireState(STATE_SERVICE_ONLINE)
                .build());
    }

    private void setupRemoveButton() {
        attachItem(GUIItem.builder(35)
                .item(ItemStackCreator.getStack("§cRemove From Display",
                        Material.BEDROCK, 1,
                        "§7Removes this item from being",
                        "§7displayed in your §9Museum§7.",
                        " ",
                        "§eClick to remove!").build())
                .onClick((ctx, item) -> {
                    DatapointMuseum.MuseumData data = ctx.player().getMuseumData();
                    ctx.player().closeInventory();
                    data.removeFromDisplay(display, position);
                    MuseumDisplays.updateDisplay(ctx.player());
                    return true;
                })
                .requireState(STATE_SERVICE_ONLINE)
                .build());
    }

    private void setupItemDisplay(SkyBlockPlayer player) {
        attachItem(GUIItem.builder(13)
                .item(() -> {
                    DatapointMuseum.MuseumData data = player.getMuseumData();
                    ItemStack.Builder stack = new NonPlayerItemUpdater(item).getUpdatedItem();
                    ArrayList<String> lore = new ArrayList<>(item.getLore());

                    lore.add("§8§m---------------------");
                    lore.add("§7Item Donated");
                    lore.add("§b" + StringUtility.formatAsDate(data.getInsertionTimes()
                            .get(item.getAttributeHandler().getUniqueTrackedID())));
                    lore.add(" ");
                    lore.add("§7Item Created");
                    lore.add("§a" + StringUtility.formatAsDate(trackedItem.getCreated()));
                    lore.add("§6  " + StringUtility.commaifyAndTh(trackedItem.getNumberMade()) + " §7created");
                    lore.add(" ");
                    lore.add("§7Item Clean Value");
                    lore.add("§6" + StringUtility.commaify(new ItemPriceCalculator(item).calculateCleanPrice()) + " Coins");
                    lore.add(" ");
                    lore.add("§7Item Value");

                    if (data.getCalculatedPrices().containsKey(item.getAttributeHandler().getUniqueTrackedID())) {
                        lore.add("§6" + StringUtility.commaify(data.getCalculatedPrices()
                                .get(item.getAttributeHandler().getUniqueTrackedID())) + " Coins");
                    } else {
                        lore.add("§cUncalculated");
                    }

                    return ItemStackCreator.updateLore(stack, lore).build();
                })
                .requireStates(new String[]{STATE_SERVICE_ONLINE, STATE_ITEM_TRACKED})
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}