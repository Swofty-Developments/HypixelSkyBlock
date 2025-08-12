package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.swofty.commons.auctions.AuctionItem;
import net.swofty.type.generic.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.generic.user.HypixelPlayer;

public interface AuctionView {
    void open(GUIAuctionViewItem gui, AuctionItem item, HypixelPlayer player);
}
