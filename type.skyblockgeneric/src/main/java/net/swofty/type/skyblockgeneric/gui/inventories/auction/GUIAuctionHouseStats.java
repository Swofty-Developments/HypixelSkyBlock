package net.swofty.type.skyblockgeneric.gui.inventories.auction;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointAuctionStatistics;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIAuctionHouseStats extends HypixelInventoryGUI {
    public GUIAuctionHouseStats() {
        super(I18n.t("gui_auction.stats.title"), InventoryType.CHEST_4_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, new GUIAuctionHouse()));

        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointAuctionStatistics.AuctionStatistics stats = player.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.AUCTION_STATISTICS,
                        DatapointAuctionStatistics.class
                ).getValue();

                return TranslatableItemStackCreator.getStack("gui_auction.stats.seller_stats", Material.PAPER, 1,
                    "gui_auction.stats.seller_stats.lore",
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_CREATED))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_COMPLETED_WITHOUT_BIDS))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_COMPLETED_WITH_BIDS))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.HIGHEST_AUCTION_HELD))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_COINS_EARNED))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COINS_SPENT_ON_FEES))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COMMON_SOLD))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.UNCOMMON_SOLD))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.RARE_SOLD))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.EPIC_SOLD))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.LEGENDARY_SOLD))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.MYTHIC_SOLD))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.SPECIAL_SOLD))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.ULTIMATE_SOLD))));
            }
        });
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                DatapointAuctionStatistics.AuctionStatistics stats = player.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.AUCTION_STATISTICS,
                        DatapointAuctionStatistics.class
                ).getValue();

                return TranslatableItemStackCreator.getStack("gui_auction.stats.buyer_stats", Material.FILLED_MAP, 1,
                    "gui_auction.stats.buyer_stats.lore",
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_WON))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_BIDS))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.HIGHEST_BID_MADE))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_COINS_SPENT))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COMMON_BOUGHT))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.UNCOMMON_BOUGHT))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.RARE_BOUGHT))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.EPIC_BOUGHT))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.LEGENDARY_BOUGHT))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.MYTHIC_BOUGHT))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.SPECIAL_BOUGHT))),
                    Component.text(String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.ULTIMATE_BOUGHT))));
            }
        });
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
}
