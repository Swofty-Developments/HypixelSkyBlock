package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AuctionViewSelfNormal implements AuctionView {
    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        gui.set(new GUIItem(33) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_auction.view_self_normal.bid_history_total", l, Map.of("count", String.valueOf(item.getBids().size()))));

                List<AuctionItem.Bid> bids = new ArrayList<>(item.getBids());
                bids.sort(Comparator.comparingLong(AuctionItem.Bid::value).reversed());

                for (int i = 0; i < 10; i++) {
                    if (i >= bids.size())
                        break;
                    AuctionItem.Bid bid = bids.get(i);

                    lore.add(I18n.string("gui_auction.view_self_normal.bid_separator", l));
                    lore.add(I18n.string("gui_auction.view_self_normal.bid_value", l, Map.of("value", String.valueOf(bid.value()))));
                    lore.add(I18n.string("gui_auction.view_self_normal.bid_by", l, Map.of("player_name", SkyBlockPlayer.getDisplayName(bid.uuid()))));
                    lore.add("§b" + StringUtility.formatTimeAsAgo(bid.timestamp()));
                }

                return ItemStackCreator.getStack(I18n.string("gui_auction.view_self_normal.bid_history", l), Material.FILLED_MAP, 1, lore);
            }
        });

        if (item.getEndTime() < System.currentTimeMillis()) {
            List<UUID> ownedActive = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
            List<UUID> ownedInactive = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

            if (ownedActive.contains(item.getUuid())) {
                if (item.getBids().isEmpty()) {
                    gui.set(new GUIClickableItem(29) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            ownedActive.remove(item.getUuid());
                            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                            ownedInactive.add(item.getUuid());
                            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);
                            player.closeInventory();

                            player.addAndUpdateItem(item.getItem());
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            return TranslatableItemStackCreator.getStack(p, "gui_auction.view_self_normal.collect_auction", Material.GOLD_BLOCK, 1,
                                    "gui_auction.view_self_normal.collect_no_bids.lore");
                        }
                    });
                } else {
                    gui.set(new GUIClickableItem(29) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            Locale l = p.getLocale();
                            double coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();
                            long highestBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::value).orElse(0L);
                            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins + highestBid);

                            ownedActive.remove(item.getUuid());
                            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                            ownedInactive.add(item.getUuid());
                            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                            player.sendMessage(I18n.string("gui_auction.view_self_normal.collected_coins", l, Map.of("amount", String.valueOf(highestBid))));
                            player.closeInventory();
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            return TranslatableItemStackCreator.getStack(p, "gui_auction.view_self_normal.collect_auction", Material.GOLD_BLOCK, 1,
                                    "gui_auction.view_self_normal.collect_with_bids.lore", Map.of(
                                            "amount", String.valueOf(item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::value).orElse(0L))
                                    ));
                        }
                    });
                }
            } else {
                gui.set(new GUIItem(29) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return TranslatableItemStackCreator.getStack(p, "gui_auction.view_self_normal.auction_ended", Material.BARRIER, 1,
                                "gui_auction.view_self_normal.auction_ended_claimed.lore", Map.of(
                                        "amount", String.valueOf(item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::value).orElse(0L))
                                ));
                    }
                });
            }
            return;
        }

        gui.set(new GUIItem(29) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return TranslatableItemStackCreator.getStack(p, "gui_auction.view_self_normal.own_auction", Material.BEDROCK, 1,
                        "gui_auction.view_self_normal.own_auction.lore");
            }
        });
    }
}
