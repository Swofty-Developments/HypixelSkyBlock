package net.swofty.types.generic.gui.inventory.inventories.auction.view;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionViewItem;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.protocol.auctions.ProtocolAddItem;
import net.swofty.types.generic.protocol.auctions.ProtocolFetchItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuctionViewThirdBin implements AuctionView {
    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        // Check if BIN item has already been bought
        if (!item.getBids().isEmpty()) {
            // Check if bidder is the player
            if (item.getBids().getFirst().uuid().equals(player.getUuid())) {
                DatapointUUIDList activeBids = player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
                DatapointUUIDList inactiveBids = player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_BIDS, DatapointUUIDList.class);

                if (activeBids.getValue().contains(item.getUuid())) {
                    gui.set(new GUIClickableItem(31) {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
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
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                double coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
                if (coins < item.getStartingPrice()) {
                    player.sendMessage("§cYou do not have enough coins to purchase this item!");
                    return;
                }

                player.sendMessage("§7Putting coins in escrow...");
                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins - item.getStartingPrice());

                player.sendMessage("§7Processing purchase...");

                // Check that it is still available, by checking it has 0 bids
                Map<String, Object> itemResponse = new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(
                        new ProtocolFetchItem(),
                        new JSONObject().put("uuid", item.getUuid().toString()).toMap()
                ).join();

                AuctionItem item = (AuctionItem) itemResponse.get("item");
                if (!item.getBids().isEmpty()) {
                    player.sendMessage("§cCouldn't purchase the item, it has been sold!");
                    player.sendMessage("§8Returning escrowed coins...");
                    player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getStartingPrice());
                    return;
                }

                CoopDatabase.Coop originatorCoop = CoopDatabase.getFromMember(item.getOriginator());
                CoopDatabase.Coop purchaserCoop = CoopDatabase.getFromMember(player.getUuid());
                if (originatorCoop != null && purchaserCoop != null && originatorCoop.isSameAs(purchaserCoop)) {
                    player.sendMessage("§cCannot purchase an item from someone in the same coop!");
                    player.sendMessage("§8Returning escrowed coins...");
                    player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getStartingPrice());
                    return;
                }

                DatapointUUIDList activeBids = player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class);
                activeBids.setValue(new ArrayList<>(activeBids.getValue()) {{
                    add(item.getUuid());
                }});
                player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).setValue(activeBids.getValue());

                // Add player bid to item and update it
                item.setBids(new ArrayList<>(item.getBids()) {{
                    add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(), item.getStartingPrice().longValue()));
                }});
                item.setEndTime(System.currentTimeMillis());

                Map<String, Object> requestMessage = new HashMap<>();
                requestMessage.put("item", item);
                requestMessage.put("category", AuctionCategories.TOOLS);

                new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolAddItem(), requestMessage).join();

                player.sendMessage("§eYou purchased " + item.getItem().getDisplayName() + "§e for §6" + item.getStartingPrice() + " coins§e!");

                ProxyPlayer owner = new ProxyPlayer(item.getOriginator());
                if (owner.isOnline().join()) {
                    owner.sendMessage(Component.text("§6[Auction] " + player.getFullDisplayName() + " §ejust purchased your item §6" + item.getItem().getDisplayName() + "§e!").clickEvent(
                            ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/ahview " + item.getUuid())
                    ));
                }
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§6Buy Item Right Now", Material.GOLD_NUGGET, 1,
                        " ",
                        "§7Price: §6" + item.getStartingPrice() + " coins",
                        " ",
                        "§eClick to purchase!");
            }
        });
        return;
    }
}
