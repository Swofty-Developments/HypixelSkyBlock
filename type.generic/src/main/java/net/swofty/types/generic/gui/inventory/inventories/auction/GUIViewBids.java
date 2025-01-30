package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItemLoreHandler;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GUIViewBids extends SkyBlockAbstractInventory {
    private static final String STATE_AUCTION_HOUSE_ONLINE = "auction_house_online";
    private static final String STATE_AUCTION_HOUSE_OFFLINE = "auction_house_offline";

    public GUIViewBids() {
        super(InventoryType.CHEST_3_ROW);
        doAction(new SetTitleAction(Component.text("Your Bids")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Back button
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Auction House").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIAuctionHouse());
                    return true;
                })
                .build());

        checkAuctionHouseStatus(player);
        startLoop("refresh", 10, () -> checkAuctionHouseStatus(player));
    }

    private void checkAuctionHouseStatus(SkyBlockPlayer player) {
        new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().thenAccept(online -> {
            if (!online) {
                doAction(new SetTitleAction(Component.text("Auction House Offline")));
                player.sendMessage("§cAuction House is currently offline!");
                player.closeInventory();
                return;
            }

            loadAuctionItems(player);
        });
    }

    private void loadAuctionItems(SkyBlockPlayer player) {
        List<UUID> auctions = player.getDataHandler()
                .get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).getValue();
        List<CompletableFuture<AuctionFetchItemProtocolObject.AuctionFetchItemResponse>> futures =
                new ArrayList<>(auctions.size());
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
                ex.printStackTrace();
                return null;
            });
            futures.add(future);
        });

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenRun(() -> {
            auctionItems.sort((o1, o2) -> Long.compare(o2.getEndTime(), o1.getEndTime()));
            List<AuctionItem> auctionItemsPage = auctionItems.getPage(1);

            for (int i = 0; i < 7; i++) {
                int slot = i + 10;
                final int index = i;

                if (i >= auctionItems.size()) {
                    attachItem(GUIItem.builder(slot)
                            .item(ItemStack.AIR)
                            .build());
                    continue;
                }

                AuctionItem item = auctionItemsPage.get(i);
                attachItem(GUIItem.builder(slot)
                        .item(() -> {
                            ItemStack.Builder baseItem = new NonPlayerItemUpdater(item.getItem()).getUpdatedItem();
                            return ItemStackCreator.getStack(
                                            StringUtility.getTextFromComponent(baseItem.build()
                                                    .get(ItemComponent.CUSTOM_NAME)),
                                            item.getItem().material(),
                                            item.getItem().amount(),
                                            new AuctionItemLoreHandler(item).getLore(player))
                                    .build();
                        })
                        .onClick((ctx, clickedItem) -> {
                            ctx.player().openInventory(new GUIAuctionViewItem(item.getUuid(), this));
                            return true;
                        })
                        .build());
            }
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}