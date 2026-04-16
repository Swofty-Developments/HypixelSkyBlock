package net.swofty.type.skyblockgeneric.gui.inventories.auction;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointAuctionEscrow;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.AuctionCategoryComponent;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GUIAuctionCreateItem extends HypixelInventoryGUI implements RefreshingGUI {
    private final HypixelInventoryGUI previousGUI;

    public GUIAuctionCreateItem(HypixelInventoryGUI previousGUI) {
        super(I18n.string("gui_auction.create.title"), InventoryType.CHEST_6_ROW);

        this.previousGUI = previousGUI;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getGoBackItem(49, previousGUI));

        DatapointAuctionEscrow.AuctionEscrow escrow = ((SkyBlockPlayer) getPlayer()).getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue();
        if (escrow.isBin())
            e.inventory().setTitle(Component.text(I18n.string("gui_auction.create.title_bin", getPlayer().getLocale())));

        set(new GUIClickableItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                if (escrow.getItem() == null)
                    return TranslatableItemStackCreator.getStack(p, "gui_auction.create.select_item", Material.STONE_BUTTON, 1,
                            "gui_auction.create.select_item.lore");

                SkyBlockItem item = escrow.getItem();
                ItemStack itemStack = new NonPlayerItemUpdater(item).getUpdatedItem().build();
                List<String> lore = new ArrayList<>();

                lore.add(" ");
                lore.add(StringUtility.getTextFromComponent(itemStack.get(DataComponents.CUSTOM_NAME)));
                itemStack.get(DataComponents.LORE).forEach(loreEntry -> {
                    lore.add(StringUtility.getTextFromComponent(loreEntry));
                });
                lore.add(" ");
                lore.add(I18n.string("gui_auction.create.auction_for_item_pickup", l));

                return ItemStackCreator.getStack(I18n.string("gui_auction.create.auction_for_item", l), itemStack.material(), itemStack.amount(), lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (escrow.getItem() == null) return;
                player.addAndUpdateItem(escrow.getItem());
                escrow.setItem(null);

                new GUIAuctionCreateItem(previousGUI).open(player);
            }
        });

        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIAuctionDuration().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                if (escrow.isBin()) {
                    lore.addAll(I18n.lore("gui_auction.create.duration_bin.lore", l));
                } else {
                    lore.addAll(I18n.lore("gui_auction.create.duration_normal.lore", l));
                }
                lore.add(" ");
                lore.add(I18n.string("gui_auction.create.duration_extra_fee", l, Map.of("fee", String.valueOf(escrow.getDuration() / 180000))));
                lore.add(" ");
                lore.add(I18n.string("gui_auction.create.duration_click", l));

                return ItemStackCreator.getStack(I18n.string("gui_auction.create.duration_label", l, Map.of("duration", StringUtility.getAuctionSetupFormattedTime(escrow.getDuration()))),
                        Material.CLOCK, 1, lore);
            }
        });
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                escrow.setBin(!escrow.isBin());
                new GUIAuctionCreateItem(previousGUI).open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (escrow.isBin()) {
                    return TranslatableItemStackCreator.getStack(p, "gui_auction.create.switch_to_auction", Material.POWERED_RAIL, 1,
                            "gui_auction.create.switch_to_auction.lore");
                } else {
                    return TranslatableItemStackCreator.getStack(p, "gui_auction.create.switch_to_bin", Material.GOLD_INGOT, 1,
                            "gui_auction.create.switch_to_bin.lore");
                }
            }
        });
        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ProxyService auctionService = new ProxyService(ServiceType.AUCTION_HOUSE);

                Locale l = player.getLocale();
                auctionService.isOnline().thenAccept((response) -> {
                    if (escrow.getItem() == null || !response)
                        return;

                    long fee = (long) ((escrow.getPrice() * 0.05) + ((double) escrow.getDuration() / 180000));
                    DatapointDouble coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class);
                    if (coins.getValue() < fee) {
                        player.sendMessage(I18n.string("gui_auction.create.not_enough_coins", l));
                        return;
                    }
                    coins.setValue(coins.getValue() - fee);

                    player.closeInventory();

                    player.sendMessage(I18n.string("gui_auction.create.escrow_message", l));

                    ItemStack builtItem = new NonPlayerItemUpdater(escrow.getItem()).getUpdatedItem().build();
                    AuctionItem item = new AuctionItem(escrow.getItem().toUnderstandable(), player.getUuid(), escrow.getDuration() + System.currentTimeMillis(),
                            escrow.isBin(), escrow.getPrice());
                    String itemName = StringUtility.getTextFromComponent(builtItem.get(DataComponents.CUSTOM_NAME));

                    AuctionCategories category = AuctionCategories.TOOLS;
                    if (escrow.getItem().hasComponent(AuctionCategoryComponent.class))
                        category = escrow.getItem().getComponent(AuctionCategoryComponent.class).getCategory();

                    player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).clearEscrow();
                    player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().add(item.getUuid());

                    player.sendMessage(I18n.string("gui_auction.create.setup_message", l));

                    AuctionAddItemProtocolObject.AuctionAddItemMessage message =
                            new AuctionAddItemProtocolObject.AuctionAddItemMessage(item, category);
                    CompletableFuture<AuctionAddItemProtocolObject.AuctionAddItemResponse> future =
                            auctionService.handleRequest(message);
                    UUID auctionUUID = future.join().uuid();

                    player.sendMessage(I18n.string("gui_auction.create.started_message", l, Map.of("item_name", itemName)));
                    player.sendMessage(I18n.string("gui_auction.create.started_id", l, Map.of("uuid", auctionUUID.toString())));
                });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                if (escrow.getItem() == null) {
                    return TranslatableItemStackCreator.getStack(p, "gui_auction.create.submit_no_item", Material.RED_TERRACOTTA, 1,
                            "gui_auction.create.submit_no_item.lore");
                } else {
                    ItemStack builtItem = new NonPlayerItemUpdater(escrow.getItem()).getUpdatedItem().build();

                    return ItemStackCreator.getStack(
                            I18n.string("gui_auction.create.submit_ready", l, Map.of("type", escrow.isBin() ? "Bin " : "")),
                            Material.GREEN_TERRACOTTA, 1,
                            I18n.lore("gui_auction.create.submit_ready.lore", l, Map.of(
                                    "item_name", StringUtility.getTextFromComponent(builtItem.get(DataComponents.CUSTOM_NAME)),
                                    "duration", StringUtility.getAuctionSetupFormattedTime(escrow.getDuration()),
                                    "price_label", escrow.isBin() ? I18n.string("gui_auction.create.price_bin_label", l) : I18n.string("gui_auction.create.price_normal_label", l),
                                    "price", StringUtility.commaify(escrow.getPrice()),
                                    "fee", String.valueOf((escrow.getPrice() * 0.05) + (escrow.getDuration() / 180000))
                            )));
                }
            }
        });
        set(new GUIQueryItem(31) {

            @Override
            public HypixelInventoryGUI onQueryFinish(String query, HypixelPlayer player) {
                long val;
                try {
                    val = Long.parseLong(query);
                } catch (NumberFormatException ex) {
                    player.sendMessage(I18n.string("gui_auction.create.number_parse_error", player.getLocale()));
                    return GUIAuctionCreateItem.this;
                }
                if (val <= 50) {
                    player.sendMessage(I18n.string("gui_auction.create.price_too_low", player.getLocale()));
                    return GUIAuctionCreateItem.this;
                }
                escrow.setPrice(val);

                return GUIAuctionCreateItem.this;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                Material material;
                List<String> lore = new ArrayList<>();
                if (escrow.isBin()) {
                    material = Material.GOLD_INGOT;
                    lore.addAll(I18n.lore("gui_auction.create.price_bin.lore", l));
                } else {
                    material = Material.POWERED_RAIL;
                    lore.addAll(I18n.lore("gui_auction.create.price_normal.lore", l));
                }
                lore.add(" ");
                lore.add(I18n.string("gui_auction.create.price_extra_fee", l, Map.of("fee", String.valueOf(escrow.getPrice() * 0.05))));
                lore.add(" ");
                lore.add(I18n.string("gui_auction.create.price_click", l));

                String priceKey = escrow.isBin() ? "gui_auction.create.price_label_bin" : "gui_auction.create.price_label_normal";
                return ItemStackCreator.getStack(
                        I18n.string(priceKey, l, Map.of("price", StringUtility.commaify(escrow.getPrice()))),
                        material, 1, lore);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
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
        ItemStack current = e.getClickedItem();
        SkyBlockItem item = new SkyBlockItem(current);

        if (item.isNA()) return;
        if (item.isAir()) return;

        DatapointAuctionEscrow.AuctionEscrow escrow = ((SkyBlockPlayer) getPlayer()).getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue();

        if (escrow.getItem() != null) {
            e.getPlayer().sendMessage(I18n.string("gui_auction.create.already_have_item", getPlayer().getLocale()));
            return;
        }

        e.setCancelled(true);
        escrow.setItem(item);
        e.getPlayer().getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
        new GUIAuctionCreateItem(previousGUI).open(getPlayer());
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage(I18n.string("gui_auction.create.offline_message", player.getLocale()));
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
