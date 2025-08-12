package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.swofty.commons.auctions.AuctionItem;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import SkyBlockPlayer;

public interface AuctionView {
    void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player);
}
