package net.swofty.type.skyblockgeneric.gui.inventories.auction;

import net.minestom.server.component.DataComponents;
import org.tinylog.Logger;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.skyblockgeneric.auction.AuctionItemLoreHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GUIViewBids extends HypixelInventoryGUI implements RefreshingGUI {
    public GUIViewBids() {
        super("Your Bids", InventoryType.CHEST_3_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(22, new GUIAuctionHouse()));
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        setItems();
    }

    public void setItems() {
        List<UUID> auctions = ((SkyBlockPlayer) getPlayer()).getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).getValue();
        List<CompletableFuture<AuctionFetchItemProtocolObject.AuctionFetchItemResponse>> futures = new ArrayList<>(auctions.size());
        PaginationList<AuctionItem> auctionItems = new PaginationList<>(7);

        auctions.forEach(uuid -> {
            AuctionFetchItemProtocolObject.AuctionFetchItemMessage message =
                    new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(uuid);
            CompletableFuture<AuctionFetchItemProtocolObject.AuctionFetchItemResponse> future =
                    new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message);

            future.thenAccept(response -> {
                AuctionItem item = response.item();
                synchronized (auctionItems) {
                    auctionItems.add(item);
                }
            }).exceptionally(ex -> {
                Logger.error(ex, "Failed to process auction bid");
                return null;
            });
            futures.add(future);
        });

        CompletableFuture<Void> allDone = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allDone.join();

        // Sort the items by the time they were added
        auctionItems.sort((o1, o2) -> Long.compare(o2.getEndTime(), o1.getEndTime()));

        List<AuctionItem> auctionItemsPage = auctionItems.getPage(1);

        for (int i = 0; i < 7; i++) {
            int slot = i + 10;

            if (i >= auctionItems.size()) {
                set(new GUIItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStack.builder(Material.AIR);
                    }
                });
                continue;
            }

            AuctionItem item = auctionItemsPage.get(i);
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIAuctionViewItem(item.getUuid(), GUIViewBids.this).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            StringUtility.getTextFromComponent(new NonPlayerItemUpdater(item.getItem()).getUpdatedItem().build()
                                    .get(DataComponents.CUSTOM_NAME)),
                            item.getItem().material(), item.getItem().amount(), new AuctionItemLoreHandler(item).getLore(player));
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

    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
            return;
        }

        setItems();
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
