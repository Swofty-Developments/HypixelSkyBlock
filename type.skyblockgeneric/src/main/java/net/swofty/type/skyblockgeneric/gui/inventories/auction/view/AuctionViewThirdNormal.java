package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
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
import java.util.Locale;
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
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_auction.view_third_normal.bid_history_total", l, Component.text(String.valueOf(item.getBids().size()))));

                List<AuctionItem.Bid> bids = new ArrayList<>(item.getBids());
                bids.sort(Comparator.comparingLong(AuctionItem.Bid::value).reversed());

                for (int i = 0; i < 10; i++) {
                    if (i >= bids.size())
                        break;
                    AuctionItem.Bid bid = bids.get(i);

                    lore.add(I18n.string("gui_auction.view_third_normal.bid_separator", l));
                    lore.add(I18n.string("gui_auction.view_third_normal.bid_value", l, Component.text(String.valueOf(bid.value()))));
                    lore.add(I18n.string("gui_auction.view_third_normal.bid_by", l, Component.text(SkyBlockPlayer.getDisplayName(bid.uuid()))));
                    lore.add("§b" + StringUtility.formatTimeAsAgo(bid.timestamp()));
                }

                return ItemStackCreator.getStack(I18n.string("gui_auction.view_third_normal.bid_history", l), Material.FILLED_MAP, 1, lore);
            }
        });

        if (System.currentTimeMillis() > item.getEndTime()) {
            DatapointUUIDList activeBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
            DatapointUUIDList inactiveBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class);

            AuctionItem.Bid winningBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);
            AuctionItem.Bid highestBidMadeByPlayer = item.getBids().stream().filter(bid -> bid.uuid().equals(player.getUuid())).max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);
            if (winningBid == null || !winningBid.uuid().equals(player.getUuid())) {
                if (activeBids.getValue().contains(item.getUuid())) {
                    gui.set(new GUIClickableItem(29) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            Locale l = p.getLocale();
                            player.sendMessage(I18n.t("gui_auction.view_third_normal.claiming_bid_coins"));
                            DatapointDouble coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class);
                            coins.setValue(coins.getValue() + highestBidMadeByPlayer.value());
                            activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                                remove(item.getUuid());
                            }});
                            inactiveBids.setValue(new ArrayList<>(inactiveBids.getValue()) {{
                                add(item.getUuid());
                            }});

                            player.sendMessage(I18n.t("gui_auction.view_third_normal.coins_returned", Component.text(String.valueOf(highestBidMadeByPlayer.value()))));
                            player.closeInventory();
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return TranslatableItemStackCreator.getStack("gui_auction.view_third_normal.auction_ended_lost", Material.BARRIER, 1,
                                "gui_auction.view_third_normal.auction_ended_lost_claim.lore", Component.text(String.valueOf(highestBidMadeByPlayer.value())
                                    ));
                        }
                    });
                } else {
                    gui.set(new GUIItem(29) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return TranslatableItemStackCreator.getStack("gui_auction.view_third_normal.auction_ended_lost", Material.BARRIER, 1,
                                    "gui_auction.view_third_normal.auction_ended_lost_no_claim.lore");
                        }
                    });
                }
            } else {
                if (activeBids.getValue().contains(item.getUuid())) {
                    gui.set(new GUIClickableItem(29) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            Locale l = p.getLocale();
                            player.sendMessage(I18n.t("gui_auction.view_third_normal.claiming_item"));
                            activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                                remove(item.getUuid());
                            }});
                            inactiveBids.setValue(new ArrayList<>(inactiveBids.getValue()) {{
                                add(item.getUuid());
                            }});

                            player.addAndUpdateItem(item.getItem());

                            player.sendMessage(I18n.t("gui_auction.view_third_normal.claimed_item"));
                            player.closeInventory();
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return TranslatableItemStackCreator.getStack("gui_auction.view_third_normal.auction_ended_won", Material.EMERALD, 1,
                                    "gui_auction.view_third_normal.auction_ended_won_claim.lore");
                        }
                    });
                } else {
                    gui.set(new GUIItem(29) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return TranslatableItemStackCreator.getStack("gui_auction.view_third_normal.auction_ended_won", Material.EMERALD, 1,
                                    "gui_auction.view_third_normal.auction_ended_won_claimed.lore");
                        }
                    });
                }
            }
            return;
        }

        gui.set(new GUIQueryItem(31) {
            @Override
            public HypixelInventoryGUI onQueryFinish(String query, HypixelPlayer player) {
                Locale l = player.getLocale();
                long val;
                try {
                    val = Long.parseLong(query);
                } catch (NumberFormatException ex) {
                    player.sendMessage(I18n.t("gui_auction.view_third_normal.number_parse_error"));
                    return gui;
                }
                if (val < gui.minimumBidAmount) {
                    player.sendMessage(I18n.t("gui_auction.view_third_normal.bid_too_low", Component.text(String.valueOf(gui.minimumBidAmount))));
                    return gui;
                }

                gui.bidAmount = val;

                return gui;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return TranslatableItemStackCreator.getStack(
                    "gui_auction.view_third_normal.bid_amount", Material.GOLD_INGOT, 1,
                    "gui_auction.view_third_normal.bid_amount.lore", Component.text(String.valueOf(gui.minimumBidAmount)), Component.text(String.valueOf(gui.bidAmount)));
            }
        });
        gui.set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                if (gui.bidAmount < gui.minimumBidAmount) {
                    player.sendMessage(I18n.t("gui_auction.view_third_normal.bid_too_low", Component.text(String.valueOf(gui.minimumBidAmount))));
                    return;
                }

                DatapointDouble coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class);
                if (coins.getValue() < gui.bidAmount) {
                    player.sendMessage(I18n.t("gui_auction.view_third_normal.not_enough_coins"));
                    return;
                }

                UUID topBidder = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::uuid).orElse(null);
                if (topBidder != null && topBidder.equals(player.getUuid())) {
                    player.sendMessage(I18n.t("gui_auction.view_third_normal.already_top_bid"));
                    return;
                }

                player.sendMessage(I18n.t("gui_auction.view_third_normal.escrow_message"));
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
                    player.sendMessage(I18n.t("gui_auction.view_third_normal.same_coop"));
                    player.sendMessage(I18n.t("gui_auction.view_third_normal.returning_escrow"));
                    coins.setValue(coins.getValue() + gui.bidAmount);
                    return;
                }

                player.sendMessage(I18n.t("gui_auction.view_third_normal.processing_bid"));
                Thread.startVirtualThread(() -> {
                    AuctionFetchItemProtocolObject.AuctionFetchItemResponse itemResponse = (AuctionFetchItemProtocolObject.AuctionFetchItemResponse) new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(
                            new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(item.getUuid())
                    ).join();

                    AuctionItem item = itemResponse.item();
                    AuctionItem.Bid highestBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);

                    if (highestBid != null && highestBid.value() >= gui.bidAmount) {
                        player.sendMessage(I18n.t("gui_auction.view_third_normal.bid_changed"));
                        player.sendMessage(I18n.t("gui_auction.view_third_normal.returning_escrow"));
                        coins.setValue(coins.getValue() + gui.bidAmount);
                        return;
                    }

                    if (item.getEndTime() + 5000 < System.currentTimeMillis()) {
                        player.sendMessage(I18n.t("gui_auction.view_third_normal.auction_ended_error"));
                        player.sendMessage(I18n.t("gui_auction.view_third_normal.returning_escrow"));
                        coins.setValue(coins.getValue() + gui.bidAmount);
                        return;
                    }

                    item.setBids(new ArrayList<>(item.getBids()) {{
                        add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(), gui.bidAmount));
                    }});
                    item.setEndTime(item.getEndTime() + 120000);

                    AuctionAddItemProtocolObject.AuctionAddItemMessage message =
                            new AuctionAddItemProtocolObject.AuctionAddItemMessage(
                                    item, category);
                    new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message).join();

                    player.sendMessage(I18n.t("gui_auction.view_third_normal.bid_placed", Component.text(String.valueOf(gui.bidAmount))));
                    new GUIAuctionViewItem(gui.auctionID, gui.previousGUI).open(player);

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
                                proxyPlayer.sendMessage(I18n.t("gui_auction.view_third_normal.outbid_notification",
                                    LegacyComponentSerializer.legacySection().deserialize(player.getFullDisplayName()),
                                    Component.text(String.valueOf(gui.bidAmount - playersBid)),
                                    LegacyComponentSerializer.legacySection().deserialize(new SkyBlockItem(item.getItem()).getDisplayName())
                                ).clickEvent(
                                        ClickEvent.runCommand("/ahview " + gui.auctionID)
                                ));
                            }
                        }
                    });

                    ProxyPlayer auctionOwner = new ProxyPlayer(item.getOriginator());
                    if (auctionOwner.isOnline().join()) {
                        auctionOwner.sendMessage(I18n.t("gui_auction.view_third_normal.owner_bid_notification",
                            LegacyComponentSerializer.legacySection().deserialize(player.getFullDisplayName()),
                            Component.text(String.valueOf(gui.bidAmount)),
                            LegacyComponentSerializer.legacySection().deserialize(new SkyBlockItem(item.getItem()).getDisplayName())
                        ).clickEvent(
                                ClickEvent.runCommand("/ahview " + gui.auctionID)
                        ));
                    }
                });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return TranslatableItemStackCreator.getStack("gui_auction.view_third_normal.submit_bid", Material.GOLD_NUGGET, 1,
                    "gui_auction.view_third_normal.submit_bid.lore", Component.text(String.valueOf(gui.bidAmount)));
            }
        });
    }
}
