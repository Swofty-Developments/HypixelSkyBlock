package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItemLoreHandler;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.AuctionViewSelfBIN;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.AuctionViewSelfNormal;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.AuctionViewThirdBin;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.AuctionViewThirdNormal;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        AuctionFetchItemProtocolObject.AuctionFetchItemMessage message =
                new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(auctionID);
        CompletableFuture<AuctionFetchItemProtocolObject.AuctionFetchItemResponse> future =
                new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message);
        AuctionItem item = future.join().item();

        set(GUIClickableItem.getGoBackItem(49, previousGUI));

        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.updateLore(
                        PlayerItemUpdater.playerUpdate(player, new SkyBlockItem(item.getItem()).getItemStack()),
                        new AuctionItemLoreHandler(item).getLore(player)
                );
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
