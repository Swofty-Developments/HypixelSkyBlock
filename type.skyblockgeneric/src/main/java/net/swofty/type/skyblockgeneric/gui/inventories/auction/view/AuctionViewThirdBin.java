package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AuctionViewThirdBin implements AuctionView {
    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        if (!item.getBids().isEmpty()) {
            if (item.getBids().getFirst().uuid().equals(player.getUuid())) {
                DatapointUUIDList activeBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
                DatapointUUIDList inactiveBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class);

                if (activeBids.getValue().contains(item.getUuid())) {
                    gui.set(new GUIClickableItem(31) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            Locale l = p.getLocale();
                            player.sendMessage(I18n.string("gui_auction.view_third_bin.claiming_item", l));
                            activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                                remove(item.getUuid());
                            }});
                            inactiveBids.setValue(new ArrayList<>(inactiveBids.getValue()) {{
                                add(item.getUuid());
                            }});

                            player.addAndUpdateItem(item.getItem());

                            player.sendMessage(I18n.string("gui_auction.view_third_bin.claimed_item", l));
                            player.closeInventory();
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return TranslatableItemStackCreator.getStack(p, "gui_auction.view_third_bin.claim_item", Material.GOLD_BLOCK, 1,
                                    "gui_auction.view_third_bin.claim_sold.lore", Map.of(
                                            "buyer_name", SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()),
                                            "price", String.valueOf(item.getBids().getFirst().value())
                                    ));
                        }
                    });
                } else {
                    gui.set(new GUIItem(31) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return TranslatableItemStackCreator.getStack(p, "gui_auction.view_third_bin.item_sold", Material.BEDROCK, 1,
                                    "gui_auction.view_third_bin.item_sold_claimed.lore", Map.of(
                                            "buyer_name", SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()),
                                            "price", String.valueOf(item.getBids().getFirst().value())
                                    ));
                        }
                    });
                }
                return;
            }

            gui.set(new GUIItem(31) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return TranslatableItemStackCreator.getStack(p, "gui_auction.view_third_bin.item_sold", Material.BEDROCK, 1,
                            "gui_auction.view_third_bin.item_sold_other.lore", Map.of(
                                    "buyer_name", SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()),
                                    "price", String.valueOf(item.getBids().getFirst().value())
                            ));
                }
            });
            return;
        }

        gui.set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                double coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();
                if (coins < item.getStartingPrice()) {
                    player.sendMessage(I18n.string("gui_auction.view_third_bin.not_enough_coins", l));
                    return;
                }

                player.sendMessage(I18n.string("gui_auction.view_third_bin.escrow_message", l));
                player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins - item.getStartingPrice());

                player.sendMessage(I18n.string("gui_auction.view_third_bin.processing", l));

                CompletableFuture<AuctionFetchItemProtocolObject.AuctionFetchItemResponse> future = new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(
                        new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(item.getUuid())
                );
                AuctionItem item = future.join().item();

                if (!item.getBids().isEmpty()) {
                    player.sendMessage(I18n.string("gui_auction.view_third_bin.already_sold", l));
                    player.sendMessage(I18n.string("gui_auction.view_third_bin.returning_escrow", l));
                    player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getStartingPrice());
                    return;
                }

                CoopDatabase.Coop originatorCoop = CoopDatabase.getFromMember(item.getOriginator());
                CoopDatabase.Coop purchaserCoop = CoopDatabase.getFromMember(player.getUuid());
                if (originatorCoop != null && purchaserCoop != null && originatorCoop.isSameAs(purchaserCoop)) {
                    player.sendMessage(I18n.string("gui_auction.view_third_bin.same_coop", l));
                    player.sendMessage(I18n.string("gui_auction.view_third_bin.returning_escrow", l));
                    player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getStartingPrice());
                    return;
                }

                DatapointUUIDList activeBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
                activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                    add(item.getUuid());
                }});
                player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).setValue(activeBids.getValue());

                item.setBids(new ArrayList<>(item.getBids()) {{
                    add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(), item.getStartingPrice().longValue()));
                }});
                item.setEndTime(System.currentTimeMillis());

                AuctionAddItemProtocolObject.AuctionAddItemMessage message =
                        new AuctionAddItemProtocolObject.AuctionAddItemMessage(
                                item, AuctionCategories.TOOLS);

                new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message).join();

                player.sendMessage(I18n.string("gui_auction.view_third_bin.purchased", l, Map.of(
                        "item_name", new SkyBlockItem(item.getItem()).getDisplayName(),
                        "price", String.valueOf(item.getStartingPrice())
                )));

                ProxyPlayer owner = new ProxyPlayer(item.getOriginator());
                if (owner.isOnline().join()) {
                    owner.sendMessage(Component.text(I18n.string("gui_auction.view_third_bin.owner_notification", l, Map.of(
                            "buyer_name", player.getFullDisplayName(),
                            "item_name", new SkyBlockItem(item.getItem()).getDisplayName()
                    ))).clickEvent(
                            ClickEvent.runCommand("/ahview " + item.getUuid())
                    ));
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return TranslatableItemStackCreator.getStack(p, "gui_auction.view_third_bin.buy_now", Material.GOLD_NUGGET, 1,
                        "gui_auction.view_third_bin.buy_now.lore", Map.of(
                                "price", String.valueOf(item.getStartingPrice())
                        ));
            }
        });
    }
}
