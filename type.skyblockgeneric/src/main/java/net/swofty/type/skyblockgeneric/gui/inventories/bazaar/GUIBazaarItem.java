package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.bazaar.BazaarCategories;
import net.swofty.type.skyblockgeneric.bazaar.BazaarConnector;
import net.swofty.type.skyblockgeneric.bazaar.BazaarItemSet;
import net.swofty.type.skyblockgeneric.gui.inventories.bazaar.selections.GUIBazaarOrderAmountSelection;
import net.swofty.type.skyblockgeneric.gui.inventories.bazaar.selections.GUIBazaarPriceSelection;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIBazaarItem extends HypixelInventoryGUI implements RefreshingGUI {
    private final ItemType itemType;
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");
    private BazaarConnector.BazaarStatistics currentStats;

    public GUIBazaarItem(ItemType itemType) {
        super(BazaarCategories.getFromItem(itemType).getValue().displayName + " → " + itemType.getDisplayName(), InventoryType.CHEST_4_ROW);
        this.itemType = itemType;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        Map.Entry<BazaarCategories, BazaarItemSet> bazaarCategory = BazaarCategories.getFromItem(itemType);
        set(GUIClickableItem.getGoBackItem(30, new GUIBazaarItemSet(bazaarCategory.getKey(), bazaarCategory.getValue())));

        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBazaarOrders().open(p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aManage Orders", Material.BOOK, 1,
                        "§7View your pending Bazaar orders", "§eClick to open");
            }
        });

        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBazaar(BazaarCategories.getFromItem(itemType).getKey()).open(p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStackHead("§6Go Back", "c232e3820897429157619b0ee099fec0628f602fff12b695de54aef11d923ad7", 1,
                        "§7To Bazaar");
            }
        });

        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return new NonPlayerItemUpdater(new SkyBlockItem(itemType)).getUpdatedItem();
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        player.getBazaarConnector().getItemStatistics(itemType)
                .thenAccept(stats -> {
                    this.currentStats = stats;
                    updateItems(stats);
                });
    }

    private void updateItems(BazaarConnector.BazaarStatistics stats) {
        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (stats.bestAsk() <= 0) {
                    p.sendMessage("§6[Bazaar] §cNo sell offers available!");
                    return;
                }

                int maxSpace = player.maxItemFit(itemType);
                if (maxSpace <= 0) {
                    p.sendMessage("§6[Bazaar] §cInventory full!");
                    return;
                }

                double priceWithFee = stats.bestAsk() * 1.04;

                new GUIBazaarOrderAmountSelection(GUIBazaarItem.this, itemType, true, true, maxSpace, priceWithFee)
                        .openAmountSelection(player)
                        .thenAccept(amount -> {
                            if (amount <= 0) return;

                            double totalCost = priceWithFee * amount;
                            if (totalCost > player.getCoins()) {
                                p.sendMessage("§6[Bazaar] §cNeed " + FORMATTER.format(totalCost) + " coins!");
                                return;
                            }

                            player.getBazaarConnector().instantBuy(itemType, amount)
                                    .thenAccept(result -> {
                                        player.sendMessage("§6[Bazaar] " + (result.success() ? "§a" : "§c") + result.message());
                                        if (result.success()) p.closeInventory();
                                    });
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                lore.add("§8Instant Buy");
                lore.add(" ");

                if (stats.bestAsk() <= 0) {
                    lore.add("§cNo sell offers available");
                } else {
                    double priceWithFee = stats.bestAsk() * 1.04;
                    lore.add("§7Price: §6" + FORMATTER.format(priceWithFee) + " coins §7(+4% fee)");
                    lore.add("§7Max space: §e" + player.maxItemFit(itemType) + "x");
                    lore.add(" ");
                    lore.add("§eClick to select amount!");
                }

                return ItemStackCreator.getStack("§aBuy Instantly", Material.GOLDEN_HORSE_ARMOR, 1, lore);
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int have = player.getAmountInInventory(itemType);
                if (have <= 0) {
                    p.sendMessage("§6[Bazaar] §cYou don't have any " + itemType.getDisplayName() + "!");
                    return;
                }

                if (stats.bestBid() <= 0) {
                    p.sendMessage("§6[Bazaar] §cNo buy orders available!");
                    return;
                }

                new GUIBazaarOrderAmountSelection(GUIBazaarItem.this, itemType, false, true, have, stats.bestBid())
                        .openAmountSelection(player)
                        .thenAccept(amount -> {
                            if (amount <= 0) return;

                            player.getBazaarConnector().instantSell(itemType)
                                    .thenAccept(result -> {
                                        p.sendMessage("§6[Bazaar] " + (result.success() ? "§a" : "§c") + result.message());
                                        if (result.success()) p.closeInventory();
                                    });
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                lore.add("§8Instant Sell");
                lore.add(" ");

                int have = player.getAmountInInventory(itemType);
                if (have <= 0) {
                    lore.add("§7You have: §c0x");
                } else if (stats.bestBid() <= 0) {
                    lore.add("§7You have: §e" + have + "x");
                    lore.add("§cNo buy orders available");
                } else {
                    lore.add("§7You have: §e" + have + "x");
                    lore.add("§7Price: §6" + FORMATTER.format(stats.bestBid()) + " coins");
                    lore.add(" ");
                    lore.add("§eClick to select amount!");
                }

                return ItemStackCreator.getStack("§6Sell Instantly", Material.HOPPER, 1, lore);
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;

                new GUIBazaarOrderAmountSelection(GUIBazaarItem.this, itemType, true, false, 71680, player.getCoins())
                        .openAmountSelection(player)
                        .thenAccept(amount -> {
                            if (amount <= 0) return;

                            new GUIBazaarPriceSelection(
                                    GUIBazaarItem.this, amount, stats.bestAsk(), stats.worstAsk(), itemType, false
                            ).openPriceSelection(player).thenAccept(price -> {
                                if (price <= 0) return;

                                double totalCost = price * amount;
                                if (totalCost > player.getCoins()) {
                                    p.sendMessage("§6[Bazaar] §cNeed " + FORMATTER.format(totalCost) + " coins!");
                                    new GUIBazaarItem(itemType).open(p);
                                    return;
                                }

                                player.removeCoins(totalCost);
                                p.sendMessage("§6[Bazaar] §7Escrowing " + FORMATTER.format(totalCost) + " coins...");

                                player.getBazaarConnector().createBuyOrder(itemType, price, amount)
                                        .thenAccept(result -> {
                                            if (result.success()) {
                                                p.sendMessage("§6[Bazaar] §aBuy order created for " + amount + "x at " +
                                                        FORMATTER.format(price) + " coins each!");
                                                p.closeInventory();
                                            } else {
                                                player.addCoins(totalCost);
                                                p.sendMessage("§6[Bazaar] §cFailed! Refunded " + FORMATTER.format(totalCost) + " coins.");
                                                new GUIBazaarItem(itemType).open(p);
                                            }
                                        });
                            });
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                lore.add("§8Create Buy Order");
                lore.add(" ");
                lore.add("§7Max space: §e71680x");
                if (stats.bestAsk() > 0) {
                    lore.add("§7Best ask: §6" + FORMATTER.format(stats.bestAsk()) + " coins");
                }
                lore.add(" ");
                lore.add("§eClick to create order!");

                return ItemStackCreator.getStack("§aCreate Buy Order", Material.FILLED_MAP, 1, lore);
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int have = player.getAmountInInventory(itemType);
                if (have <= 0) {
                    p.sendMessage("§6[Bazaar] §cYou don't have any " + itemType.getDisplayName() + "!");
                    return;
                }

                new GUIBazaarOrderAmountSelection(GUIBazaarItem.this, itemType, false, false, have, 0)
                        .openAmountSelection(player)
                        .thenAccept(amount -> {
                            if (amount <= 0) return;

                            new GUIBazaarPriceSelection(
                                    GUIBazaarItem.this, amount, stats.bestBid(), stats.worstBid(), itemType, true
                            ).openPriceSelection(player).thenAccept(price -> {
                                if (price <= 0) return;

                                var items = player.takeItem(itemType, amount);
                                if (items == null) {
                                    p.sendMessage("§6[Bazaar] §cFailed to remove items!");
                                    new GUIBazaarItem(itemType).open(p);
                                    return;
                                }

                                p.sendMessage("§6[Bazaar] §7Escrowing " + amount + "x " + itemType.getDisplayName() + "...");

                                player.getBazaarConnector().createSellOrder(itemType, price, amount)
                                        .thenAccept(result -> {
                                            if (result.success()) {
                                                p.sendMessage("§6[Bazaar] §aSell order created for " + amount + "x at " +
                                                        FORMATTER.format(price) + " coins each!");
                                                p.closeInventory();
                                            } else {
                                                player.addAndUpdateItem(items);
                                                p.sendMessage("§6[Bazaar] §cFailed! Items returned.");
                                                new GUIBazaarItem(itemType).open(p);
                                            }
                                        });
                            });
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                lore.add("§8Create Sell Order");
                lore.add(" ");
                int have = player.getAmountInInventory(itemType);
                lore.add("§7You have: §e" + have + "x");
                if (stats.bestBid() > 0) {
                    lore.add("§7Best bid: §6" + FORMATTER.format(stats.bestBid()) + " coins");
                }
                lore.add(" ");
                lore.add("§eClick to create order!");

                return ItemStackCreator.getStack("§6Create Sell Order", Material.MAP, 1, lore);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public void refreshItems(HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        if (currentStats != null) {
            player.getBazaarConnector().getItemStatistics(itemType)
                    .thenAccept(stats -> {
                        this.currentStats = stats;
                        updateItems(stats);
                    });
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