package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointAuctionEscrow;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SpecificAuctionCategory;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.protocol.auctions.ProtocolAddItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIAuctionCreateItem extends SkyBlockInventoryGUI implements RefreshingGUI {
    private SkyBlockInventoryGUI previousGUI;

    public GUIAuctionCreateItem(SkyBlockInventoryGUI previousGUI) {
        super("Create Auction", InventoryType.CHEST_6_ROW);

        this.previousGUI = previousGUI;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getGoBackItem(49, previousGUI));

        DatapointAuctionEscrow.AuctionEscrow escrow = getPlayer().getDataHandler().get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue();
        if (escrow.isBin())
            e.inventory().setTitle(Component.text("Create BIN Auction"));

        set(new GUIClickableItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (escrow.getItem() == null)
                    return ItemStackCreator.getStack("§eClick an item in your inventory!", Material.STONE_BUTTON, 1,
                            "§7Selects it for auction");

                SkyBlockItem item = escrow.getItem();
                ItemStack itemStack = new NonPlayerItemUpdater(item).getUpdatedItem().build();
                List<String> lore = new ArrayList<>();

                lore.add(" ");
                lore.add(StringUtility.getTextFromComponent(itemStack.getDisplayName()));
                itemStack.getLore().forEach(loreEntry -> {
                    lore.add(StringUtility.getTextFromComponent(loreEntry));
                });
                lore.add(" ");
                lore.add("§eClick to pickup!");

                return ItemStackCreator.getStack("§a§lAUCTION FOR ITEM:", itemStack.getMaterial(), itemStack.getAmount(), lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (escrow.getItem() == null) return;
                player.addAndUpdateItem(escrow.getItem());
                escrow.setItem(null);

                new GUIAuctionCreateItem(previousGUI).open(player);
            }
        });

        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIAuctionDuration().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>();
                if (escrow.isBin()) {
                    lore.add("§7How long the item will be");
                    lore.add("§7up for sale.");
                } else {
                    lore.add("§7How long players will be");
                    lore.add("§7able to place bids for.");
                    lore.add(" ");
                    lore.add("§7Note: Bids automatically");
                    lore.add("§7increase the duration of");
                    lore.add("§7auctions.");
                }
                lore.add(" ");
                lore.add("§7Extra fee: §6+" + (escrow.getDuration() / 180000) + " coins");
                lore.add(" ");
                lore.add("§eClick to edit!");

                return ItemStackCreator.getStack("Duration: §e" + StringUtility.getAuctionSetupFormattedTime(escrow.getDuration()),
                        Material.CLOCK, 1, lore);
            }
        });
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                escrow.setBin(!escrow.isBin());
                new GUIAuctionCreateItem(previousGUI).open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (escrow.isBin()) {
                    return ItemStackCreator.getStack("§aSwitch to Auction", Material.POWERED_RAIL, 1,
                            "§7With traditional auctions, multiple",
                            "§7buyers compete for the item by",
                            "§7bidding turn by turn.",
                            " ",
                            "§eClick to switch!");
                } else {
                    return ItemStackCreator.getStack("§aSwitch to BIN", Material.GOLD_INGOT, 1,
                            "§7BIN Auctions are simple.",
                            " ",
                            "§7Set a price, then one player may buy",
                            "§7the item at that price.",
                            " ",
                            "§8(BIN means Buy It Now)",
                            " ",
                            "§eClick to switch!");
                }
            }
        });
        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                ProxyService auctionService = new ProxyService(ServiceType.AUCTION_HOUSE);

                auctionService.isOnline(new ProtocolPingSpecification()).thenAccept((response) -> {
                    if (escrow.getItem() == null || !response)
                        return;

                    long fee = (long) ((escrow.getPrice() * 0.05) + (escrow.getDuration() / 180000));
                    DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
                    if (coins.getValue() < fee) {
                        player.sendMessage("§cYou don't have enough coins to create this auction!");
                        return;
                    }
                    coins.setValue(coins.getValue() - fee);

                    player.closeInventory();

                    player.sendMessage("§7Putting item in escrow...");

                    ItemStack builtItem = new NonPlayerItemUpdater(escrow.getItem()).getUpdatedItem().build();
                    AuctionItem item = new AuctionItem(escrow.getItem(), player.getUuid(), escrow.getDuration() + System.currentTimeMillis(),
                            escrow.isBin(), escrow.getPrice());
                    String itemName = StringUtility.getTextFromComponent(builtItem.getDisplayName());

                    AuctionCategories category = AuctionCategories.TOOLS;
                    if (escrow.getItem().getGenericInstance() != null && escrow.getItem().getGenericInstance() instanceof SpecificAuctionCategory instanceCategory)
                        category = instanceCategory.getAuctionCategory();

                    player.getDataHandler().get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).clearEscrow();
                    player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().add(item.getUuid());

                    player.sendMessage("§7Setting up the auction...");

                    Map<String, Object> requestMessage = new HashMap<>();
                    requestMessage.put("category", category);
                    requestMessage.put("item", item);

                    auctionService.callEndpoint(new ProtocolAddItem(), requestMessage).thenAccept(response2 -> {
                        player.sendMessage("§eAuction started for " + itemName + "§e!");
                        player.sendMessage("§8ID: " + response2.get("uuid"));
                    });
                });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (escrow.getItem() == null) {
                    return ItemStackCreator.getStack("§cCreate Auction", Material.RED_TERRACOTTA, 1,
                            "§7No item selected!",
                            " ",
                            "§7Click an item in your inventory to",
                            "§7select it for this auction.");
                } else {
                    ItemStack builtItem = new NonPlayerItemUpdater(escrow.getItem()).getUpdatedItem().build();

                    return ItemStackCreator.getStack("§aCreate " + (escrow.isBin() ? "Bin " : "") + "Auction", Material.GREEN_TERRACOTTA, 1,
                            "§7This item will be added to the auction",
                            "§7house for other players to",
                            "§7purchase.",
                            " ",
                            "§7Item: " + StringUtility.getTextFromComponent(builtItem.getDisplayName()),
                            "§7Auction Duration: §e" + StringUtility.getAuctionSetupFormattedTime(escrow.getDuration()),
                            "§7" + (escrow.isBin() ? "Item Price" : "Starting bid") + ": §e" + StringUtility.commaify(escrow.getPrice()) + " coins",
                            " ",
                            "§7Creation fee: §6+" + ((escrow.getPrice() * 0.05) + (escrow.getDuration() / 180000)) + " coins",
                            " ",
                            "§eClick to submit!");
                }
            }
        });
        set(new GUIQueryItem(31) {

            @Override
            public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                long l;
                try {
                    l = Long.parseLong(query);
                } catch (NumberFormatException ex) {
                    player.sendMessage("§cCould not read this number!");
                    return GUIAuctionCreateItem.this;
                }
                if (l <= 50) {
                    player.sendMessage("§cThat price is too low for your item!");
                    return GUIAuctionCreateItem.this;
                }
                escrow.setPrice(l);

                return GUIAuctionCreateItem.this;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                Material material;
                List<String> lore = new ArrayList<>();
                if (escrow.isBin()) {
                    material = Material.GOLD_INGOT;

                    lore.add("§7The price at which you want");
                    lore.add("§7to sell this item.");
                } else {
                    material = Material.POWERED_RAIL;

                    lore.add("§7The minimum price a player");
                    lore.add("§7can offer to obtain your");
                    lore.add("§7item.");
                    lore.add(" ");
                    lore.add("§7Once a player bids for your");
                    lore.add("§7item, other players will");
                    lore.add("§7have until the auction ends");
                    lore.add("§7to make a higher bid.");
                }
                lore.add(" ");
                lore.add("§7Extra fee: §6+" + (escrow.getPrice() * 0.05) + " coins §e(5%)");
                lore.add(" ");
                lore.add("§eClick to edit!");

                return ItemStackCreator.getStack((escrow.isBin() ? "Item Price: " : "Starting bid: ")
                                + "§e" + StringUtility.commaify(escrow.getPrice()) + " coins",
                        material, 1, lore);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
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

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        ItemStack current = e.getClickedItem();
        SkyBlockItem item = new SkyBlockItem(current);

        if (item.isNA()) return;
        if (item.isAir()) return;

        DatapointAuctionEscrow.AuctionEscrow escrow = getPlayer().getDataHandler().get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue();

        if (escrow.getItem() != null) {
            e.getPlayer().sendMessage("§cYou already have an item in the auction slot!");
            return;
        }

        e.setCancelled(true);
        escrow.setItem(item);
        e.getPlayer().getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
        new GUIAuctionCreateItem(previousGUI).open(getPlayer());
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline(new ProtocolPingSpecification()).join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
