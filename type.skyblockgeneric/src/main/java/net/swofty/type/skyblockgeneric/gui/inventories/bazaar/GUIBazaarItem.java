package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
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
import java.util.Locale;
import java.util.Map;

public class GUIBazaarItem extends HypixelInventoryGUI implements RefreshingGUI {
    private final ItemType itemType;
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");
    private BazaarConnector.BazaarStatistics currentStats;

    public GUIBazaarItem(ItemType itemType) {
        super(I18n.string("gui_bazaar.item.title", Component.text(BazaarCategories.getFromItem(itemType).getValue().displayName), Component.text(itemType.getDisplayName())), InventoryType.CHEST_4_ROW);
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
                return TranslatableItemStackCreator.getStack("gui_bazaar.item.manage_orders_button", Material.BOOK, 1,
                        "gui_bazaar.item.manage_orders_button.lore");
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
                return TranslatableItemStackCreator.getStackHead("gui_bazaar.item.go_back_bazaar", "c232e3820897429157619b0ee099fec0628f602fff12b695de54aef11d923ad7", 1,
                        "gui_bazaar.item.go_back_bazaar.lore");
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
                    p.sendMessage(I18n.t("gui_bazaar.item.buy_no_offers_message"));
                    return;
                }

                int maxSpace = player.maxItemFit(itemType);
                if (maxSpace <= 0) {
                    p.sendMessage(I18n.t("gui_bazaar.item.buy_inventory_full"));
                    return;
                }

                double priceWithFee = stats.bestAsk() * 1.04;

