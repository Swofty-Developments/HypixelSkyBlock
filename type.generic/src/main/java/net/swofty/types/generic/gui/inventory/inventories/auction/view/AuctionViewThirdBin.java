package net.swofty.types.generic.gui.inventory.inventories.auction.view;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionViewItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class AuctionViewThirdBin implements AuctionView {
    private static final String STATE_BOUGHT_BY_PLAYER_UNCLAIMED = "bought_by_player_unclaimed";
    private static final String STATE_BOUGHT_BY_PLAYER_CLAIMED = "bought_by_player_claimed";
    private static final String STATE_BOUGHT_BY_OTHER = "bought_by_other";
    private static final String STATE_AVAILABLE = "available";

    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        // Determine state
        if (!item.getBids().isEmpty()) {
            if (item.getBids().getFirst().uuid().equals(player.getUuid())) {
                DatapointUUIDList activeBids = player.getDataHandler()
                        .get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);

                if (activeBids.getValue().contains(item.getUuid())) {
                    gui.getStates().add(STATE_BOUGHT_BY_PLAYER_UNCLAIMED);
                } else {
                    gui.getStates().add(STATE_BOUGHT_BY_PLAYER_CLAIMED);
                }
            } else {
                gui.getStates().add(STATE_BOUGHT_BY_OTHER);
            }
        } else {
            gui.getStates().add(STATE_AVAILABLE);
        }

        // Claim item button (when player bought and hasn't claimed)
        gui.attachItem(GUIItem.builder(31)
                .requireState(STATE_BOUGHT_BY_PLAYER_UNCLAIMED)
                .item(() -> ItemStackCreator.getStack("§aClaim Item", Material.GOLD_BLOCK, 1,
                        " ",
                        "§7This item has been sold!",
                        " ",
                        SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" +
                                item.getBids().getFirst().value() + " coins",
                        " ",
                        "§eClick to claim item!").build())
                .onClick((ctx, clickedItem) -> {
                    DatapointUUIDList activeBids = ctx.player().getDataHandler()
                            .get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
                    DatapointUUIDList inactiveBids = ctx.player().getDataHandler()
                            .get(DataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class);

                    ctx.player().sendMessage("§8Claiming your item...");
                    activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                        remove(item.getUuid());
                    }});
                    inactiveBids.setValue(new ArrayList<>(inactiveBids.getValue()) {{
                        add(item.getUuid());
                    }});

                    ctx.player().addAndUpdateItem(item.getItem());
                    ctx.player().sendMessage("§aYou have claimed your item.");
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Already claimed item display
        gui.attachItem(GUIItem.builder(31)
                .requireState(STATE_BOUGHT_BY_PLAYER_CLAIMED)
                .item(() -> ItemStackCreator.getStack("§cItem Has Been Sold", Material.BEDROCK, 1,
                        " ",
                        "§7This item has been sold!",
                        " ",
                        SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" +
                                item.getBids().getFirst().value() + " coins",
                        " ",
                        "§cYou have already claimed this item!").build())
                .build());

        // Sold to someone else display
        gui.attachItem(GUIItem.builder(31)
                .requireState(STATE_BOUGHT_BY_OTHER)
                .item(() -> ItemStackCreator.getStack("§cItem Has Been Sold", Material.BEDROCK, 1,
                        " ",
                        "§7This item has been sold!",
                        " ",
                        SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" +
                                item.getBids().getFirst().value() + " coins",
                        " ",
                        "§7You can no longer purchase this item.").build())
                .build());

        // Buy now button
        gui.attachItem(GUIItem.builder(31)
                .requireState(STATE_AVAILABLE)
                .item(() -> ItemStackCreator.getStack("§6Buy Item Right Now", Material.GOLD_NUGGET, 1,
                        " ",
                        "§7Price: §6" + item.getStartingPrice() + " coins",
                        " ",
                        "§eClick to purchase!").build())
                .onClick((ctx, clickedItem) -> {
                    handleItemPurchase(ctx.player(), item);
                    return true;
                })
                .build());
    }

    private void handleItemPurchase(SkyBlockPlayer player, AuctionItem item) {
        double coins = player.getDataHandler()
                .get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
        if (coins < item.getStartingPrice()) {
            player.sendMessage("§cYou do not have enough coins to purchase this item!");
            return;
        }

        player.sendMessage("§7Putting coins in escrow...");
        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class)
                .setValue(coins - item.getStartingPrice());

        player.sendMessage("§7Processing purchase...");

        // Verify availability
        CompletableFuture<AuctionFetchItemProtocolObject.AuctionFetchItemResponse> future =
                new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(
                        new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(item.getUuid())
                );
        AuctionItem currentItem = future.join().item();

        if (!currentItem.getBids().isEmpty()) {
            player.sendMessage("§cCouldn't purchase the item, it has been sold!");
            player.sendMessage("§8Returning escrowed coins...");
            player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class)
                    .setValue(coins);
            return;
        }

        // Check coop status
        CoopDatabase.Coop originatorCoop = CoopDatabase.getFromMember(item.getOriginator());
        CoopDatabase.Coop purchaserCoop = CoopDatabase.getFromMember(player.getUuid());
        if (originatorCoop != null && purchaserCoop != null && originatorCoop.isSameAs(purchaserCoop)) {
            player.sendMessage("§cCannot purchase an item from someone in the same coop!");
            player.sendMessage("§8Returning escrowed coins...");
            player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class)
                    .setValue(coins);
            return;
        }

        // Update bid tracking
        DatapointUUIDList activeBids = player.getDataHandler()
                .get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
        activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
            add(item.getUuid());
        }});

        // Update item
        currentItem.setBids(new ArrayList<>(currentItem.getBids()) {{
            add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(),
                    item.getStartingPrice().longValue()));
        }});
        currentItem.setEndTime(System.currentTimeMillis());

        // Save to auction house
        AuctionAddItemProtocolObject.AuctionAddItemMessage message =
                new AuctionAddItemProtocolObject.AuctionAddItemMessage(
                        currentItem, AuctionCategories.TOOLS);
        new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message).join();

        // Notifications
        player.sendMessage("§eYou purchased " + new SkyBlockItem(item.getItem()).getDisplayName() +
                "§e for §6" + item.getStartingPrice() + " coins§e!");

        ProxyPlayer owner = new ProxyPlayer(item.getOriginator());
        if (owner.isOnline().join()) {
            owner.sendMessage(Component.text("§6[Auction] " + player.getFullDisplayName() +
                            " §ejust purchased your item §6" +
                            new SkyBlockItem(item.getItem()).getDisplayName() + "§e!")
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,
                            "/ahview " + item.getUuid())));
        }
    }
}