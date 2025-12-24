package net.swofty.type.skyblockgeneric.gui.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.auction.AuctionItemLoreHandler;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.view.AuctionViewSelfBIN;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.view.AuctionViewSelfNormal;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.view.AuctionViewThirdBin;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.view.AuctionViewThirdNormal;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GUIAuctionViewItem extends HypixelInventoryGUI implements RefreshingGUI {
    public final UUID auctionID;
    public final HypixelInventoryGUI previousGUI;
    public long bidAmount = 0;
    public long minimumBidAmount = 0;

    public GUIAuctionViewItem(UUID auctionID, HypixelInventoryGUI previousGUI) {
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
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.updateLore(
                        PlayerItemUpdater.playerUpdate(player, new SkyBlockItem(item.getItem()).getItemStack()),
                        new AuctionItemLoreHandler(item).getLore(player)
                );
            }
        });

        if (!item.getOriginator().equals(getPlayer().getUuid())) {
            if (item.isBin()) {
                new AuctionViewThirdBin().open(this, item, (SkyBlockPlayer) getPlayer());
                return;
            }

            new AuctionViewThirdNormal().open(this, item, (SkyBlockPlayer) getPlayer());
        } else {
            if (item.isBin()) {
                new AuctionViewSelfBIN().open(this, item, (SkyBlockPlayer) getPlayer());
                return;
            }

            new AuctionViewSelfNormal().open(this, item, (SkyBlockPlayer) getPlayer());
        }
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