                new GUIBazaarOrderAmountSelection(GUIBazaarItem.this, itemType, true, true, maxSpace, priceWithFee)
                        .openAmountSelection(player)
                        .thenAccept(amount -> {
                            if (amount <= 0) return;

                            double totalCost = priceWithFee * amount;
                            if (totalCost > player.getCoins()) {
                                p.sendMessage(I18n.string("gui_bazaar.item.buy_need_coins", p.getLocale(), Component.text(FORMATTER.format(totalCost))));
                                return;
                            }

                            player.getBazaarConnector().instantBuy(itemType, amount)
                                    .thenAccept(result -> {
                                        player.sendMessage(I18n.string("gui_bazaar.item.bazaar_result_prefix", player.getLocale()) + " " + (result.success() ? "§a" : "§c") + result.message());
                                        if (result.success()) p.closeInventory();
                                    });
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_bazaar.item.buy_instantly_subtitle", l));
                lore.add(" ");

                if (stats.bestAsk() <= 0) {
                    lore.add(I18n.string("gui_bazaar.item.buy_no_offers", l));
                } else {
                    double priceWithFee = stats.bestAsk() * 1.04;
                    lore.add(I18n.string("gui_bazaar.item.buy_price", l, Component.text(FORMATTER.format(priceWithFee))));
                    lore.add(I18n.string("gui_bazaar.item.buy_max_space", l, Component.text(String.valueOf(player.maxItemFit(itemType)))));
                    lore.add(" ");
                    lore.add(I18n.string("gui_bazaar.item.buy_click", l));
                }

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.item.buy_instantly", l), Material.GOLDEN_HORSE_ARMOR, 1, lore);
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int have = player.getAmountInInventory(itemType);
                if (have <= 0) {
                    p.sendMessage(I18n.string("gui_bazaar.item.sell_no_items", p.getLocale(), Component.text(itemType.getDisplayName())));
                    return;
                }

                if (stats.bestBid() <= 0) {
                    p.sendMessage(I18n.t("gui_bazaar.item.sell_no_orders_message"));
                    return;
                }

                new GUIBazaarOrderAmountSelection(GUIBazaarItem.this, itemType, false, true, have, stats.bestBid())
                        .openAmountSelection(player)
                        .thenAccept(amount -> {
                            if (amount <= 0) return;

                            player.getBazaarConnector().instantSell(itemType)
                                    .thenAccept(result -> {
                                        p.sendMessage(I18n.string("gui_bazaar.item.bazaar_result_prefix", p.getLocale()) + " " + (result.success() ? "§a" : "§c") + result.message());
                                        if (result.success()) p.closeInventory();
                                    });
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_bazaar.item.sell_instantly_subtitle", l));
                lore.add(" ");

                int have = player.getAmountInInventory(itemType);
                if (have <= 0) {
                    lore.add(I18n.string("gui_bazaar.item.sell_have_zero", l));
                } else if (stats.bestBid() <= 0) {
                    lore.add(I18n.string("gui_bazaar.item.sell_have", l, Component.text(String.valueOf(have))));
                    lore.add(I18n.string("gui_bazaar.item.sell_no_orders", l));
                } else {
                    lore.add(I18n.string("gui_bazaar.item.sell_have", l, Component.text(String.valueOf(have))));
                    lore.add(I18n.string("gui_bazaar.item.sell_price", l, Component.text(FORMATTER.format(stats.bestBid()))));
                    lore.add(" ");
                    lore.add(I18n.string("gui_bazaar.item.sell_click", l));
                }

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.item.sell_instantly", l), Material.HOPPER, 1, lore);
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
                                Locale l = p.getLocale();
                                if (totalCost > player.getCoins()) {
                                    p.sendMessage(I18n.string("gui_bazaar.item.buy_order_need_coins", l, Component.text(FORMATTER.format(totalCost))));
                                    new GUIBazaarItem(itemType).open(p);
                                    return;
                                }

                                player.removeCoins(totalCost);
                                p.sendMessage(I18n.string("gui_bazaar.item.buy_order_escrow", l, Component.text(FORMATTER.format(totalCost))));

                                player.getBazaarConnector().createBuyOrder(itemType, price, amount)
                                        .thenAccept(result -> {
                                            if (result.success()) {
                                                p.sendMessage(I18n.string("gui_bazaar.item.buy_order_created", l, Component.text(String.valueOf(amount)), Component.text(FORMATTER.format(price))));
                                                p.closeInventory();
                                            } else {
                                                player.addCoins(totalCost);
                                                p.sendMessage(I18n.string("gui_bazaar.item.buy_order_failed", l, Component.text(FORMATTER.format(totalCost))));
                                                new GUIBazaarItem(itemType).open(p);
                                            }
                                        });
                            });
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_bazaar.item.create_buy_order_subtitle", l));
                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.item.create_buy_order_max", l));
                if (stats.bestAsk() > 0) {
                    lore.add(I18n.string("gui_bazaar.item.create_buy_order_best_ask", l, Component.text(FORMATTER.format(stats.bestAsk()))));
                }
                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.item.create_buy_order_click", l));

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.item.create_buy_order", l), Material.FILLED_MAP, 1, lore);
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int have = player.getAmountInInventory(itemType);
                if (have <= 0) {
                    p.sendMessage(I18n.string("gui_bazaar.item.sell_order_no_items", p.getLocale(), Component.text(itemType.getDisplayName())));
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
                                Locale l = p.getLocale();
                                if (items == null) {
                                    p.sendMessage(I18n.t("gui_bazaar.item.sell_order_remove_fail"));
                                    new GUIBazaarItem(itemType).open(p);
                                    return;
                                }

                                p.sendMessage(I18n.string("gui_bazaar.item.sell_order_escrow", l, Component.text(String.valueOf(amount)), Component.text(itemType.getDisplayName())));

                                player.getBazaarConnector().createSellOrder(itemType, price, amount)
                                        .thenAccept(result -> {
                                            if (result.success()) {
                                                p.sendMessage(I18n.string("gui_bazaar.item.sell_order_created", l, Component.text(String.valueOf(amount)), Component.text(FORMATTER.format(price))));
                                                p.closeInventory();
                                            } else {
                                                player.addAndUpdateItem(items);
                                                p.sendMessage(I18n.t("gui_bazaar.item.sell_order_failed"));
                                                new GUIBazaarItem(itemType).open(p);
                                            }
                                        });
                            });
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_bazaar.item.create_sell_order_subtitle", l));
                lore.add(" ");
                int have = player.getAmountInInventory(itemType);
                lore.add(I18n.string("gui_bazaar.item.create_sell_order_have", l, Component.text(String.valueOf(have))));
                if (stats.bestBid() > 0) {
                    lore.add(I18n.string("gui_bazaar.item.create_sell_order_best_bid", l, Component.text(FORMATTER.format(stats.bestBid()))));
                }
                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.item.create_sell_order_click", l));

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.item.create_sell_order", l), Material.MAP, 1, lore);
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
