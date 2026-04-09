package net.swofty.type.skyblockgeneric.gui.inventories.auction;

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

import java.util.Map;
import static java.util.Map.entry;

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
                        "gui_auction.stats.seller_stats.lore", Map.ofEntries(
                                entry("auctions_created", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_CREATED))),
                                entry("completed_without_bids", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_COMPLETED_WITHOUT_BIDS))),
                                entry("completed_with_bids", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_COMPLETED_WITH_BIDS))),
                                entry("highest_auction", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.HIGHEST_AUCTION_HELD))),
                                entry("total_earned", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_COINS_EARNED))),
                                entry("fees_spent", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COINS_SPENT_ON_FEES))),
                                entry("common_sold", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COMMON_SOLD))),
                                entry("uncommon_sold", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.UNCOMMON_SOLD))),
                                entry("rare_sold", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.RARE_SOLD))),
                                entry("epic_sold", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.EPIC_SOLD))),
                                entry("legendary_sold", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.LEGENDARY_SOLD))),
                                entry("mythic_sold", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.MYTHIC_SOLD))),
                                entry("special_sold", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.SPECIAL_SOLD))),
                                entry("ultimate_sold", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.ULTIMATE_SOLD)))
                        ));
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
                        "gui_auction.stats.buyer_stats.lore", Map.ofEntries(
                                entry("auctions_won", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_WON))),
                                entry("total_bids", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_BIDS))),
                                entry("highest_bid", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.HIGHEST_BID_MADE))),
                                entry("total_spent", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_COINS_SPENT))),
                                entry("common_bought", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COMMON_BOUGHT))),
                                entry("uncommon_bought", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.UNCOMMON_BOUGHT))),
                                entry("rare_bought", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.RARE_BOUGHT))),
                                entry("epic_bought", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.EPIC_BOUGHT))),
                                entry("legendary_bought", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.LEGENDARY_BOUGHT))),
                                entry("mythic_bought", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.MYTHIC_BOUGHT))),
                                entry("special_bought", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.SPECIAL_BOUGHT))),
                                entry("ultimate_bought", String.valueOf(stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.ULTIMATE_BOUGHT)))
                        ));
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
