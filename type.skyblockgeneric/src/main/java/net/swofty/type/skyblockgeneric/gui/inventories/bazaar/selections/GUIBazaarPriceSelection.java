package net.swofty.type.skyblockgeneric.gui.inventories.bazaar.selections;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class GUIBazaarPriceSelection extends HypixelInventoryGUI implements RefreshingGUI {
    private CompletableFuture<Double> future = new CompletableFuture<>();
    private final boolean isSellOrder;
    private final ItemType itemTypeLinker;
    private final Double lowestPrice;
    private final Double highestPrice;
    private final Integer amount;


    public GUIBazaarPriceSelection(HypixelInventoryGUI previousGUI, Integer amount,
                                   Double lowestPrice, Double highestPrice,
                                   ItemType itemTypeLinker, boolean isSellOrder) {
        super(isSellOrder ? I18n.t("gui_bazaar.price_selection.title_sell") : I18n.t("gui_bazaar.price_selection.title_buy"), InventoryType.CHEST_4_ROW);

        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        this.itemTypeLinker = itemTypeLinker;
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
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    future.complete(spreadPrice);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    Locale l = p.getLocale();
                    return ItemStackCreator.getStack(I18n.string("gui_bazaar.price_selection.spread_10pct", l),
                            Material.GOLDEN_HORSE_ARMOR, 1,
                            isSellOrder ? I18n.string("gui_bazaar.price_selection.offer_setup_sell", l) : I18n.string("gui_bazaar.price_selection.offer_setup_buy", l),
                            " ",
                        I18n.string("gui_bazaar.price_selection.lowest_price", l, Component.text(lowestPrice)),
                        I18n.string("gui_bazaar.price_selection.highest_price", l, Component.text(highestPrice)),
                        I18n.string("gui_bazaar.price_selection.spread", l, Component.text(highestPrice), Component.text(lowestPrice), Component.text(spread)),
                            " ",
                        isSellOrder ? I18n.string("gui_bazaar.price_selection.selling_amount", l, Component.text(amount)) : I18n.string("gui_bazaar.price_selection.buying_amount", l, Component.text(amount)),
                        I18n.string("gui_bazaar.price_selection.unit_price", l, Component.text(spreadPrice)),
                            " ",
                        I18n.string("gui_bazaar.price_selection.total", l, Component.text(spreadPrice * amount)),
                            " ",
                            I18n.string("gui_bazaar.price_selection.click_to_use", l));
                }
            });


            double incrementedOffer = isSellOrder ? lowestPrice - 0.1 : highestPrice + 0.1;
            set(new GUIClickableItem(12) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    future.complete(incrementedOffer);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    Locale l = p.getLocale();
                    List<Component> lore = new ArrayList<>();
                    lore.add(isSellOrder ? I18n.t("gui_bazaar.price_selection.offer_setup_sell") : I18n.t("gui_bazaar.price_selection.offer_setup_buy"));
                    lore.add(Component.space());
                    lore.addAll(List.of(I18n.iterable("gui_bazaar.price_selection.best_offer_beat")));
                    lore.add(Component.space());
                    lore.add(isSellOrder ? I18n.t("gui_bazaar.price_selection.selling_amount", Component.text(amount)) : I18n.t("gui_bazaar.price_selection.buying_amount", Component.text(amount)));
                    lore.add(I18n.t("gui_bazaar.price_selection.unit_price", Component.text(incrementedOffer)));
                    lore.add(Component.space());
                    lore.add(I18n.t("gui_bazaar.price_selection.total", Component.text(incrementedOffer * amount)));
                    lore.add(Component.space());
                    lore.add(I18n.t("gui_bazaar.price_selection.click_to_use"));
                    return ItemStackCreator.getStack(isSellOrder ? I18n.t("gui_bazaar.price_selection.best_offer_minus") : I18n.t("gui_bazaar.price_selection.best_offer_plus"),
                            Material.GOLD_NUGGET, 1, lore);
                }
            });

            double bestOffer = isSellOrder ? highestPrice : lowestPrice;
            set(new GUIClickableItem(10) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    future.complete(bestOffer);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    Locale l = p.getLocale();
                    List<Object> lore = new java.util.ArrayList<>();
                    lore.add(isSellOrder ? I18n.string("gui_bazaar.price_selection.offer_setup_sell", l) : I18n.string("gui_bazaar.price_selection.offer_setup_buy", l));
                    lore.add(" ");
                    lore.addAll(List.of(I18n.iterable("gui_bazaar.price_selection.same_as_best.lore")));
                    lore.add(" ");
                    lore.add(isSellOrder ? I18n.string("gui_bazaar.price_selection.selling_amount", l, Component.text(amount)) : I18n.string("gui_bazaar.price_selection.buying_amount", l, Component.text(amount)));
                    lore.add(I18n.string("gui_bazaar.price_selection.unit_price", l, Component.text(bestOffer)));
                    lore.add(" ");
                    lore.add(I18n.string("gui_bazaar.price_selection.total", l, Component.text(bestOffer * amount)));
                    lore.add(" ");
                    lore.add(I18n.string("gui_bazaar.price_selection.click_to_use", l));
                    return ItemStackCreator.getStack(I18n.string("gui_bazaar.price_selection.same_as_best", l),
                            itemTypeLinker.material, 1, lore);
                }
            });
            set(new GUIQueryItem(16) {
                @Override
                public HypixelInventoryGUI onQueryFinish(String query, HypixelPlayer player) {
                    try {
                        double price = Double.parseDouble(query);
                        if (price <= 0) {
                            throw new NumberFormatException();
                        }
                        future.complete(price);
                        return null;
                    } catch (NumberFormatException e) {
                        player.sendMessage(I18n.t("gui_bazaar.price_selection.invalid_price"));
                        return null;
                    }
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    Locale l = p.getLocale();
                    List<Object> lore = new java.util.ArrayList<>();
                    lore.add(isSellOrder ? I18n.string("gui_bazaar.price_selection.offer_setup_sell", l) : I18n.string("gui_bazaar.price_selection.offer_setup_buy", l));
                    lore.add(" ");
                    lore.addAll(List.of(I18n.iterable("gui_bazaar.price_selection.custom_price.lore")));
                    lore.add(" ");
                    lore.add(I18n.string("gui_bazaar.price_selection.ordering_amount", l, Component.text(amount)));
                    lore.add(" ");
                    lore.add(I18n.string("gui_bazaar.price_selection.custom_click", l));
                    return ItemStackCreator.getStack(I18n.string("gui_bazaar.price_selection.custom_price", l),
                            Material.OAK_SIGN, 1, lore);
                }
            });

            refreshItems(player);
        });

        return future;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        if (reason == CloseReason.SIGN_OPENED) return;
        future.complete(0D);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
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
    public void refreshItems(HypixelPlayer player) {
        if (!new ProxyService(ServiceType.BAZAAR).isOnline().join()) {
            player.sendMessage(I18n.t("gui_bazaar.price_selection.offline_message"));
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
