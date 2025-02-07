package net.swofty.types.generic.gui.inventory.inventories.bazaar.selections;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.item.ItemType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.concurrent.CompletableFuture;

public class GUIBazaarPriceSelection extends SkyBlockAbstractInventory {
    private CompletableFuture<Double> future = new CompletableFuture<>();
    private final boolean isSellOrder;
    private final ItemType itemTypeLinker;
    private final Double lowestPrice;
    private final Double highestPrice;
    private final Integer amount;
    private final SkyBlockAbstractInventory previousGUI;

    public GUIBazaarPriceSelection(SkyBlockAbstractInventory previousGUI, Integer amount,
                                   Double lowestPrice, Double highestPrice,
                                   ItemType itemTypeLinker, boolean isSellOrder) {
        super(InventoryType.CHEST_4_ROW);
        this.previousGUI = previousGUI;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.itemTypeLinker = itemTypeLinker;
        this.isSellOrder = isSellOrder;
        this.amount = amount;

        doAction(new SetTitleAction(Component.text("At what price" +
                (isSellOrder ? " are you selling?" : "are you buying?") + "?")));

        startLoop("refresh", 10, () -> refreshItems(owner));
    }

    public CompletableFuture<Double> openPriceSelection(SkyBlockPlayer player) {
        future = new CompletableFuture<>();
        addViewer(player);
        return future;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        setupBackButton();
        setupPriceOptions();
    }

    private void setupBackButton() {
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + previousGUI.getTitleAsString()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(previousGUI);
                    return true;
                })
                .build());
    }

    private void setupPriceOptions() {
        double spread = highestPrice - lowestPrice;
        double spreadPrice = isSellOrder ? highestPrice - (spread / 10) : lowestPrice + (spread / 10);

        // Spread Price Option
        attachItem(GUIItem.builder(14)
                .item(ItemStackCreator.getStack("§610% of Spread",
                        Material.GOLDEN_HORSE_ARMOR, 1,
                        "§8" + (isSellOrder ? "Sell Offer" : "Buy Offer") + " Setup",
                        " ",
                        "§7Lowest price: §6" + lowestPrice + " coins",
                        "§7Highest price: §6" + highestPrice + " coins",
                        "§7Spread: §6" + highestPrice + " §7- §6" + lowestPrice + " §7= §6" + spread + " coins",
                        " ",
                        "§7" + (isSellOrder ? "Selling" : "Buying") + " §a" +  amount + "§7x",
                        "§7Unit price: §6" + spreadPrice + " coins",
                        " ",
                        "§7Total: §6" + (spreadPrice * amount) + " coins",
                        " ",
                        "§eClick to use this price!").build())
                .onClick((ctx, item) -> {
                    future.complete(spreadPrice);
                    return true;
                })
                .build());

        double incrementedOffer = isSellOrder ? lowestPrice - 0.1 : highestPrice + 0.1;

        // Incremental Price Option
        attachItem(GUIItem.builder(12)
                .item(ItemStackCreator.getStack("§6Best Offer " + (isSellOrder ? "-" : "+") + "0.1",
                        Material.GOLD_NUGGET, 1,
                        "§8" + (isSellOrder ? "Sell Offer" : "Buy Offer") + " Setup",
                        " ",
                        "§7Beat the price of the best offer so",
                        "§7yours is filled first.",
                        " ",
                        "§7" + (isSellOrder ? "Selling" : "Buying") + " §a" +  amount + "§7x",
                        "§7Unit price: §6" + incrementedOffer + " coins",
                        " ",
                        "§7Total: §6" + (incrementedOffer * amount) + " coins",
                        " ",
                        "§eClick to use this price!").build())
                .onClick((ctx, item) -> {
                    future.complete(incrementedOffer);
                    return true;
                })
                .build());

        double bestOffer = isSellOrder ? lowestPrice : highestPrice;

        // Best Offer Option
        attachItem(GUIItem.builder(10)
                .item(ItemStackCreator.getStack("§6Same as Best Offer",
                        itemTypeLinker.material, 1,
                        "§8" + (isSellOrder ? "Sell Offer" : "Buy Offer") + " Setup",
                        " ",
                        "§7Use the same price as the lowest",
                        "§7Sell Offer for this item.",
                        " ",
                        "§7" + (isSellOrder ? "Selling" : "Buying") + " §a" +  amount + "§7x",
                        "§7Unit price: §6" + bestOffer + " coins",
                        " ",
                        "§7Total: §6" + (bestOffer * amount) + " coins",
                        " ",
                        "§eClick to use this price!").build())
                .onClick((ctx, item) -> {
                    future.complete(bestOffer);
                    return true;
                })
                .build());

        // Custom Price Option
        attachItem(GUIItem.builder(16)
                .item(ItemStackCreator.getStack("§6Custom Price",
                        Material.OAK_SIGN, 1,
                        "§8" + (isSellOrder ? "Sell Offer" : "Buy Offer") + " Setup",
                        " ",
                        "§7Set the price per unit you're willing",
                        "§7to pay. Minimum 50% of the best order.",
                        " ",
                        "§7Ordering: §a" +  amount + "§7x",
                        " ",
                        "§eClick to specify!").build())
                .onClick((ctx, item) -> {
                    SkyBlockSignGUI signGUI = new SkyBlockSignGUI(ctx.player());
                    String output = signGUI.open(new String[]{"Enter price", "per unit"}).join();
                    try {
                        double customPrice = Double.parseDouble(output);
                        future.complete(customPrice);
                    } catch (NumberFormatException e) {
                        ctx.player().sendMessage("§cInvalid price format!");
                    }
                    return true;
                })
                .build());
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
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        future.complete(0D);
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        future.complete(0D);
    }
}