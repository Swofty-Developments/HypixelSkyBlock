package net.swofty.types.generic.gui.inventory.inventories.auction.view;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionViewItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.AuctionCategoryComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

public class AuctionViewThirdNormal implements AuctionView {
    private static final String STATE_AUCTION_EXPIRED = "auction_expired";
    private static final String STATE_AUCTION_ACTIVE = "auction_active";
    private static final String STATE_WON_UNCLAIMED = "won_unclaimed";
    private static final String STATE_WON_CLAIMED = "won_claimed";
    private static final String STATE_LOST_UNCLAIMED = "lost_unclaimed";
    private static final String STATE_LOST_CLAIMED = "lost_claimed";

    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        if (System.currentTimeMillis() > item.getEndTime()) {
            gui.getStates().add(STATE_AUCTION_EXPIRED);
            setupExpiredAuctionState(gui, item, player);
        } else {
            gui.getStates().add(STATE_AUCTION_ACTIVE);
            setupActiveAuctionState(gui, item, player);
        }

        // Bid History (shown in all states)
        setupBidHistory(gui, item);
    }

    private void setupBidHistory(GUIAuctionViewItem gui, AuctionItem item) {
        gui.attachItem(GUIItem.builder(33)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7Total bids: §a" + item.getBids().size() + " bids");

                    List<AuctionItem.Bid> bids = new ArrayList<>(item.getBids());
                    bids.sort(Comparator.comparingLong(AuctionItem.Bid::value).reversed());

                    for (int i = 0; i < Math.min(10, bids.size()); i++) {
                        AuctionItem.Bid bid = bids.get(i);
                        lore.add("§8§m---------------");
                        lore.add("§7Bid: §6" + bid.value() + " coins");
                        lore.add("§7By: " + SkyBlockPlayer.getDisplayName(bid.uuid()));
                        lore.add("§b" + StringUtility.formatTimeAsAgo(bid.timestamp()));
                    }

                    return ItemStackCreator.getStack("Bid History", Material.FILLED_MAP, 1, lore).build();
                })
                .build());
    }

    private void setupExpiredAuctionState(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        DatapointUUIDList activeBids = player.getDataHandler()
                .get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
        DatapointUUIDList inactiveBids = player.getDataHandler()
                .get(DataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class);

        AuctionItem.Bid winningBid = item.getBids().stream()
                .max(Comparator.comparingLong(AuctionItem.Bid::value))
                .orElse(null);
        AuctionItem.Bid highestBidMadeByPlayer = item.getBids().stream()
                .filter(bid -> bid.uuid().equals(player.getUuid()))
                .max(Comparator.comparingLong(AuctionItem.Bid::value))
                .orElse(null);

        boolean isWinner = winningBid != null && winningBid.uuid().equals(player.getUuid());
        boolean hasClaimedReward = !activeBids.getValue().contains(item.getUuid());

        if (isWinner) {
            gui.getStates().add(hasClaimedReward ? STATE_WON_CLAIMED : STATE_WON_UNCLAIMED);
        } else {
            gui.getStates().add(hasClaimedReward ? STATE_LOST_CLAIMED : STATE_LOST_UNCLAIMED);
        }

        // Lost auction - unclaimed coins
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_LOST_UNCLAIMED)
                .item(() -> ItemStackCreator.getStack("§cAuction Ended", Material.BARRIER, 1,
                        "§7This auction has ended.",
                        "§7You did not win this auction.",
                        " ",
                        "§7You can claim your §6" + highestBidMadeByPlayer.value() + " coins §7back.",
                        " ",
                        "§eClick to claim coins!").build())
                .onClick((ctx, itemStack) -> {
                    player.sendMessage("§8Claiming your bid coins back...");
                    DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
                    coins.setValue(coins.getValue() + highestBidMadeByPlayer.value());

                    activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                        remove(item.getUuid());
                    }});
                    inactiveBids.setValue(new ArrayList<>(inactiveBids.getValue()) {{
                        add(item.getUuid());
                    }});

                    player.sendMessage("§aYou have had §6" + highestBidMadeByPlayer.value() + " coins §areturned to you.");
                    player.closeInventory();
                    return true;
                })
                .build());

        // Lost auction - claimed coins
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_LOST_CLAIMED)
                .item(() -> ItemStackCreator.getStack("§cAuction Ended", Material.BARRIER, 1,
                        "§7This auction has ended.",
                        "§7You did not win this auction.",
                        " ",
                        "§cYou either did not bid or you",
                        "§chave claimed your coins back.").build())
                .build());

        // Won auction - unclaimed item
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_WON_UNCLAIMED)
                .item(() -> ItemStackCreator.getStack("§aAuction Ended", Material.EMERALD, 1,
                        "§7This auction has ended.",
                        "§7You won this auction.",
                        " ",
                        "§7You can claim your item.",
                        " ",
                        "§eClick to claim item!").build())
                .onClick((ctx, itemStack) -> {
                    player.sendMessage("§8Claiming your item...");
                    activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                        remove(item.getUuid());
                    }});
                    inactiveBids.setValue(new ArrayList<>(inactiveBids.getValue()) {{
                        add(item.getUuid());
                    }});

                    player.addAndUpdateItem(item.getItem());
                    player.sendMessage("§aYou have claimed your item.");
                    player.closeInventory();
                    return true;
                })
                .build());

        // Won auction - claimed item
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_WON_CLAIMED)
                .item(() -> ItemStackCreator.getStack("§aAuction Ended", Material.EMERALD, 1,
                        "§7This auction has ended.",
                        "§7You won this auction.",
                        " ",
                        "§cYou have already claimed your item.").build())
                .build());
    }

    private void setupActiveAuctionState(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        AuctionItem.Bid highestBid = item.getBids().stream()
                .max(Comparator.comparingLong(AuctionItem.Bid::value))
                .orElse(null);

        if (gui.bidAmount == 0) {
            gui.bidAmount = highestBid == null ? item.getStartingPrice() : highestBid.value() + 1;
        }
        gui.minimumBidAmount = highestBid == null ? item.getStartingPrice() : highestBid.value() + 1;

        // Bid amount editor
        gui.attachItem(GUIItem.builder(31)
                .requireState(STATE_AUCTION_ACTIVE)
                .item(() -> ItemStackCreator.getStack("Bid Amount: §6" + gui.bidAmount, Material.GOLD_INGOT, 1,
                        "§7You need to bid at least §6" + gui.minimumBidAmount + " coins §7to",
                        "§7hold the top bid on this auction.",
                        " ",
                        "§7The §etop bid §7on auction end wins the",
                        "§7item.",
                        " ",
                        "§7If you do not win, you can claim your",
                        "§7bid coins back.",
                        " ",
                        "§eClick to edit amount!").build())
                .onClick((ctx, itemStack) -> {
                    // Show sign GUI for bid amount
                    return true;
                })
                .build());

        // Submit bid button
        gui.attachItem(GUIItem.builder(29)
                .requireState(STATE_AUCTION_ACTIVE)
                .item(() -> ItemStackCreator.getStack("§6Submit Bid", Material.GOLD_NUGGET, 1,
                        " ",
                        "§7New Bid: §6" + gui.bidAmount + " coins",
                        " ",
                        "§eClick to bid!").build())
                .onClick((ctx, itemStack) -> {
                    handleBidSubmission(gui, item, player);
                    return true;
                })
                .build());
    }

    private void handleBidSubmission(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        // Bid validation
        if (gui.bidAmount < gui.minimumBidAmount) {
            player.sendMessage("§cYou need to bid at least §6" + gui.minimumBidAmount +
                    " coins §cto hold the top bid on this auction.");
            return;
        }

        DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
        if (coins.getValue() < gui.bidAmount) {
            player.sendMessage("§cYou do not have enough coins to bid this amount!");
            return;
        }

        UUID topBidder = item.getBids().stream()
                .max(Comparator.comparingLong(AuctionItem.Bid::value))
                .map(AuctionItem.Bid::uuid)
                .orElse(null);
        if (topBidder != null && topBidder.equals(player.getUuid())) {
            player.sendMessage("§cYou already have the top bid on this auction!");
            return;
        }

        // Process bid
        processBid(gui, item, player, coins);
    }

    private void processBid(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player, DatapointDouble coins) {
        player.sendMessage("§7Putting coins in escrow...");
        coins.setValue(coins.getValue() - gui.bidAmount);
        player.closeInventory();

        AuctionCategories category = new SkyBlockItem(item.getItem()).hasComponent(AuctionCategoryComponent.class) ?
                new SkyBlockItem(item.getItem()).getComponent(AuctionCategoryComponent.class).getCategory() :
                AuctionCategories.TOOLS;

        // Check coop status
        CoopDatabase.Coop originatorCoop = CoopDatabase.getFromMember(item.getOriginator());
        CoopDatabase.Coop purchaserCoop = CoopDatabase.getFromMember(player.getUuid());
        if (originatorCoop != null && purchaserCoop != null && originatorCoop.isSameAs(purchaserCoop)) {
            player.sendMessage("§cCannot purchase an item from someone in the same coop!");
            player.sendMessage("§8Returning escrowed coins...");
            coins.setValue(coins.getValue() + gui.bidAmount);
            return;
        }

        player.sendMessage("§7Processing bid...");
        Thread.startVirtualThread(() -> processBidAsync(gui, item, player, coins, category));
    }

    private void processBidAsync(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player,
                                 DatapointDouble coins, AuctionCategories category) {
        // Fetch latest auction state
        AuctionFetchItemProtocolObject.AuctionFetchItemResponse itemResponse =
                (AuctionFetchItemProtocolObject.AuctionFetchItemResponse) new ProxyService(ServiceType.AUCTION_HOUSE)
                        .handleRequest(new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(item.getUuid()))
                        .join();

        AuctionItem currentItem = itemResponse.item();
        AuctionItem.Bid highestBid = currentItem.getBids().stream()
                .max(Comparator.comparingLong(AuctionItem.Bid::value))
                .orElse(null);

        // Validate bid is still valid
        if (highestBid != null && highestBid.value() >= gui.bidAmount) {
            player.sendMessage("§cCouldn't place your bid, the highest bid has changed!");
            player.sendMessage("§8Returning escrowed coins...");
            coins.setValue(coins.getValue() + gui.bidAmount);
            return;
        }

        // Validate auction hasn't ended
        if (currentItem.getEndTime() + 5000 < System.currentTimeMillis()) {
            player.sendMessage("§cCouldn't place your bid, the auction has ended!");
            player.sendMessage("§8Returning escrowed coins...");
            coins.setValue(coins.getValue() + gui.bidAmount);
            return;
        }

        // Place bid
        currentItem.setBids(new ArrayList<>(currentItem.getBids()) {{
            add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(), gui.bidAmount));
        }});
        // Add two minutes to auction time
        currentItem.setEndTime(currentItem.getEndTime() + 120000);

        // Update auction in service
        AuctionAddItemProtocolObject.AuctionAddItemMessage message =
                new AuctionAddItemProtocolObject.AuctionAddItemMessage(currentItem, category);
        new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message).join();

        // Update player's active bids
        DatapointUUIDList activeBids = player.getDataHandler()
                .get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
        activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
            add(currentItem.getUuid());
        }});

        player.sendMessage("§eBid of §6" + gui.bidAmount + " coins §eplaced!");
        player.openInventory(new GUIAuctionViewItem(gui.auctionID, gui.previousGUI));

        // Send outbid notifications
        notifyOutbidPlayers(currentItem, player, gui);
        notifyAuctionOwner(currentItem, player, gui);
    }

    private void notifyOutbidPlayers(AuctionItem item, SkyBlockPlayer bidder, GUIAuctionViewItem gui) {
        List<UUID> alertsSentOutTo = new ArrayList<>();
        new ProxyPlayerSet(item.getBids().stream()
                .map(AuctionItem.Bid::uuid)
                .toList())
                .asProxyPlayers()
                .forEach(proxyPlayer -> {
                    if (proxyPlayer.isOnline().join()) {
                        long playersBid = item.getBids().stream()
                                .filter(bid -> bid.uuid().equals(proxyPlayer.getUuid()))
                                .mapToLong(AuctionItem.Bid::value)
                                .max()
                                .orElse(0);

                        if (playersBid < gui.bidAmount && !alertsSentOutTo.contains(proxyPlayer.getUuid())) {
                            alertsSentOutTo.add(proxyPlayer.getUuid());
                            proxyPlayer.sendMessage(Component.text("§6[Auction] " + bidder.getFullDisplayName() +
                                            " §eoutbid you by §6" + (gui.bidAmount - playersBid) +
                                            " coins §efor the item §6" + new SkyBlockItem(item.getItem()).getDisplayName() + "§e!")
                                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,
                                            "/ahview " + gui.auctionID)));
                        }
                    }
                });
    }

    private void notifyAuctionOwner(AuctionItem item, SkyBlockPlayer bidder, GUIAuctionViewItem gui) {
        ProxyPlayer auctionOwner = new ProxyPlayer(item.getOriginator());
        if (auctionOwner.isOnline().join()) {
            auctionOwner.sendMessage(Component.text("§6[Auction] " + bidder.getFullDisplayName() +
                            " §eplaced a bid of §6" + gui.bidAmount + " coins §eon your item §6" +
                            new SkyBlockItem(item.getItem()).getDisplayName() + "§e!")
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,
                            "/ahview " + gui.auctionID)));
        }
    }
}