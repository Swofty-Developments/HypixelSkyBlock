package net.swofty.type.skyblockgeneric.gui.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIAuctionHouse extends HypixelInventoryGUI implements RefreshingGUI {
    public GUIAuctionHouse() {
        super("Auction House", InventoryType.CHEST_4_ROW);

        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join())
            fill(Material.BLACK_STAINED_GLASS_PANE, "§cAuction House is currently offline!");
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(31));
        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIAuctionHouseStats().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aAuction Stats", Material.PAPER, 1,
                        "§7View various statistics about you and",
                        "§7the Auction House.",
                        " ",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIAuctionBrowser().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
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

        if (((SkyBlockPlayer) getPlayer()).getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().isEmpty()) {
            set(new GUIClickableItem(15) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIAuctionCreateItem(GUIAuctionHouse.this).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
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
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIManageAuctions().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§aManage Auctions", Material.GOLDEN_HORSE_ARMOR, 1,
                            "§7You own §e" + player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().size() + " auctions §7in progress or",
                            "§7which recently ended.",
                            " ",
                            "§eClick to manage!");
                }
            });
        }

        if (!((SkyBlockPlayer) getPlayer()).getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).getValue().isEmpty()) {
            set(new GUIClickableItem(13) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIViewBids().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 20;
    }
}
