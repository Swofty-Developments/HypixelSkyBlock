package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.bazaar.BazaarCategories;
import net.swofty.type.skyblockgeneric.bazaar.BazaarItemSet;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.EnchantedComponent;
import net.swofty.type.skyblockgeneric.item.components.SkullHeadComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static net.swofty.type.generic.gui.inventory.ItemStackCreator.*;

public class GUIBazaarItemSet extends HypixelInventoryGUI implements RefreshingGUI {
    private static final Map<Integer, int[]> SLOTS = Map.of(
            1, new int[]{13},
            2, new int[]{12, 14},
            3, new int[]{11, 13, 15},
            4, new int[]{10, 12, 14, 16},
            5, new int[]{11, 12, 13, 14, 15},
            6, new int[]{11, 12, 13, 14, 15, 22},
            7, new int[]{10, 11, 12, 13, 14, 15, 16},
            8, new int[]{11, 12, 13, 14, 15, 21, 22, 23},
            9, new int[]{10, 11, 12, 13, 14, 15, 16, 21, 23},
            10, new int[]{11, 12, 13, 14, 15, 20, 21, 22, 23, 24}
    );

    private final BazaarItemSet itemSet;

    public GUIBazaarItemSet(BazaarCategories category, BazaarItemSet itemSet) {
        super(I18n.t("gui_bazaar.item_set.title", Component.text(StringUtility.toNormalCase(category.name())), Component.text(itemSet.displayName)), InventoryType.CHEST_4_ROW);

        this.itemSet = itemSet;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));
        set(GUIClickableItem.getGoBackItem(30, new GUIBazaar(category)));
        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBazaarOrders().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return TranslatableItemStackCreator.getStack("gui_bazaar.item_set.manage_orders_button", Material.BOOK, 1,
                        "gui_bazaar.item_set.manage_orders_button.lore");
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        int i = 0;
        for (ItemType itemType : itemSet.items) {
            int slot = SLOTS.get(itemSet.items.size())[i];

            CompletableFuture<Void> future = ((SkyBlockPlayer) e.player()).getBazaarConnector().getItemStatistics(itemType)
                    .thenAccept(stats -> {
                        set(new GUIClickableItem(slot) {
                            @Override
                            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                                SkyBlockPlayer player = (SkyBlockPlayer) p;
                                new GUIBazaarItem(itemType).open(player);
                            }

                            @Override
                            public ItemStack.Builder getItem(HypixelPlayer p) {
                                SkyBlockPlayer player = (SkyBlockPlayer) p;
                                Locale l = p.getLocale();
                                List<String> lore = new ArrayList<>();
                                lore.add(I18n.string("gui_bazaar.item_set.commodity_label", l, Component.text(StringUtility.toNormalCase(itemType.rarity.name()))));
                                lore.add(" ");

                                if (stats.bestAsk() > 0) {
                                    lore.add(I18n.string("gui_bazaar.item_set.buy_price", l, Component.text(new DecimalFormat("#,###").format(stats.bestAsk()))));
                                    lore.add(I18n.string("gui_bazaar.item_set.buy_price_best", l, Component.text(StringUtility.shortenNumber(stats.bestAsk()))));
                                } else {
                                    lore.add(I18n.string("gui_bazaar.item_set.buy_price_none", l));
                                    lore.add(I18n.string("gui_bazaar.item_set.buy_price_none_desc", l));
                                }

                                lore.add(" ");

                                if (stats.bestBid() > 0) {
                                    lore.add(I18n.string("gui_bazaar.item_set.sell_price", l, Component.text(new DecimalFormat("#,###").format(stats.bestBid()))));
                                    lore.add(I18n.string("gui_bazaar.item_set.sell_price_best", l, Component.text(StringUtility.shortenNumber(stats.bestBid()))));
                                } else {
                                    lore.add(I18n.string("gui_bazaar.item_set.sell_price_none", l));
                                    lore.add(I18n.string("gui_bazaar.item_set.sell_price_none_desc", l));
                                }

                                lore.add(" ");
                                lore.add(I18n.string("gui_bazaar.item_set.click_to_view", l));

                                SkyBlockItem item = new SkyBlockItem(itemType);

                                return ItemStackCreator.updateLore(getFromSkyBlockItem(item), lore);
                            }
                        });
                    })
                    .exceptionally(throwable -> {
                        // Handle errors gracefully
                        set(new GUIClickableItem(slot) {
                            @Override
                            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                                SkyBlockPlayer player = (SkyBlockPlayer) p;
                                new GUIBazaarItem(itemType).open(player);
                            }

                            @Override
                            public ItemStack.Builder getItem(HypixelPlayer p) {
                                SkyBlockPlayer player = (SkyBlockPlayer) p;
                                Locale l = p.getLocale();
                                List<String> lore = new ArrayList<>();
                                lore.add(I18n.string("gui_bazaar.item_set.commodity_label", l, Component.text(StringUtility.toNormalCase(itemType.rarity.name()))));
                                lore.add(" ");
                                lore.add(I18n.string("gui_bazaar.item_set.error_loading", l));
                                lore.add(I18n.string("gui_bazaar.item_set.error_try_again", l));
                                lore.add(" ");
                                lore.add(I18n.string("gui_bazaar.item_set.click_to_view", l));

                                return getStack(
                                        itemType.rarity.getColor() + itemType.getDisplayName(),
                                        itemType.material, 1, lore);
                            }
                        });
                        return null;
                    });

            futures.add(future);
            i++;
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> updateItemStacks(getInventory(), getPlayer()));
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        SkyBlockItem clickedItem = new SkyBlockItem(e.getClickedItem());
        ItemType type = clickedItem.getAttributeHandler().getPotentialType();
        e.setCancelled(true);

        if (clickedItem.isNA()) {
            return;
        }

        if (type == null) {
            return;
        }

        Map.Entry<BazaarCategories, BazaarItemSet> entry = BazaarCategories.getFromItem(type);

        if (entry == null) {
            return;
        }

        Thread.startVirtualThread(() -> {
            new GUIBazaarItemSet(entry.getKey(), entry.getValue()).open((SkyBlockPlayer) e.getPlayer());
        });
    }

    @Override
    public void refreshItems(HypixelPlayer p) {
        if (!(p instanceof SkyBlockPlayer player)) {
            return;
        }
        player.getBazaarConnector().isOnline().thenAccept(online -> {
            if (!online) {
                player.sendMessage(I18n.t("gui_bazaar.item_set.offline_message"));
                player.closeInventory();
            }
        });
    }

    @Override
    public int refreshRate() {
        return 10;
    }

    /**
     * Creates an {@link ItemStack.Builder} from an existing {@link SkyBlockItem}.
     *
     * @param item the original {@link SkyBlockItem} to create a builder from
     * @return an {@link ItemStack.Builder} with the properties of the original item
     * @implNote moved from {@link ItemStackCreator} to here until a better place is found
     */
    private ItemStack.Builder getFromSkyBlockItem(SkyBlockItem item) {
        ItemStack.Builder builder;

        if (item.hasComponent(SkullHeadComponent.class)) {
            builder = getStackHead(item.getDisplayName(), item.getComponent(SkullHeadComponent.class).getSkullTexture(item), item.getAmount());
        } else {
            builder = getStack(item.getDisplayName(), item.getMaterial(), item.getAmount());
        }

        if (item.hasComponent(EnchantedComponent.class)) return enchant(builder);
        else return builder;
    }
}
