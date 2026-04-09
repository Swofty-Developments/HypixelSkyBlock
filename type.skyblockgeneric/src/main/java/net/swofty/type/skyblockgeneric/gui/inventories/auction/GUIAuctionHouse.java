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
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;

public class GUIAuctionHouse extends HypixelInventoryGUI implements RefreshingGUI {
    public GUIAuctionHouse() {
        super(I18n.t("gui_auction.house.title"), InventoryType.CHEST_4_ROW);

        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join())
            fill(Material.BLACK_STAINED_GLASS_PANE, "");
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
                return TranslatableItemStackCreator.getStack("gui_auction.house.stats_button", Material.PAPER, 1,
                        "gui_auction.house.stats_button.lore");
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
                return TranslatableItemStackCreator.getStack("gui_auction.house.browser_button", Material.GOLD_BLOCK, 1,
                        "gui_auction.house.browser_button.lore");
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
                    return TranslatableItemStackCreator.getStack("gui_auction.house.create_button", Material.GOLDEN_HORSE_ARMOR, 1,
                            "gui_auction.house.create_button.lore");
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
                    return TranslatableItemStackCreator.getStack("gui_auction.house.manage_button", Material.GOLDEN_HORSE_ARMOR, 1,
                            "gui_auction.house.manage_button.lore", Map.of(
                                    "count", String.valueOf(player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().size())
                            ));
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
                    return TranslatableItemStackCreator.getStack("gui_auction.house.bids_button", Material.GOLDEN_CARROT, 1,
                            "gui_auction.house.bids_button.lore");
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
            player.sendMessage(I18n.t("gui_auction.house.offline_message"));
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 20;
    }
}
