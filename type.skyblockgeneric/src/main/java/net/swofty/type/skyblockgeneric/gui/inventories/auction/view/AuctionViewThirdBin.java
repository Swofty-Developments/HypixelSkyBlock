package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionAddItemProtocolObject;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class AuctionViewThirdBin implements AuctionView {
    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        // Check if the BIN item has already been bought
        if (!item.getBids().isEmpty()) {
            // Check if the bidder is the player
            if (item.getBids().getFirst().uuid().equals(player.getUuid())) {
                DatapointUUIDList activeBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
                DatapointUUIDList inactiveBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class);

                if (activeBids.getValue().contains(item.getUuid())) {
                    gui.set(new GUIClickableItem(31) {
                        @Override
                        public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                            SkyBlockPlayer player = (SkyBlockPlayer) p;
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
                        }

                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return ItemStackCreator.getStack("§aClaim Item", Material.GOLD_BLOCK, 1,
                                    " ",
                                    "§7This item has been sold!",
                                    " ",
                                    SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" + item.getBids().getFirst().value() + " coins",
                                    " ",
                                    "§eClick to claim item!");
                        }
                    });
                } else {
                    gui.set(new GUIItem(31) {
                        @Override
                        public ItemStack.Builder getItem(HypixelPlayer p) {
                            return ItemStackCreator.getStack("§cItem Has Been Sold", Material.BEDROCK, 1,
                                    " ",
                                    "§7This item has been sold!",
                                    " ",
                                    SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" + item.getBids().getFirst().value() + " coins",
                                    " ",
                                    "§cYou have already claimed this item!");
                        }
                    });
                }
                return;
            }

            gui.set(new GUIItem(31) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStackCreator.getStack("§cItem Has Been Sold", Material.BEDROCK, 1,
                            " ",
                            "§7This item has been sold!",
                            " ",
                            SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" + item.getBids().getFirst().value() + " coins",
                            " ",
                            "§7You can no longer purchase this item.");
                }
            });
            return;
        }

        gui.set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                double coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();
                if (coins < item.getStartingPrice()) {
                    player.sendMessage("§cYou do not have enough coins to purchase this item!");
                    return;
                }

                player.sendMessage("§7Putting coins in escrow...");
                player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins - item.getStartingPrice());

                player.sendMessage("§7Processing purchase...");

                // Check that it is still available, by checking it has 0 bids
                CompletableFuture<AuctionFetchItemProtocolObject.AuctionFetchItemResponse> future = new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(
                        new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(item.getUuid())
                );
                AuctionItem item = future.join().item();

                if (!item.getBids().isEmpty()) {
                    player.sendMessage("§cCouldn't purchase the item, it has been sold!");
                    player.sendMessage("§8Returning escrowed coins...");
                    player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getStartingPrice());
                    return;
                }

                CoopDatabase.Coop originatorCoop = CoopDatabase.getFromMember(item.getOriginator());
                CoopDatabase.Coop purchaserCoop = CoopDatabase.getFromMember(player.getUuid());
                if (originatorCoop != null && purchaserCoop != null && originatorCoop.isSameAs(purchaserCoop)) {
                    player.sendMessage("§cCannot purchase an item from someone in the same coop!");
                    player.sendMessage("§8Returning escrowed coins...");
                    player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getStartingPrice());
                    return;
                }

                DatapointUUIDList activeBids = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
                activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                    add(item.getUuid());
                }});
                player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).setValue(activeBids.getValue());

                // Add player bid to item and update it
                item.setBids(new ArrayList<>(item.getBids()) {{
                    add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(), item.getStartingPrice().longValue()));
                }});
                item.setEndTime(System.currentTimeMillis());

                AuctionAddItemProtocolObject.AuctionAddItemMessage message =
                        new AuctionAddItemProtocolObject.AuctionAddItemMessage(
                                item, AuctionCategories.TOOLS);

                new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message).join();

                player.sendMessage("§eYou purchased " + new SkyBlockItem(item.getItem()).getDisplayName() + "§e for §6" + item.getStartingPrice() + " coins§e!");

                ProxyPlayer owner = new ProxyPlayer(item.getOriginator());
                if (owner.isOnline().join()) {
                    owner.sendMessage(Component.text("§6[Auction] " + player.getFullDisplayName() + " §ejust purchased your item §6" + new SkyBlockItem(item.getItem()).getDisplayName() + "§e!").clickEvent(
                            ClickEvent.runCommand("/ahview " + item.getUuid())
                    ));
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§6Buy Item Right Now", Material.GOLD_NUGGET, 1,
                        " ",
                        "§7Price: §6" + item.getStartingPrice() + " coins",
                        " ",
                        "§eClick to purchase!");
            }
        });
    }
}
