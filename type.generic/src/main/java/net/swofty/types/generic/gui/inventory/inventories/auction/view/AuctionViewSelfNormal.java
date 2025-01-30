package net.swofty.types.generic.gui.inventory.inventories.auction.view;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionViewItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class AuctionViewSelfNormal implements AuctionView {
    private static final String STATE_EXPIRED_NO_BIDS = "expired_no_bids";
    private static final String STATE_EXPIRED_WITH_BIDS = "expired_with_bids";
    private static final String STATE_EXPIRED_CLAIMED = "expired_claimed";
    private static final String STATE_ACTIVE = "active";

    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        // Set initial state
        if (item.getEndTime() < System.currentTimeMillis()) {
            List<UUID> ownedActive = player.getDataHandler()
                    .get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();

            if (ownedActive.contains(item.getUuid())) {
                if (item.getBids().isEmpty()) {
                    gui.getStates().add(STATE_EXPIRED_NO_BIDS);
                } else {
                    gui.getStates().add(STATE_EXPIRED_WITH_BIDS);
                }
            } else {
                gui.getStates().add(STATE_EXPIRED_CLAIMED);
            }
        } else {
            gui.getStates().add(STATE_ACTIVE);
        }

        // Bid History
        gui.attachItem(GUIItem.builder(33)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Total bids: §a" + item.getBids().size() + " bids");

                    List<AuctionItem.Bid> bids = new ArrayList<>(item.getBids());
                    bids.sort(Comparator.comparingLong(AuctionItem.Bid::value).reversed());

                    for (int i = 0; i < 10; i++) {
                        if (i >= bids.size()) break;
                        AuctionItem.Bid bid = bids.get(i);

                        lore.add("§8§m---------------");
                        lore.add("§7Bid: §6" + bid.value() + " coins");
                        lore.add("§7By: " + SkyBlockPlayer.getDisplayName(bid.uuid()));
                        lore.add("§b" + StringUtility.formatTimeAsAgo(bid.timestamp()));
                    }

                    return ItemStackCreator.getStack("Bid History", Material.FILLED_MAP, 1, lore).build();
                })
                .build());

        // Expired with no bids - collect item
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_EXPIRED_NO_BIDS)
                .item(() -> ItemStackCreator.getStack("§6Collect Auction", Material.GOLD_BLOCK, 1,
                        " ",
                        "§7This auction has ended!",
                        " ",
                        "§7Noone bid on this auction!",
                        " ",
                        "§eClick to collect your item!").build())
                .onClick((ctx, clickedItem) -> {
                    List<UUID> ownedActive = ctx.player().getDataHandler()
                            .get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
                    List<UUID> ownedInactive = ctx.player().getDataHandler()
                            .get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

                    ownedActive.remove(item.getUuid());
                    ctx.player().getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class)
                            .setValue(ownedActive);
                    ownedInactive.add(item.getUuid());
                    ctx.player().getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class)
                            .setValue(ownedInactive);

                    ctx.player().closeInventory();
                    ctx.player().addAndUpdateItem(item.getItem());
                    return true;
                })
                .build());

        // Expired with bids - collect coins
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_EXPIRED_WITH_BIDS)
                .item(() -> {
                    long highestBid = item.getBids().stream()
                            .max(Comparator.comparingLong(AuctionItem.Bid::value))
                            .map(AuctionItem.Bid::value)
                            .orElse(0L);

                    return ItemStackCreator.getStack("§6Collect Auction", Material.GOLD_BLOCK, 1,
                            " ",
                            "§7This auction has ended!",
                            " ",
                            "§7The highest bid was §6" + highestBid + " coins",
                            " ",
                            "§eClick to collect coins!").build();
                })
                .onClick((ctx, clickedItem) -> {
                    double coins = ctx.player().getDataHandler()
                            .get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
                    long highestBid = item.getBids().stream()
                            .max(Comparator.comparingLong(AuctionItem.Bid::value))
                            .map(AuctionItem.Bid::value)
                            .orElse(0L);

                    ctx.player().getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class)
                            .setValue(coins + highestBid);

                    List<UUID> ownedActive = ctx.player().getDataHandler()
                            .get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
                    List<UUID> ownedInactive = ctx.player().getDataHandler()
                            .get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

                    ownedActive.remove(item.getUuid());
                    ctx.player().getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class)
                            .setValue(ownedActive);
                    ownedInactive.add(item.getUuid());
                    ctx.player().getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class)
                            .setValue(ownedInactive);

                    ctx.player().sendMessage("§eYou collected §6" + highestBid + " coins §efrom the auction!");
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Already claimed auction
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_EXPIRED_CLAIMED)
                .item(() -> {
                    long highestBid = item.getBids().stream()
                            .max(Comparator.comparingLong(AuctionItem.Bid::value))
                            .map(AuctionItem.Bid::value)
                            .orElse(0L);

                    return ItemStackCreator.getStack("§6Auction Ended", Material.BARRIER, 1,
                            " ",
                            "§7This auction has ended!",
                            " ",
                            "§7The highest bid was §6" + highestBid + " coins",
                            " ",
                            "§cYou have already collected your coins!").build();
                })
                .build());

        // Active auction
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_ACTIVE)
                .item(() -> ItemStackCreator.getStack("§cYour Own Auction", Material.BEDROCK, 1,
                        "§7You cannot buy your own item!",
                        "§7Allow it to end and collect your coins.").build())
                .build());
    }
}