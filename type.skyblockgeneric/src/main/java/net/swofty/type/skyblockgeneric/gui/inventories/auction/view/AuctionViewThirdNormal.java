package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.AuctionCategoryComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AuctionViewThirdNormal implements AuctionView {
    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        AuctionItem.Bid highestBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);
        if (highestBid == null) {
            if (gui.bidAmount == 0)
                gui.bidAmount = item.getStartingPrice();
            gui.minimumBidAmount = item.getStartingPrice();
        } else {
            if (gui.bidAmount == 0)
                gui.bidAmount = highestBid.value() + 1;
            gui.minimumBidAmount = highestBid.value() + 1;
        }

        gui.set(new GUIItem(33) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_auction.view_third_normal.bid_history_total", Map.of("count", String.valueOf(item.getBids().size()))));

                List<AuctionItem.Bid> bids = new ArrayList<>(item.getBids());
                bids.sort(Comparator.comparingLong(AuctionItem.Bid::value).reversed());

                for (int i = 0; i < 10; i++) {
                    if (i >= bids.size())
                        break;
                    AuctionItem.Bid bid = bids.get(i);

                    lore.add(I18n.string("gui_auction.view_third_normal.bid_separator"));
                    lore.add(I18n.string("gui_auction.view_third_normal.bid_value", Map.of("value", String.valueOf(bid.value()))));
                    lore.add(I18n.string("gui_auction.view_third_normal.bid_by", Map.of("player_name", SkyBlockPlayer.getDisplayName(bid.uuid()))));
                    lore.add("§b" + StringUtility.formatTimeAsAgo(bid.timestamp()));
                }

                return ItemStackCreator.getStack(I18n.string("gui_auction.view_third_normal.bid_history"), Material.FILLED_MAP, 1, lore);
            }
        });

        if (System.currentTimeMillis() > item.getEndTime()) {
            DatapointUUIDList activeBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
            DatapointUUIDList inactiveBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class);

            // If UUID is in here, it hasn't been claimed yet, if it's not, it has been claimed,
            // goes for both coins and items
            // Check that they won the auction in the first place

            AuctionItem.Bid winningBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);
            AuctionItem.Bid highestBidMadeByPlayer = item.getBids().stream().filter(bid -> bid.uuid().equals(player.getUuid())).max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);
            if (winningBid == null || !winningBid.uuid().equals(player.getUuid())) {
                if (activeBids.getValue().contains(item.getUuid())) {
                    gui.set(new GUIClickableItem(29) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            player.sendMessage(I18n.string("gui_auction.view_third_normal.claiming_bid_coins"));
                            DatapointDouble coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class);
                            coins.setValue(coins.getValue() + highestBidMadeByPlayer.value());
                            activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                                remove(item.getUuid());
                            }});
                            inactiveBids.setValue(new ArrayList<>(inactiveBids.getValue()) {{
                                add(item.getUuid());
                            }});

                            player.sendMessage(I18n.string("gui_auction.view_third_normal.coins_returned", Map.of("amount", String.valueOf(highestBidMadeByPlayer.value()))));
                            player.closeInventory();
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return ItemStackCreator.getStack(I18n.string("gui_auction.view_third_normal.auction_ended_lost"), Material.BARRIER, 1,
                                    I18n.lore("gui_auction.view_third_normal.auction_ended_lost_claim.lore", Map.of(
                                            "amount", String.valueOf(highestBidMadeByPlayer.value())
                                    )));
                        }
                    });
                } else {
                    gui.set(new GUIItem(29) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return ItemStackCreator.getStack(I18n.string("gui_auction.view_third_normal.auction_ended_lost"), Material.BARRIER, 1,
                                    I18n.lore("gui_auction.view_third_normal.auction_ended_lost_no_claim.lore"));
                        }
                    });
                }
            } else {
                // Player won auction, do similar checks as above to see
                // if they have claimed their item
                if (activeBids.getValue().contains(item.getUuid())) {
                    gui.set(new GUIClickableItem(29) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            player.sendMessage(I18n.string("gui_auction.view_third_normal.claiming_item"));
                            activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                                remove(item.getUuid());
                            }});
                            inactiveBids.setValue(new ArrayList<>(inactiveBids.getValue()) {{
                                add(item.getUuid());
                            }});

                            player.addAndUpdateItem(item.getItem());

                            player.sendMessage(I18n.string("gui_auction.view_third_normal.claimed_item"));
                            player.closeInventory();
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return ItemStackCreator.getStack(I18n.string("gui_auction.view_third_normal.auction_ended_won"), Material.EMERALD, 1,
                                    I18n.lore("gui_auction.view_third_normal.auction_ended_won_claim.lore"));
                        }
                    });
                } else {
                    gui.set(new GUIItem(29) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return ItemStackCreator.getStack(I18n.string("gui_auction.view_third_normal.auction_ended_won"), Material.EMERALD, 1,
                                    I18n.lore("gui_auction.view_third_normal.auction_ended_won_claimed.lore"));
                        }
                    });
                }
            }
            return;
        }

        gui.set(new GUIQueryItem(31) {
            @Override
            public HypixelInventoryGUI onQueryFinish(String query, HypixelPlayer player) {
                long l;
                try {
                    l = Long.parseLong(query);
                } catch (NumberFormatException ex) {
                    player.sendMessage(I18n.string("gui_auction.view_third_normal.number_parse_error"));
                    return gui;
                }
                if (l < gui.minimumBidAmount) {
                    player.sendMessage(I18n.string("gui_auction.view_third_normal.bid_too_low", Map.of("minimum", String.valueOf(gui.minimumBidAmount))));
                    return gui;
                }

                gui.bidAmount = l;

                return gui;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(
                        I18n.string("gui_auction.view_third_normal.bid_amount", Map.of("amount", String.valueOf(gui.bidAmount))),
                        Material.GOLD_INGOT, 1,
                        I18n.lore("gui_auction.view_third_normal.bid_amount.lore", Map.of("minimum", String.valueOf(gui.minimumBidAmount))));
            }
        });
        gui.set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (gui.bidAmount < gui.minimumBidAmount) {
                    player.sendMessage(I18n.string("gui_auction.view_third_normal.bid_too_low", Map.of("minimum", String.valueOf(gui.minimumBidAmount))));
                    return;
                }

                DatapointDouble coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class);
                if (coins.getValue() < gui.bidAmount) {
                    player.sendMessage(I18n.string("gui_auction.view_third_normal.not_enough_coins"));
                    return;
                }

                UUID topBidder = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::uuid).orElse(null);
                if (topBidder != null && topBidder.equals(player.getUuid())) {
                    player.sendMessage(I18n.string("gui_auction.view_third_normal.already_top_bid"));
                    return;
                }

                player.sendMessage(I18n.string("gui_auction.view_third_normal.escrow_message"));
                coins.setValue(coins.getValue() - gui.bidAmount);
                player.closeInventory();

                AuctionCategories category;
                if (new SkyBlockItem(item.getItem()).hasComponent(AuctionCategoryComponent.class))
                    category = new SkyBlockItem(item.getItem()).getComponent(AuctionCategoryComponent.class).getCategory();
                else {
                    category = AuctionCategories.TOOLS;
                }

                CoopDatabase.Coop originatorCoop = CoopDatabase.getFromMember(item.getOriginator());
                CoopDatabase.Coop purchaserCoop = CoopDatabase.getFromMember(player.getUuid());
                if (originatorCoop != null && purchaserCoop != null && originatorCoop.isSameAs(purchaserCoop)) {
                    player.sendMessage(I18n.string("gui_auction.view_third_normal.same_coop"));
                    player.sendMessage(I18n.string("gui_auction.view_third_normal.returning_escrow"));
                    coins.setValue(coins.getValue() + gui.bidAmount);
                    return;
                }

                player.sendMessage(I18n.string("gui_auction.view_third_normal.processing_bid"));
                Thread.startVirtualThread(() -> {
                    AuctionFetchItemProtocolObject.AuctionFetchItemResponse itemResponse = (AuctionFetchItemProtocolObject.AuctionFetchItemResponse) new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(
                            new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(item.getUuid())
                    ).join();

                    AuctionItem item = itemResponse.item();
                    AuctionItem.Bid highestBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);

                    if (highestBid != null && highestBid.value() >= gui.bidAmount) {
                        player.sendMessage(I18n.string("gui_auction.view_third_normal.bid_changed"));
                        player.sendMessage(I18n.string("gui_auction.view_third_normal.returning_escrow"));
                        coins.setValue(coins.getValue() + gui.bidAmount);
                        return;
                    }

                    if (item.getEndTime() + 5000 < System.currentTimeMillis()) {
                        player.sendMessage(I18n.string("gui_auction.view_third_normal.auction_ended_error"));
                        player.sendMessage(I18n.string("gui_auction.view_third_normal.returning_escrow"));
                        coins.setValue(coins.getValue() + gui.bidAmount);
                        return;
                    }

                    item.setBids(new ArrayList<>(item.getBids()) {{
                        add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(), gui.bidAmount));
                    }});
                    // Add two minutes on
                    item.setEndTime(item.getEndTime() + 120000);

                    AuctionAddItemProtocolObject.AuctionAddItemMessage message =
                            new AuctionAddItemProtocolObject.AuctionAddItemMessage(
                                    item, category);
                    new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message).join();

                    player.sendMessage(I18n.string("gui_auction.view_third_normal.bid_placed", Map.of("amount", String.valueOf(gui.bidAmount))));
                    new GUIAuctionViewItem(gui.auctionID, gui.previousGUI).open(player);

                    // Add auction uuid to activebids
                    DatapointUUIDList activeBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
                    activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                        add(item.getUuid());
                    }});

                    List<UUID> alertsSentOutTo = new ArrayList<>();
                    new ProxyPlayerSet(item.getBids().stream().map(AuctionItem.Bid::uuid).toList()).asProxyPlayers().forEach(proxyPlayer -> {
                        if (proxyPlayer.isOnline().join()) {
                            long playersBid = item.getBids().stream().filter(bid -> bid.uuid().equals(proxyPlayer.getUuid())).mapToLong(AuctionItem.Bid::value).max().orElse(0);

                            if (playersBid < gui.bidAmount && !alertsSentOutTo.contains(proxyPlayer.getUuid())) {
                                alertsSentOutTo.add(proxyPlayer.getUuid());
                                proxyPlayer.sendMessage(Component.text(I18n.string("gui_auction.view_third_normal.outbid_notification", Map.of(
                                        "bidder_name", player.getFullDisplayName(),
                                        "difference", String.valueOf(gui.bidAmount - playersBid),
                                        "item_name", new SkyBlockItem(item.getItem()).getDisplayName()
                                ))).clickEvent(
                                        ClickEvent.runCommand("/ahview " + gui.auctionID)
                                ));
                            }
                        }
                    });

                    ProxyPlayer auctionOwner = new ProxyPlayer(item.getOriginator());
                    if (auctionOwner.isOnline().join()) {
                        auctionOwner.sendMessage(Component.text(I18n.string("gui_auction.view_third_normal.owner_bid_notification", Map.of(
                                "bidder_name", player.getFullDisplayName(),
                                "amount", String.valueOf(gui.bidAmount),
                                "item_name", new SkyBlockItem(item.getItem()).getDisplayName()
                        ))).clickEvent(
                                ClickEvent.runCommand("/ahview " + gui.auctionID)
                        ));
                    }
                });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack(I18n.string("gui_auction.view_third_normal.submit_bid"), Material.GOLD_NUGGET, 1,
                        I18n.lore("gui_auction.view_third_normal.submit_bid.lore", Map.of("amount", String.valueOf(gui.bidAmount))));
            }
        });
    }
}
