package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetItemProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarSellProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.bazaar.selections.GUIBazaarPriceSelection;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class GUIBazaarItem extends SkyBlockAbstractInventory {
    private static final String STATE_ORDERS_AVAILABLE = "orders_available";
    private static final String STATE_NO_ORDERS = "no_orders";
    private static final String STATE_HAS_INVENTORY = "has_inventory";
    private static final String STATE_NO_INVENTORY = "no_inventory";

    private final ItemType itemType;
    private BazaarItem currentBazaarItem;

    public GUIBazaarItem(ItemType itemType) {
        super(InventoryType.CHEST_4_ROW);
        this.itemType = itemType;

        doAction(new SetTitleAction(Component.text(BazaarCategories.getFromItem(itemType).getKey() +
                " -> " + itemType.getDisplayName())));

        startLoop("refresh", 10, () -> refreshItems(owner));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        setupNavigationButtons();
        setupItemDisplay();

        Thread.startVirtualThread(() -> {
            BazaarGetItemProtocolObject.BazaarGetItemResponse response =
                    (BazaarGetItemProtocolObject.BazaarGetItemResponse) new ProxyService(ServiceType.BAZAAR)
                            .handleRequest(new BazaarGetItemProtocolObject.BazaarGetItemMessage(itemType.name()))
                            .join();

            currentBazaarItem = response.item();
            updateBazaarState();
            setupTradeButtons();
        });
    }

    private void setupNavigationButtons() {
        // Back to category button
        attachItem(GUIItem.builder(30)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + BazaarCategories.getFromItem(itemType).getKey()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBazaarItemSet(
                            BazaarCategories.getFromItem(itemType).getKey(),
                            BazaarCategories.getFromItem(itemType).getValue()));
                    return true;
                })
                .build());

        // Back to main bazaar button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStackHead("§6Go Back",
                        "c232e3820897429157619b0ee099fec0628f602fff12b695de54aef11d923ad7",
                        1, "§7To Bazaar").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBazaar(
                            BazaarCategories.getFromItem(itemType).getKey()));
                    return true;
                })
                .build());

        // Manage orders button
        attachItem(GUIItem.builder(32)
                .item(ItemStackCreator.getStack("§aManage Orders", Material.BOOK, 1,
                        "§7You don't have any ongoing orders.",
                        " ",
                        "§eClick to manage!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBazaarOrders());
                    return true;
                })
                .build());
    }

    private void setupItemDisplay() {
        attachItem(GUIItem.builder(13)
                .item(() -> new NonPlayerItemUpdater(new SkyBlockItem(itemType))
                        .getUpdatedItem().build())
                .build());
    }

    private void updateBazaarState() {
        if (!currentBazaarItem.getSellOrders().isEmpty()) {
            doAction(new AddStateAction(STATE_ORDERS_AVAILABLE));
        } else {
            doAction(new AddStateAction(STATE_NO_ORDERS));
        }

        Map<Integer, Integer> inventory = owner.getAllOfTypeInInventory(itemType);
        if (!inventory.isEmpty()) {
            doAction(new AddStateAction(STATE_HAS_INVENTORY));
        } else {
            doAction(new AddStateAction(STATE_NO_INVENTORY));
        }
    }

    private void setupTradeButtons() {
        setupBuyInstantlyButton();
        setupSellInstantlyButton();
        setupCreateSellOfferButton();
    }

    private void setupBuyInstantlyButton() {
        attachItem(GUIItem.builder(10)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8" + itemType.getDisplayName());
                    lore.add(" ");

                    if (!currentBazaarItem.getSellOrders().isEmpty()) {
                        double pricePerUnit = currentBazaarItem.getSellStatistics().getLowestOrder();
                        lore.add("§7Price per unit: §6" + pricePerUnit + " coins");
                        lore.add("§7Stack price: §6" + (pricePerUnit * 64) + " coins");
                        lore.add(" ");
                        lore.add("§eClick to pick amount!");
                    } else {
                        lore.add("§cNo sell orders available.");
                        lore.add(" ");
                        lore.add("§8Cannot buy instantly");
                    }

                    return ItemStackCreator.getStack("§aBuy Instantly",
                            Material.GOLDEN_HORSE_ARMOR, 1, lore).build();
                })
                .build());
    }

    private void setupSellInstantlyButton() {
        attachItem(GUIItem.builder(11)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8" + itemType.getDisplayName());
                    lore.add(" ");

                    int amountInInventory = owner.getAllOfTypeInInventory(itemType).size();

                    if (amountInInventory == 0) {
                        lore.add("§7Inventory: §cNone!");
                        lore.add(" ");
                        lore.add("§7Price Per Unit: §6" + currentBazaarItem.getBuyStatistics().getHighestOrder() + " coins");
                        lore.add(" ");
                        lore.add("§8None to sell in your inventory!");
                    } else {
                        lore.add("§7Inventory: §a" + amountInInventory + "configuration/items");
                        lore.add(" ");

                        if (!currentBazaarItem.getBuyOrders().isEmpty()) {
                            double pricePerUnit = currentBazaarItem.getBuyStatistics().getHighestOrder();
                            lore.add("§7Amount: §a" + amountInInventory + "§7x");
                            lore.add("§7Total price: §6" + (pricePerUnit * amountInInventory) + " coins");
                            lore.add(" ");
                            lore.add("§eClick to sell!");
                        } else {
                            lore.add("§cNo buy orders available.");
                            lore.add(" ");
                            lore.add("§8Cannot sell instantly");
                        }
                    }

                    return ItemStackCreator.getStack("§6Sell Instantly",
                            Material.HOPPER, 1, lore).build();
                })
                .build());
    }

    private void setupCreateSellOfferButton() {
        attachItem(GUIItem.builder(16)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§8" + itemType.getDisplayName());
                    lore.add(" ");
                    lore.add("§6Top Offers:");

                    if (currentBazaarItem.getSellOrders().isEmpty()) {
                        lore.add("§cNo sell orders available.");
                        lore.add(" ");
                        Map<Integer, Integer> itemInInventory = owner.getAllOfTypeInInventory(itemType);
                        if (!itemInInventory.isEmpty()) {
                            lore.add("§7You have §e" + owner.getAmountInInventory(itemType) + "§7x in your inventory.");
                            lore.add("§eClick to setup Sell Order");
                        } else {
                            lore.add("§8None in inventory!");
                        }
                    } else {
                        currentBazaarItem.getSellStatistics().getTop(7, true).forEach((order) -> {
                            lore.add("§7- §6" + order.totalCoins() + " coins §7each | §a" + order.totalItems() +
                                    "§7x in §f" + order.numberOfOrders() + "§7 orders");
                        });
                        lore.add(" ");

                        Map<Integer, Integer> itemInInventory = owner.getAllOfTypeInInventory(itemType);
                        if (!itemInInventory.isEmpty()) {
                            lore.add("§7You have §e" + owner.getAmountInInventory(itemType) + "§7x in your inventory.");
                            lore.add("§eClick to setup Sell Order");
                        } else {
                            lore.add("§8None in inventory!");
                        }
                    }

                    return ItemStackCreator.getStack("§6Create Sell Offer",
                            Material.PAPER, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    Map<Integer, Integer> itemInInventory = ctx.player().getAllOfTypeInInventory(itemType);
                    if (itemInInventory.isEmpty()) {
                        ctx.player().sendMessage("§cYou don't have any §e" + itemType.getDisplayName() +
                                "§c in your inventory!");
                        return true;
                    }

                    handleSellOffer(ctx.player(), itemInInventory);
                    return true;
                })
                .build());
    }

    private void handleSellOffer(SkyBlockPlayer player, Map<Integer, Integer> itemInInventory) {
        new GUIBazaarPriceSelection(this, itemInInventory.size(),
                currentBazaarItem.getSellStatistics().getLowestOrder(),
                currentBazaarItem.getSellStatistics().getHighestOrder(),
                itemType, true)
                .openPriceSelection(player)
                .thenAccept(price -> processSellOffer(player, price, itemInInventory));
    }

    private void processSellOffer(SkyBlockPlayer player, double price, Map<Integer, Integer> itemInInventory) {
        if (price <= 0) {
            if (player.isOnline())
                player.sendMessage("§cYou can only sell for a positive amount of coins!");
            return;
        }

        player.sendMessage("§6[Bazaar] §7Putting goods in escrow...");

        int amountInInventory = player.getAmountInInventory(itemType);
        itemInInventory.forEach((slot, amount) ->
                player.getInventory().setItemStack(slot, ItemStack.AIR));

        player.sendMessage("§6[Bazaar] §7Submitting sell order...");

        BazaarSellProtocolObject.BazaarSellMessage message =
                new BazaarSellProtocolObject.BazaarSellMessage(
                        itemType.name(), player.getUuid(), price, amountInInventory);

        new ProxyService(ServiceType.BAZAAR)
                .handleRequest(message)
                .thenAccept(response -> {
                    BazaarSellProtocolObject.BazaarSellResponse bazaarResponse = (BazaarSellProtocolObject.BazaarSellResponse) response;
                    if (bazaarResponse.successful) {
                        player.sendMessage("§6[Bazaar] §eSell Order Setup! §a" + amountInInventory +
                                "x §e" + itemType.getDisplayName() + "§a for §e" + price + " coins each!");
                    } else {
                        player.sendMessage("§c[Bazaar] §cFailed to submit buy order!");
                        player.sendMessage("§c[Bazaar] §cYou cannot place orders on items you " +
                                "already have orders on!");
                    }
                });
    }

    private void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.BAZAAR).isOnline().join()) {
            player.sendMessage("§cThe Bazaar is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}