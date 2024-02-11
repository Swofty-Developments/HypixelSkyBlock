package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.AuctionViewSelfBIN;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.AuctionViewSelfNormal;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.AuctionViewThirdBin;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.AuctionViewThirdNormal;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.types.generic.item.impl.SpecificAuctionCategory;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.protocol.ProtocolAddItem;
import net.swofty.types.generic.protocol.ProtocolDeprecateItem;
import net.swofty.types.generic.protocol.ProtocolFetchItem;
import net.swofty.types.generic.protocol.ProtocolFetchItems;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GUIAuctionViewItem extends SkyBlockInventoryGUI implements RefreshingGUI {
    public final UUID auctionID;
    public final SkyBlockInventoryGUI previousGUI;
    public long bidAmount = 0;
    public long minimumBidAmount = 0;

    public GUIAuctionViewItem(UUID auctionID, SkyBlockInventoryGUI previousGUI) {
        super("Auction View", InventoryType.CHEST_6_ROW);

        this.auctionID = auctionID;
        this.previousGUI = previousGUI;

        Thread.startVirtualThread(this::updateItems);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
    }

    public void updateItems() {
        JSONObject response = new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolFetchItem(),
                new JSONObject().put("uuid", auctionID.toString())).join();
        AuctionItem item = AuctionItem.fromDocument(Document.parse(response.getString("item")));
        set(GUIClickableItem.getGoBackItem(49, previousGUI));

        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return PlayerItemUpdater.playerUpdate(player, item.getItem().getItemStack()).lore(item.getLore(player).stream().map(line -> {
                    return Component.text(line).decoration(TextDecoration.ITALIC, false);
                }).toList());
            }
        });

        if (!item.getOriginator().equals(getPlayer().getUuid())) {
            if (item.isBin()) {
                new AuctionViewThirdBin().open(this, item, getPlayer());
                return;
            }

            new AuctionViewThirdNormal().open(this, item, getPlayer());
        } else {
            if (item.isBin()) {
                new AuctionViewSelfBIN().open(this, item, getPlayer());
                return;
            }

            new AuctionViewSelfNormal().open(this, item, getPlayer());
        }
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
        }

        updateItems();
    }

    @Override
    public int refreshRate() {
        return 10;
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
