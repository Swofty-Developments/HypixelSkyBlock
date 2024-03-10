package net.swofty.types.generic.gui.inventory.inventories.auction.view;

import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionViewItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface AuctionView {
    void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player);
}
