package net.swofty.types.generic.gui.inventory.inventories.bazaar.selections;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.concurrent.CompletableFuture;

public class GUIBazaarPriceSelection extends SkyBlockInventoryGUI implements RefreshingGUI {
    private CompletableFuture<Double> future = new CompletableFuture<>();
    private final boolean isSellOrder;
    private final ItemType itemType;
    private final Double lowestPrice;
    private final Double highestPrice;
    private final Integer amount;


    public GUIBazaarPriceSelection(SkyBlockInventoryGUI previousGUI, Integer amount,
                                   Double lowestPrice, Double highestPrice,
                                   ItemType itemType, boolean isSellOrder) {
        super("At what price" + (isSellOrder ? " are you selling?" : "are you buying?") + "?", InventoryType.CHEST_4_ROW);

        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.itemType = itemType;
        this.isSellOrder = isSellOrder;
        this.amount = amount;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, previousGUI));
    }

    public CompletableFuture<Double> openPriceSelection(SkyBlockPlayer player) {
        future = new CompletableFuture<>();
        open(player);

        Thread.startVirtualThread(() -> {
            double spread = highestPrice - lowestPrice;
            double spreadPrice = isSellOrder ? highestPrice - (spread / 10) : lowestPrice + (spread / 10);
            set(new GUIClickableItem(14) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    future.complete(spreadPrice);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§610% of Spread",
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
                            "§eClick to use this price!");
                }
            });


            double incrementedOffer = isSellOrder ? lowestPrice - 0.1 : highestPrice + 0.1;
            set(new GUIClickableItem(12) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    future.complete(incrementedOffer);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§6Best Offer " + (isSellOrder ? "-" : "+") + "0.1",
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
                            "§eClick to use this price!");
                }
            });

            double bestOffer = isSellOrder ? lowestPrice : highestPrice;
            set(new GUIClickableItem(10) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    future.complete(bestOffer);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§6Same as Best Offer",
                            itemType.material, 1,
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
                            "§eClick to use this price!");
                }
            });
            set(new GUIQueryItem(16) {
                @Override
                public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                    return null;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§6Custom Price",
                            Material.OAK_SIGN, 1,
                            "§8" + (isSellOrder ? "Sell Offer" : "Buy Offer") + " Setup",
                            " ",
                            "§7Set the price per unit you're willing",
                            "§7to pay. Minimum 50% of the best order.",
                            " ",
                            "§7Ordering: §a" +  amount + "§7x",
                            " ",
                            "§eClick to specify!");
                }
            });

            refreshItems(player);
        });

        return future;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        future.complete(0D);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
        future.complete(0D);
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
    public void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.BAZAAR).isOnline(new ProtocolPingSpecification()).join()) {
            player.sendMessage("§cThe Bazaar is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
