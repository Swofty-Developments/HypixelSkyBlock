package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.bazaar.*;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.gui.inventory.*;
import net.swofty.types.generic.gui.inventory.inventories.bazaar.selections.GUIBazaarPriceSelection;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.*;

public class GUIBazaarItem extends SkyBlockInventoryGUI implements RefreshingGUI {
    private final ItemType itemType;

    public GUIBazaarItem(ItemType itemType) {
        super(BazaarCategories.getFromItem(itemType).getKey()
                        + " → " + itemType.getDisplayName(),
                InventoryType.CHEST_4_ROW);

        this.itemType = itemType;

        // Background and "back" button
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(30, new GUIBazaar(BazaarCategories.getFromItem(itemType).getKey())));

        // Manage Orders
        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                new GUIBazaarOrders().open(p);
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                return ItemStackCreator.getStack("§aManage Orders",
                        Material.BOOK, 1,
                        "§7View your pending Bazaar orders",
                        "§eClick to open");
            }
        });

        // Center item preview
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                return new NonPlayerItemUpdater(new SkyBlockItem(itemType))
                        .getUpdatedItem();
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        // Fetch live order book for this item
        new ProxyService(ServiceType.BAZAAR)
                .<BazaarGetItemProtocolObject.BazaarGetItemMessage, BazaarGetItemProtocolObject.BazaarGetItemResponse>handleRequest(
                        new BazaarGetItemProtocolObject.BazaarGetItemMessage(itemType.name())
                )
                .thenAccept(resp -> updateItems(resp));
    }

    private void updateItems(BazaarGetItemProtocolObject.BazaarGetItemResponse response) {
        String itemName = response.itemName();
        List<BazaarGetItemProtocolObject.OrderRecord> buyOrders = response.buyOrders();
        List<BazaarGetItemProtocolObject.OrderRecord> sellOrders = response.sellOrders();

        // Calculate statistics from order books
        BazaarStatistics buyStats = calculateBuyStatistics(buyOrders);
        BazaarStatistics sellStats = calculateSellStatistics(sellOrders);

        // SLOT 10: Instant Buy
        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                int space = p.maxItemFit(itemType);
                if (space <= 0) {
                    p.sendMessage("§cNo room to buy any " + itemType.getDisplayName());
                    return;
                }
                if (sellStats.getLowestPrice() <= 0) {
                    p.sendMessage("§cNo sell orders available for instant buy.");
                    return;
                }
                double unitPrice = sellStats.getLowestPrice() * 1.04; // 4% fee
                var msg = new BazaarBuyProtocolObject.BazaarBuyMessage(
                        itemType.name(), space, (int)Math.ceil(unitPrice), p.getUuid()
                );
                new ProxyService(ServiceType.BAZAAR)
                        .<BazaarBuyProtocolObject.BazaarBuyMessage, BazaarBuyProtocolObject.BazaarBuyResponse>handleRequest(msg)
                        .thenAccept(resp -> {
                            if (resp.successful) {
                                p.sendMessage("§6[Bazaar] Bought " + space
                                        + "x " + itemType.getDisplayName()
                                        + " §afor " + Math.ceil(unitPrice) + " coins each.");
                            } else {
                                p.sendMessage("§c[Bazaar] Instant buy failed.");
                            }
                        });
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§8" + itemType.getDisplayName());
                lore.add(" ");
                if (sellOrders.isEmpty()) {
                    lore.add("§cNo sell offers available");
                    lore.add("§8Cannot buy instantly");
                } else {
                    double lp = sellStats.getLowestPrice();
                    lore.add("§7Unit price: §6" + String.format("%.2f", lp) + " §7(+4% fee)");
                    lore.add("§7Max stack: §6" + p.maxItemFit(itemType));
                    lore.add(" ");
                    lore.add("§eClick to buy instantly");
                }
                return ItemStackCreator.getStack("§aBuy Instantly",
                        Material.GOLDEN_HORSE_ARMOR, 1, lore);
            }
        });

        // SLOT 11: Instant Sell
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                Map<Integer,Integer> inv = p.getAllOfTypeInInventory(itemType);
                int total = inv.values().stream().mapToInt(i->i).sum();
                if (total == 0) {
                    p.sendMessage("§cYou have none to sell.");
                    return;
                }
                if (buyStats.getHighestPrice() <= 0) {
                    p.sendMessage("§cNo buy orders available for instant sell.");
                    return;
                }
                double unitPrice = buyStats.getHighestPrice();
                inv.keySet().forEach(slot -> p.getInventory().setItemStack(slot, ItemStack.AIR));
                var msg = new BazaarSellProtocolObject.BazaarSellMessage(
                        itemType.name(), p.getUuid(), unitPrice, total
                );
                new ProxyService(ServiceType.BAZAAR)
                        .<BazaarSellProtocolObject.BazaarSellMessage, BazaarSellProtocolObject.BazaarSellResponse>handleRequest(msg)
                        .thenAccept(resp -> {
                            if (resp.successful) {
                                p.sendMessage("§6[Bazaar] Sold " + total
                                        + "x " + itemType.getDisplayName()
                                        + " §afor " + String.format("%.2f", unitPrice) + " coins each.");
                            } else {
                                p.sendMessage("§c[Bazaar] Instant sell failed.");
                            }
                        });
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§8" + itemType.getDisplayName());
                lore.add(" ");
                int have = p.getAmountInInventory(itemType);
                if (have == 0) {
                    lore.add("§7You have none");
                    lore.add("§8Cannot sell instantly");
                } else if (buyOrders.isEmpty()) {
                    lore.add("§7You have: §a" + have);
                    lore.add("§cNo buy orders available");
                    lore.add("§8Cannot sell instantly");
                } else {
                    double hp = buyStats.getHighestPrice();
                    lore.add("§7You have: §a" + have);
                    lore.add("§7Unit price: §6" + String.format("%.2f", hp));
                    lore.add(" ");
                    lore.add("§eClick to sell instantly");
                }
                return ItemStackCreator.getStack("§6Sell Instantly",
                        Material.HOPPER, 1, lore);
            }
        });

        // SLOT 15: Create Buy Order
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                new GUIBazaarPriceSelection(
                        GUIBazaarItem.this,
                        p.maxItemFit(itemType),
                        sellStats.getLowestPrice(),
                        sellStats.getHighestPrice(),
                        itemType, false
                ).openPriceSelection(p).thenAccept(price -> {
                    if (price <= 0) {
                        p.sendMessage("§cMust pick a positive price.");
                        return;
                    }
                    int qty = p.maxItemFit(itemType);
                    var msg = new BazaarBuyProtocolObject.BazaarBuyMessage(
                            itemType.name(), qty, price.intValue(), p.getUuid()
                    );
                    p.sendMessage("§6[Bazaar] §7Putting goods in escrow...");
                    double coinsToTake = price * qty;
                    if (coinsToTake > p.getCoins()) {
                        p.sendMessage("§6[Bazaar] §cInsufficient funds to complete escrow!");
                        return;
                    }
                    p.setCoins(p.getCoins() - coinsToTake);
                    p.sendMessage("§6[Bazaar] §7Submitting buy order...");
                    new ProxyService(ServiceType.BAZAAR)
                            .<BazaarBuyProtocolObject.BazaarBuyMessage, BazaarBuyProtocolObject.BazaarBuyResponse>handleRequest(msg)
                            .thenAccept(resp -> {
                                if (resp.successful) {
                                    p.sendMessage("§6[Bazaar] §eBuy order setup! §a"
                                            + qty + "§7x " + itemType.getDisplayName() + " §7for §6" + price + " coins§7!");
                                    p.closeInventory();
                                } else {
                                    p.setCoins(p.getCoins() + coinsToTake);
                                    p.sendMessage("§6[Bazaar] §cFailed! §7Refunded §6" + new DecimalFormat("#,###.##").format(coinsToTake) + " coins §7from escrow!");
                                }
                            });
                });
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§8Create a limit‑buy order");
                if (sellStats.getLowestPrice() > 0) {
                    lore.add("§7Best ask: §6" + String.format("%.2f", sellStats.getLowestPrice()));
                } else {
                    lore.add("§7No current sell orders");
                }
                lore.add(" ");
                lore.add("§eClick to choose price");
                return ItemStackCreator.getStack("§aCreate Buy Order",
                        Material.LIME_STAINED_GLASS_PANE, 1, lore);
            }
        });

        // SLOT 16: Create Sell Offer
        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                Map<Integer,Integer> inv = p.getAllOfTypeInInventory(itemType);
                int total = inv.values().stream().mapToInt(i->i).sum();
                if (total == 0) {
                    p.sendMessage("§cYou have none to list.");
                    return;
                }
                new GUIBazaarPriceSelection(
                        GUIBazaarItem.this,
                        total,
                        buyStats.getHighestPrice(),
                        buyStats.getLowestPrice(),
                        itemType, true
                ).openPriceSelection(p).thenAccept(price -> {
                    if (price <= 0) {
                        p.sendMessage("§cMust pick a positive price.");
                        return;
                    }
                    p.sendMessage("§6[Bazaar] §7Putting goods in escrow...");
                    List<SkyBlockItem> items = p.takeItem(itemType, total);
                    if (items == null) {
                        p.sendMessage("§6[Bazaar] §cYou don't have enough " + itemType.getDisplayName() + " to sell!");
                        return;
                    }
                    double coinsToTake = price * total;
                    if (coinsToTake > p.getCoins()) {
                        p.sendMessage("§6[Bazaar] §cInsufficient funds to complete escrow!");
                        return;
                    }
                    p.setCoins(p.getCoins() - coinsToTake);
                    p.sendMessage("§6[Bazaar] §7Submitting sell order...");
                    var msg = new BazaarSellProtocolObject.BazaarSellMessage(
                            itemType.name(), p.getUuid(), price, total
                    );

                    new ProxyService(ServiceType.BAZAAR)
                            .<BazaarSellProtocolObject.BazaarSellMessage, BazaarSellProtocolObject.BazaarSellResponse>handleRequest(msg)
                            .thenAccept(resp -> {
                                if (resp.successful) {
                                    p.sendMessage("§6[Bazaar] §eSell order setup! §a"
                                            + total + "§7x " + itemType.getDisplayName() + " §7for §6" + price + " coins§7!");
                                    p.closeInventory();
                                } else {
                                    p.setCoins(p.getCoins() + coinsToTake);
                                    p.sendMessage("§6[Bazaar] §cFailed! §7Refunded §6" + new DecimalFormat("#,###.##").format(coinsToTake) + " coins §7from escrow!");
                                }
                            });
                });
            }
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§8Create a limit‑sell order");
                if (buyStats.getHighestPrice() > 0) {
                    lore.add("§7Best bid: §6" + String.format("%.2f", buyStats.getHighestPrice()));
                } else {
                    lore.add("§7No current buy orders");
                }
                lore.add(" ");
                lore.add("§eClick to choose price");
                return ItemStackCreator.getStack("§6Create Sell Offer",
                        Material.PAPER, 1, lore);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
    }

    /**
     * Calculate statistics for buy orders (highest price = best bid)
     */
    private BazaarStatistics calculateBuyStatistics(List<BazaarGetItemProtocolObject.OrderRecord> buyOrders) {
        if (buyOrders.isEmpty()) {
            return new BazaarStatistics(0, 0);
        }

        double highestPrice = buyOrders.stream()
                .mapToDouble(BazaarGetItemProtocolObject.OrderRecord::price)
                .max()
                .orElse(0);

        double lowestPrice = buyOrders.stream()
                .mapToDouble(BazaarGetItemProtocolObject.OrderRecord::price)
                .min()
                .orElse(0);

        return new BazaarStatistics(highestPrice, lowestPrice);
    }

    /**
     * Calculate statistics for sell orders (lowest price = best ask)
     */
    private BazaarStatistics calculateSellStatistics(List<BazaarGetItemProtocolObject.OrderRecord> sellOrders) {
        if (sellOrders.isEmpty()) {
            return new BazaarStatistics(0, 0);
        }

        double highestPrice = sellOrders.stream()
                .mapToDouble(BazaarGetItemProtocolObject.OrderRecord::price)
                .max()
                .orElse(0);

        double lowestPrice = sellOrders.stream()
                .mapToDouble(BazaarGetItemProtocolObject.OrderRecord::price)
                .min()
                .orElse(0);

        return new BazaarStatistics(highestPrice, lowestPrice);
    }

    /**
     * Helper class to hold price statistics
     */
    private static class BazaarStatistics {
        private final double highestPrice;
        private final double lowestPrice;

        public BazaarStatistics(double highestPrice, double lowestPrice) {
            this.highestPrice = highestPrice;
            this.lowestPrice = lowestPrice;
        }

        public double getHighestPrice() {
            return highestPrice;
        }

        public double getLowestPrice() {
            return lowestPrice;
        }
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        boolean online = new ProxyService(ServiceType.BAZAAR).isOnline().join();
        if (!online) {
            player.sendMessage("§cThe Bazaar is offline!");
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 10;
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