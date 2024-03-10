package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIAuctionHouse extends SkyBlockInventoryGUI implements RefreshingGUI {
    public GUIAuctionHouse() {
        super("Auction House", InventoryType.CHEST_4_ROW);

        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline(new ProtocolPingSpecification()).join())
            fill(Material.BLACK_STAINED_GLASS_PANE, "§cAuction House is currently offline!");
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(31));
        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIAuctionHouseStats().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aAuction Stats", Material.PAPER, 1,
                        "§7View various statistics about you and",
                        "§7the Auction House.",
                        " ",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIAuctionBrowser().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§6Auctions Browser", Material.GOLD_BLOCK, 1,
                        "§7Find items for sale by players",
                        "§7across Hypixel SkyBlock!",
                        " ",
                        "§7Items offered here are for §6auction§7,",
                        "§7meaning you have to place the top",
                        "§7bid to acquire them!",
                        " ",
                        "§eClick to browse!");
            }
        });

        if (getPlayer().getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().isEmpty()) {
            set(new GUIClickableItem(15) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIAuctionCreateItem(GUIAuctionHouse.this).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aCreate Auction", Material.GOLDEN_HORSE_ARMOR, 1,
                            "§7Set your own items on auction for",
                            "§7other players to purchase.",
                            " ",
                            "§eClick to become rich!");
                }
            });
        } else {
            set(new GUIClickableItem(15) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIManageAuctions().open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aManage Auctions", Material.GOLDEN_HORSE_ARMOR, 1,
                            "§7You own §e" + player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().size() + " auctions §7in progress or",
                            "§7which recently ended.",
                            " ",
                            "§eClick to manage!");
                }
            });
        }

        if (!getPlayer().getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).getValue().isEmpty()) {
            set(new GUIClickableItem(13) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIViewBids().open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aView Bids", Material.GOLDEN_CARROT, 1,
                            "§7You've placed bids, check up on",
                            "§7them here!",
                            " ",
                            "§eClick to view!");
                }
            });
        }
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
        e.setCancelled(true);
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
        return 20;
    }
}
