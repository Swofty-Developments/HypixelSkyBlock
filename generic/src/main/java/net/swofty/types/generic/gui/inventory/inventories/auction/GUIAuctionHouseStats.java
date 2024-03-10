package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointAuctionStatistics;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIAuctionHouseStats extends SkyBlockInventoryGUI {
    public GUIAuctionHouseStats() {
        super("Auction Stats", InventoryType.CHEST_4_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, new GUIAuctionHouse()));

        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                DatapointAuctionStatistics.AuctionStatistics stats = player.getDataHandler().get(
                        DataHandler.Data.AUCTION_STATISTICS,
                        DatapointAuctionStatistics.class
                ).getValue();

                return ItemStackCreator.getStack("§aSeller Stats", Material.PAPER, 1,
                        "§7Auctions created: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_CREATED),
                        "§7Auctions completed without bids: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_COMPLETED_WITHOUT_BIDS),
                        "§7Auctions completed with bids: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_COMPLETED_WITH_BIDS),
                        " ",
                        "§7Highest auction held: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.HIGHEST_AUCTION_HELD),
                        "§7Total coins earned: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_COINS_EARNED),
                        "§7Coins spent on fees: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COINS_SPENT_ON_FEES),
                        " ",
                        "§7Common Sold: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COMMON_SOLD),
                        "§7Uncommon Sold: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.UNCOMMON_SOLD),
                        "§7Rare Sold: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.RARE_SOLD),
                        "§7Epic Sold: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.EPIC_SOLD),
                        "§7Legendary Sold: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.LEGENDARY_SOLD),
                        "§7Mythic Sold: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.MYTHIC_SOLD),
                        "§7Special Sold: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.SPECIAL_SOLD),
                        "§7Ultimate Sold: §6" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.ULTIMATE_SOLD));
            }
        });
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                DatapointAuctionStatistics.AuctionStatistics stats = player.getDataHandler().get(
                        DataHandler.Data.AUCTION_STATISTICS,
                        DatapointAuctionStatistics.class
                ).getValue();

                return ItemStackCreator.getStack("§6Buyer Stats", Material.FILLED_MAP, 1,
                        "§7Auctions won: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.AUCTIONS_WON),
                        "§7Total bids: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_BIDS),
                        " ",
                        "§7Highest bid made: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.HIGHEST_BID_MADE),
                        "§7Total coins spent: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.TOTAL_COINS_SPENT),
                        " ",
                        "§7Common Bought: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.COMMON_BOUGHT),
                        "§7Uncommon Bought: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.UNCOMMON_BOUGHT),
                        "§7Rare Bought: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.RARE_BOUGHT),
                        "§7Epic Bought: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.EPIC_BOUGHT),
                        "§7Legendary Bought: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.LEGENDARY_BOUGHT),
                        "§7Mythic Bought: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.MYTHIC_BOUGHT),
                        "§7Special Bought: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.SPECIAL_BOUGHT),
                        "§7Ultimate Bought: §a" + stats.get(DatapointAuctionStatistics.AuctionStatistics.AuctionStat.ULTIMATE_BOUGHT));
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
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
