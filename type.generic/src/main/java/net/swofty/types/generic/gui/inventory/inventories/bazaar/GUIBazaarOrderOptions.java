package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.bazaar.*;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocolObject.CancelMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocolObject.CancelResponse;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GUIBazaarOrderOptions extends SkyBlockInventoryGUI {
    private final BazaarGetPendingOrdersProtocolObject.PendingOrder order;

    public GUIBazaarOrderOptions(BazaarGetPendingOrdersProtocolObject.PendingOrder order) {
        super("§8Order options", InventoryType.CHEST_4_ROW);
        this.order = order;

        // Black background
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        DecimalFormat formatter = new DecimalFormat("#,###.##");
        boolean isSell = "SELL".equalsIgnoreCase(order.side());

        ItemType itemType;
        try {
            itemType = ItemType.valueOf(order.itemName());
        } catch (IllegalArgumentException e) {
            itemType = null; // Fallback
        }

        set(GUIClickableItem.getGoBackItem(31, new GUIBazaarOrders()));

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                // Cancel order
                CancelMessage cm = new CancelMessage(order.orderId(), p.getUuid());
                p.sendMessage("§6[Bazaar] §7Cancelling order...");
                new ProxyService(ServiceType.BAZAAR)
                        .<CancelMessage, CancelResponse>handleRequest(cm)
                        .thenAccept(res -> {
                            if (res.successful) {
                                double coinsToRefund = order.price() * order.amount();
                                p.sendMessage("§6[Bazaar] §cCancelled! §7Refunded §6" + formatter.format(coinsToRefund) + " coins §7from cancelling order!");
                                p.setCoins(p.getCoins() + coinsToRefund);
                            } else {
                                p.sendMessage("§6[Bazaar] §cFailed to cancel order.");
                            }
                            p.closeInventory();
                        });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7You will be refunded §6" + formatter.format(order.price() * order.amount()) + " coins");
                lore.add("§7from §e" + (int)order.amount() + "x §7missing items.");

                return ItemStackCreator.getStack("§c§lCancel Order",
                        Material.RED_WOOL, 1, lore);
            }
        });

        // FLIP ORDER at slot 15 (right side)
        if (!isSell) { // Only show flip option for buy orders
            set(new GUIClickableItem(15) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                    String[] prompt = {
                            "Flip " + order.itemName(),
                            "Enter new price:"
                    };
                    new SkyBlockSignGUI(p).open(prompt)
                            .thenAccept(text -> {
                                try {
                                    double newPrice = Double.parseDouble(text);
                                    if (newPrice <= 0) throw new NumberFormatException();

                                    // Create a new sell order for the remaining amount
                                    var sellMsg = new BazaarSellProtocolObject.BazaarSellMessage(
                                            order.itemName(),
                                            p.getUuid(),
                                            newPrice,
                                            (int)order.amount()
                                    );
                                    new ProxyService(ServiceType.BAZAAR)
                                            .<BazaarSellProtocolObject.BazaarSellMessage,
                                                    BazaarSellProtocolObject.BazaarSellResponse>handleRequest(sellMsg)
                                            .thenAccept(r2 -> {
                                                if (r2.successful) {
                                                    p.sendMessage("§6[Bazaar] §eFlip order placed at §6"
                                                            + formatter.format(newPrice) + " coins§e.");
                                                } else {
                                                    p.sendMessage("§c[Bazaar] Failed to flip order.");
                                                }
                                                p.closeInventory();
                                            });
                                } catch (NumberFormatException ex) {
                                    p.sendMessage("§cInvalid price. Flip cancelled.");
                                }
                            });
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer p) {
                    // This will be updated with real data when the item is rendered
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Directly create a new sell offer for");
                    lore.add("§a" + (int)order.amount() + "§8x §7products.");
                    lore.add(" ");
                    lore.add("§7Current unit price: §6" + formatter.format(order.price()) + " coins");
                    lore.add(" ");
                    lore.add("§7Loading market data...");

                    return ItemStackCreator.getStack("§a§lFlip Order",
                            Material.NAME_TAG, 1, lore);
                }
            });
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        // Fetch real market data for the flip order option
        if (!"SELL".equalsIgnoreCase(order.side())) {
            fetchMarketDataAndUpdateFlipButton();
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    private void fetchMarketDataAndUpdateFlipButton() {
        new ProxyService(ServiceType.BAZAAR)
                .<BazaarGetItemProtocolObject.BazaarGetItemMessage, BazaarGetItemProtocolObject.BazaarGetItemResponse>handleRequest(
                        new BazaarGetItemProtocolObject.BazaarGetItemMessage(order.itemName())
                )
                .thenAccept(response -> {
                    // Update the flip button with real market data
                    updateFlipButtonWithMarketData(response.sellOrders());
                })
                .exceptionally(throwable -> {
                    System.err.println("Failed to fetch market data for " + order.itemName() + ": " + throwable.getMessage());
                    return null;
                });
    }

    private void updateFlipButtonWithMarketData(List<BazaarGetItemProtocolObject.OrderRecord> sellOrders) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                String[] prompt = {
                        "§aFlip " + order.itemName(),
                        "Enter new price:"
                };
                new SkyBlockSignGUI(p).open(prompt)
                        .thenAccept(text -> {
                            try {
                                double newPrice = Double.parseDouble(text);
                                if (newPrice <= 0) throw new NumberFormatException();

                                var sellMsg = new BazaarSellProtocolObject.BazaarSellMessage(
                                        order.itemName(),
                                        p.getUuid(),
                                        newPrice,
                                        (int)order.amount()
                                );
                                new ProxyService(ServiceType.BAZAAR)
                                        .<BazaarSellProtocolObject.BazaarSellMessage,
                                                BazaarSellProtocolObject.BazaarSellResponse>handleRequest(sellMsg)
                                        .thenAccept(r2 -> {
                                            if (r2.successful) {
                                                p.sendMessage("§6[Bazaar] §eFlip order placed at §6"
                                                        + formatter.format(newPrice) + " coins§e.");
                                            } else {
                                                p.sendMessage("§c[Bazaar] Failed to flip order.");
                                            }
                                            p.closeInventory();
                                        });
                            } catch (NumberFormatException ex) {
                                p.sendMessage("§cInvalid price. Flip cancelled.");
                            }
                        });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Directly create a new sell offer for");
                lore.add("§a" + (int)order.amount() + "§8x §7products.");
                lore.add(" ");
                lore.add("§7Current unit price: §6" + formatter.format(order.price()) + " coins");
                lore.add(" ");

                if (sellOrders.isEmpty()) {
                    lore.add("§7No current sell offers available");
                } else {
                    lore.add("§eTop Offers:");

                    // Sort sell orders by price (lowest first) and show top offers
                    sellOrders.stream()
                            .sorted((a, b) -> Double.compare(a.price(), b.price()))
                            .limit(7) // Show top 7 offers
                            .forEach(offer -> {
                                lore.add("§7- §6" + formatter.format(offer.price()) + " coins §7each | " +
                                        "§a" + formatter.format(offer.amount()) + "x §7from §e1 offer");
                            });
                }

                lore.add(" ");
                lore.add("§cThis order can't be flipped until it is filled!");

                return ItemStackCreator.getStack("§a§lFlip Order",
                        Material.NAME_TAG, 1, lore);
            }
        });

        // Update the GUI to reflect the changes
        updateItemStacks(getInventory(), getPlayer());
    }
    @Override public void onBottomClick(InventoryPreClickEvent e) { e.setCancelled(true); }
}