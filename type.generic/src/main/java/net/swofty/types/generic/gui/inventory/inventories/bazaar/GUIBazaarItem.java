package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.bazaar.BazaarConnector;
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

        // Background and navigation
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
        // Fetch live statistics for this item
        e.player().getBazaarConnector().getItemStatistics(itemType)
                .thenAccept(this::updateItems);
    }

    private void updateItems(BazaarConnector.BazaarStatistics stats) {
        // SLOT 10: Instant Buy
        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                int maxSpace = p.maxItemFit(itemType);

                p.getBazaarConnector().instantBuy(itemType, maxSpace)
                        .thenAccept(result -> {
                            if (result.success()) {
                                p.sendMessage("§6[Bazaar] §a" + result.message());
                                p.closeInventory();
                            } else {
                                p.sendMessage("§c[Bazaar] " + result.message());
                            }
                        });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§8" + itemType.getDisplayName());
                lore.add(" ");

                if (stats.bestAsk() <= 0) {
                    lore.add("§cNo sell offers available");
                    lore.add("§8Cannot buy instantly");
                } else {
                    double priceWithFee = stats.bestAsk() * 1.04; // 4% fee
                    lore.add("§7Unit price: §6" + String.format("%.2f", priceWithFee) + " coins §7(+4% fee)");
                    lore.add("§7Max available: §6" + p.maxItemFit(itemType));
                    lore.add(" ");
                    lore.add("§eClick to buy instantly");
                }

                return ItemStackCreator.getStack("§aBuy Instantly", Material.GOLDEN_HORSE_ARMOR, 1, lore);
            }
        });

        // SLOT 11: Instant Sell
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                p.getBazaarConnector().instantSell(itemType)
                        .thenAccept(result -> {
                            if (result.success()) {
                                p.sendMessage("§6[Bazaar] §a" + result.message());
                                p.closeInventory();
                            } else {
                                p.sendMessage("§6[Bazaar] §c" + result.message());
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
                } else if (stats.bestBid() <= 0) {
                    lore.add("§7You have: §a" + have);
                    lore.add("§cNo buy orders available");
                    lore.add("§8Cannot sell instantly");
                } else {
                    lore.add("§7You have: §a" + have);
                    lore.add("§7Unit price: §6" + String.format("%.2f", stats.bestBid()));
                    lore.add(" ");
                    lore.add("§eClick to sell instantly");
                }

                return ItemStackCreator.getStack("§6Sell Instantly", Material.HOPPER, 1, lore);
            }
        });

        // SLOT 15: Create Buy Order
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                new GUIBazaarPriceSelection(
                        GUIBazaarItem.this,
                        p.maxItemFit(itemType),
                        stats.bestAsk(),
                        stats.worstAsk(),
                        itemType, false
                ).openPriceSelection(p).thenAccept(price -> {
                    if (price <= 0) {
                        p.sendMessage("§cMust pick a positive price.");
                        return;
                    }

                    int qty = p.maxItemFit(itemType);
                    double totalCost = price * qty;

                    if (totalCost > p.getCoins()) {
                        p.sendMessage("§6[Bazaar] §cInsufficient funds to complete escrow!");
                        return;
                    }

                    // Deduct coins upfront for escrow
                    p.setCoins(p.getCoins() - totalCost);
                    p.sendMessage("§6[Bazaar] §7Putting goods in escrow...");
                    p.sendMessage("§6[Bazaar] §7Submitting buy order...");

                    p.getBazaarConnector().createBuyOrder(itemType, price, qty)
                            .thenAccept(result -> {
                                if (result.success()) {
                                    p.sendMessage("§6[Bazaar] §eBuy Order Setup! §a"
                                            + qty + "§7x " + itemType.getDisplayName() + " §7for §6" +(price * qty) + " coins§7!");
                                    p.closeInventory();
                                } else {
                                    // Refund on failure
                                    p.setCoins(p.getCoins() + totalCost);
                                    p.sendMessage("§6[Bazaar] §cFailed! §7Refunded §6" + new DecimalFormat("#,###.##").format(totalCost) + " coins §7from escrow!");
                                }
                            });
                });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§8Create a limit‑buy order");
                if (stats.bestAsk() > 0) {
                    lore.add("§7Best ask: §6" + String.format("%.2f", stats.bestAsk()));
                } else {
                    lore.add("§7No current sell orders");
                }
                lore.add(" ");
                lore.add("§eClick to choose price");
                return ItemStackCreator.getStack("§aCreate Buy Order", Material.LIME_STAINED_GLASS_PANE, 1, lore);
            }
        });

        // SLOT 16: Create Sell Offer
        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                int total = p.getAmountInInventory(itemType);
                if (total == 0) {
                    p.sendMessage("§cYou have none to list.");
                    return;
                }

                new GUIBazaarPriceSelection(
                        GUIBazaarItem.this,
                        total,
                        stats.bestBid(),
                        stats.worstBid(),
                        itemType, true
                ).openPriceSelection(p).thenAccept(price -> {
                    if (price <= 0) {
                        p.sendMessage("§cMust pick a positive price.");
                        return;
                    }

                    p.sendMessage("§6[Bazaar] §7Putting goods in escrow...");
                    var items = p.takeItem(itemType, total);
                    if (items == null) {
                        p.sendMessage("§6[Bazaar] §cYou don't have enough " + itemType.getDisplayName() + " to sell!");
                        return;
                    }

                    p.sendMessage("§6[Bazaar] §7Submitting sell offer...");

                    p.getBazaarConnector().createSellOrder(itemType, price, total)
                            .thenAccept(result -> {
                                if (result.success()) {
                                    p.sendMessage("§6[Bazaar] §eSell Offer Setup! §a"
                                            + total + "§7x " + itemType.getDisplayName() + " §7for §6" + (price * total) + " coins§7!");
                                    p.closeInventory();
                                } else {
                                    // Return items on failure (this would need to be implemented)
                                    p.sendMessage("§6[Bazaar] §cFailed! " + result.message());
                                }
                            });
                });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§8Create a limit‑sell order");
                if (stats.bestBid() > 0) {
                    lore.add("§7Best bid: §6" + String.format("%.2f", stats.bestBid()));
                } else {
                    lore.add("§7No current buy orders");
                }
                lore.add(" ");
                lore.add("§eClick to choose price");
                return ItemStackCreator.getStack("§6Create Sell Offer", Material.PAPER, 1, lore);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        player.getBazaarConnector().isOnline().thenAccept(online -> {
            if (!online) {
                player.sendMessage("§cThe Bazaar is offline!");
                player.closeInventory();
            }
        });
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