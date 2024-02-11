package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.types.generic.item.impl.SpecificAuctionCategory;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.protocol.ProtocolAddItem;
import net.swofty.types.generic.protocol.ProtocolDeprecateItem;
import net.swofty.types.generic.protocol.ProtocolFetchItem;
import net.swofty.types.generic.protocol.ProtocolFetchItems;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GUIAuctionViewItem extends SkyBlockInventoryGUI implements RefreshingGUI {
    private final UUID auctionID;
    private final SkyBlockInventoryGUI previousGUI;
    private long bidAmount = 0;
    private long minimumBidAmount = 0;

    public GUIAuctionViewItem(UUID auctionID, SkyBlockInventoryGUI previousGUI) {
        super("Auction View", InventoryType.CHEST_6_ROW);

        this.auctionID = auctionID;
        this.previousGUI = previousGUI;

        Thread.startVirtualThread(this::updateItems);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
    }

    public void updateItems() {
        JSONObject response = new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolFetchItem(),
                new JSONObject().put("uuid", auctionID.toString())).join();
        AuctionItem item = AuctionItem.fromDocument(Document.parse(response.getString("item")));
        set(GUIClickableItem.getGoBackItem(49, previousGUI));

        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return PlayerItemUpdater.playerUpdate(player, item.getItem().getItemStack()).lore(item.getLore(player).stream().map(Component::text).toList());
            }
        });

        if (item.getOriginator() != getPlayer().getUuid()) {
            thirdPartyView(item);
        } else {
            if (item.isBin()) {
                if (!item.getBids().isEmpty()) {
                    set(new GUIClickableItem(31) {
                        @Override
                        public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                            double coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();

                            player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getBids().getFirst().value());

                            List<UUID> ownedActive = player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
                            ownedActive.remove(auctionID);
                            player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);

                            List<UUID> ownedInactive = player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();
                            ownedInactive.add(auctionID);
                            player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                            player.closeInventory();
                        }

                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                            return ItemStackCreator.getStack("§6Collect Auction", Material.GOLD_BLOCK, 1,
                                    " ",
                                    "§7This item has been sold!",
                                    " ",
                                    SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" + item.getBids().getFirst().value() + " coins",
                                    " ",
                                    "§eClick to collect coins!");
                        }
                    });
                    return;
                }

                set(new GUIItem(31) {
                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§cYou Cannot Buy Your Own Item", Material.BEDROCK, 1,
                                " ",
                                "§7You cannot buy your own item!",
                                " ",
                                "§7You can only buy items from other players.");
                    }
                });
                return;
            }

            setBidHistoryItem(item);

            // Check if endTime - 1000 < currentTimeMillis
            if (item.getEndTime() - 1000 < System.currentTimeMillis()) {
                set(new GUIClickableItem(29) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        double coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
                        long highestBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::value).orElse(0L);
                        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins + highestBid);

                        List<UUID> ownedActive = player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
                        ownedActive.remove(auctionID);
                        player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);

                        List<UUID> ownedInactive = player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();
                        ownedInactive.add(auctionID);
                        player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                        player.sendMessage("§eYou collected §6" + highestBid + " coins §efrom the auction!");
                        player.closeInventory();
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§6Collect Auction", Material.GOLD_BLOCK, 1,
                                " ",
                                "§7This auction has ended!",
                                " ",
                                "§7The highest bid was §6" + item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).map(AuctionItem.Bid::value).orElse(0L) + " coins",
                                " ",
                                "§eClick to collect coins!");
                    }
                });
                return;
            }

            set(new GUIItem(29) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§cYou Cannot Buy Your Own Item", Material.BEDROCK, 1,
                            " ",
                            "§7You cannot buy your own item!",
                            " ",
                            "§7You can only buy items from other players.");
                }
            });

            // Bid history

        }
    }

    private void setBidHistoryItem(AuctionItem item) {
        set(new GUIItem(33) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
        }

        updateItems();
    }

    @Override
    public int refreshRate() {
        return 10;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    public void thirdPartyView(AuctionItem item) {
        if (item.isBin()) {
            // Check if BIN item has already been bought
            if (!item.getBids().isEmpty()) {
                set(new GUIItem(31) {
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

            set(new GUIClickableItem(31) {
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
                    JSONObject itemResponse = new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolFetchItem(), new JSONObject().put("uuid", auctionID.toString())).join();
                    AuctionItem item = AuctionItem.fromDocument(Document.parse(itemResponse.getString("item")));
                    if (item.getBids().size() > 0) {
                        player.sendMessage("§cCouldn't purchase the item, it has been sold!");
                        player.sendMessage("§8Returning escrowed coins...");
                        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getStartingPrice());
                        return;
                    }

                    // Add player bid to item and update it
                    item.setBids(new ArrayList<>(item.getBids()) {{
                        add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(), item.getStartingPrice().longValue()));
                    }});

                    new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolAddItem(),
                            new JSONObject().put("item", item.toDocument()).put("category", AuctionCategories.TOOLS.toString())).join();

                    player.sendMessage("§eYou purchased " + item.getItem().getDisplayName() + "§e for §6" + item.getStartingPrice() + " coins§e!");

                    ProxyPlayer owner = new ProxyPlayer(item.getOriginator());
                    if (owner.isOnline().join()) {
                        owner.sendMessage(Component.text("§6[Auction] " + player.getFullDisplayName() + " §ejust purchased your item §6" + item.getItem().getDisplayName() + "§e!").clickEvent(
                                ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/ah view " + auctionID.toString())
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

        AuctionItem.Bid highestBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);
        if (highestBid == null) {
            if (bidAmount == 0)
                bidAmount = item.getStartingPrice();
            minimumBidAmount = item.getStartingPrice();
        } else {
            if (bidAmount == 0)
                bidAmount = highestBid.value() + 1;
            minimumBidAmount = highestBid.value() + 1;
        }

        setBidHistoryItem(item);

        set(new GUIQueryItem(31) {
            @Override
            public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                long l;
                try {
                    l = Long.parseLong(query);
                } catch (NumberFormatException ex) {
                    player.sendMessage("§cCould not read this number!");
                    return GUIAuctionViewItem.this;
                }
                if (l < minimumBidAmount) {
                    player.sendMessage("§cYou need to bid at least §6" + minimumBidAmount + " coins §cto hold the top bid on this auction.");
                    return GUIAuctionViewItem.this;
                }

                bidAmount = l;

                return GUIAuctionViewItem.this;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("Bid Amount: §6" + bidAmount, Material.GOLD_INGOT, 1,
                        "§7You need to bid at least §6" + minimumBidAmount + " coins §7to",
                        "§7hold the top bid on this auction.",
                        " ",
                        "§7The §etop bid §7on auction end wins the",
                        "§7item.",
                        " ",
                        "§7If you do not win, you can claim your",
                        "§7bid coins back.",
                        " ",
                        "§eClick to edit amount!");
            }
        });
        set(new GUIClickableItem( 29) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (bidAmount < minimumBidAmount) {
                    player.sendMessage("§cYou need to bid at least §6" + minimumBidAmount + " coins §cto hold the top bid on this auction.");
                    return;
                }

                DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
                if (coins.getValue() < bidAmount) {
                    player.sendMessage("§cYou do not have enough coins to bid this amount!");
                    return;
                }

                player.sendMessage("§7Putting coins in escrow...");
                coins.setValue(coins.getValue() - bidAmount);
                player.closeInventory();

                AuctionCategories category;
                if (item.getItem().getGenericInstance() != null && item.getItem().getGenericInstance() instanceof SpecificAuctionCategory instanceCategory)
                    category = instanceCategory.getAuctionCategory();
                else {
                    category = AuctionCategories.TOOLS;
                }

                player.sendMessage("§7Processing bid...");
                Thread.startVirtualThread(() -> {
                    JSONObject itemResponse = new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolFetchItem(), new JSONObject().put("uuid", auctionID.toString())).join();
                    AuctionItem item = AuctionItem.fromDocument(Document.parse(itemResponse.getString("item")));
                    AuctionItem.Bid highestBid = item.getBids().stream().max(Comparator.comparingLong(AuctionItem.Bid::value)).orElse(null);

                    if (highestBid != null && highestBid.value() >= bidAmount) {
                        player.sendMessage("§cCouldn't place your bid, the highest bid has changed!");
                        player.sendMessage("§8Returning escrowed coins...");
                        coins.setValue(coins.getValue() + bidAmount);
                        return;
                    }

                    if (item.getEndTime() + 5000 < System.currentTimeMillis()) {
                        player.sendMessage("§cCouldn't place your bid, the auction has ended!");
                        player.sendMessage("§8Returning escrowed coins...");
                        coins.setValue(coins.getValue() + bidAmount);
                        return;
                    }

                    item.setBids(new ArrayList<>(item.getBids()) {{
                        add(new AuctionItem.Bid(System.currentTimeMillis(), player.getUuid(), bidAmount));
                    }});

                    new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolAddItem(),
                            new JSONObject().put("item", item.toDocument()).put("category", category.toString())).join();

                    player.sendMessage("§eBid of §6" + bidAmount + " coins §eplaced!");
                    new GUIAuctionViewItem(auctionID, previousGUI).open(player);

                    new ProxyPlayerSet(item.getBids().stream().map(AuctionItem.Bid::uuid).toList()).asProxyPlayers().forEach(proxyPlayer -> {
                        if (proxyPlayer.isOnline().join()) {
                            long playersBid = item.getBids().stream().filter(bid -> bid.uuid().equals(proxyPlayer.getUuid())).mapToLong(AuctionItem.Bid::value).max().orElse(0);

                            proxyPlayer.sendMessage(Component.text("§6[Auction] " + player.getFullDisplayName() + " §eout you by §6" + (bidAmount - playersBid) + " coins §efor the item §6" + item.getItem().getDisplayName() + "§e!").clickEvent(
                                    ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/ah view " + auctionID.toString())
                            ));
                        }
                    });
                });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§6Submit Bid", Material.GOLD_NUGGET, 1,
                        " ",
                        "§7New Bid: §6" + bidAmount + " coins",
                        " ",
                        "§eClick to bid!");
            }
        });
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
