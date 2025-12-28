package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class AuctionViewSelfNormal implements AuctionView {
    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        gui.set(new GUIItem(33) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                lore.add("§7Total bids: §a" + item.getBids().size() + " bids");

                List<AuctionItem.Bid> bids = new ArrayList<>(item.getBids());
                bids.sort(Comparator.comparingLong(AuctionItem.Bid::value).reversed());

                for (int i = 0; i < 10; i++) {
                    if (i >= bids.size())
                        break;
                    AuctionItem.Bid bid = bids.get(i);

                    lore.add("§8§m---------------");
                    lore.add("§7Bid: §6" + bid.value() + " coins");
                    lore.add("§7By: " + SkyBlockPlayer.getDisplayName(bid.uuid()));
                    lore.add("§b" + StringUtility.formatTimeAsAgo(bid.timestamp()));
                }

                return ItemStackCreator.getStack("Bid History", Material.FILLED_MAP, 1, lore);
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
                            return ItemStackCreator.getStack("§6Collect Auction", Material.GOLD_BLOCK, 1,
                                    " ",
                                    "§7This auction has ended!",
                                    " ",
                                    "§7Noone bid on this auction!",
                                    " ",
                                    "§eClick to collect your item!");
                        }
                    });
                } else {
                    gui.set(new GUIClickableItem(29) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            double coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();
                            long highestBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::value).orElse(0L);
                            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins + highestBid);

                            ownedActive.remove(item.getUuid());
                            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                            ownedInactive.add(item.getUuid());
                            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                            player.sendMessage("§eYou collected §6" + highestBid + " coins §efrom the auction!");
                            player.closeInventory();
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
                            return ItemStackCreator.getStack("§6Collect Auction", Material.GOLD_BLOCK, 1,
                                    " ",
                                    "§7This auction has ended!",
                                    " ",
                                    "§7The highest bid was §6" + item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::value).orElse(0L) + " coins",
                                    " ",
                                    "§eClick to collect coins!");
                        }
                    });
                }
            } else {
                // Player has already claimed their coins
                gui.set(new GUIItem(29) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStackCreator.getStack("§6Auction Ended", Material.BARRIER, 1,
                                " ",
                                "§7This auction has ended!",
                                " ",
                                "§7The highest bid was §6" + item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::value).orElse(0L) + " coins",
                                " ",
                                "§cYou have already collected your coins!");
                    }
                });
            }
            return;
        }

        gui.set(new GUIItem(29) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§cYour Own Auction", Material.BEDROCK, 1,
                        "§7You cannot buy your own item!",
                        "§7Allow it to end and collect your coins.");
            }
        });
    }
}
