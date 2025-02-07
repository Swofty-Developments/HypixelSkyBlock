package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItemLoreHandler;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.auction.view.*;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GUIAuctionViewItem extends SkyBlockAbstractInventory {
    private static final String STATE_SELF_BIN = "self_bin";
    private static final String STATE_SELF_NORMAL = "self_normal";
    private static final String STATE_THIRD_BIN = "third_bin";
    private static final String STATE_THIRD_NORMAL = "third_normal";

    public final UUID auctionID;
    public final SkyBlockAbstractInventory previousGUI;
    public long bidAmount = 0;
    public long minimumBidAmount = 0;

    public GUIAuctionViewItem(UUID auctionID, SkyBlockAbstractInventory previousGUI) {
        super(InventoryType.CHEST_6_ROW);
        this.auctionID = auctionID;
        this.previousGUI = previousGUI;

        doAction(new SetTitleAction(Component.text("Auction View")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Back button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + previousGUI.getTitleAsString()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(previousGUI);
                    return true;
                })
                .build());

        Thread.startVirtualThread(() -> updateItems(player));
        startLoop("refresh", 10, () -> checkAuctionHouse(player));
    }

    private void checkAuctionHouse(SkyBlockPlayer player) {
        new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().thenAccept(online -> {
            if (!online) {
                player.sendMessage("§cAuction House is currently offline!");
                player.closeInventory();
                return;
            }
            updateItems(player);
        });
    }

    private void updateItems(SkyBlockPlayer player) {
        AuctionFetchItemProtocolObject.AuctionFetchItemMessage message =
                new AuctionFetchItemProtocolObject.AuctionFetchItemMessage(auctionID);
        CompletableFuture<AuctionFetchItemProtocolObject.AuctionFetchItemResponse> future =
                new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message);
        AuctionItem item = future.join().item();

        // Auction Item Display
        attachItem(GUIItem.builder(13)
                .item(() -> ItemStackCreator.updateLore(
                        PlayerItemUpdater.playerUpdate(player, new SkyBlockItem(item.getItem()).getItemStack()),
                        new AuctionItemLoreHandler(item).getLore(player)).build())
                .build());

        // Set appropriate view state and open corresponding interface
        if (item.getOriginator().equals(player.getUuid())) {
            if (item.isBin()) {
                doAction(new AddStateAction(STATE_SELF_BIN));
                doAction(new RemoveStateAction(STATE_SELF_NORMAL));
                doAction(new RemoveStateAction(STATE_THIRD_BIN));
                doAction(new RemoveStateAction(STATE_THIRD_NORMAL));
                new AuctionViewSelfBIN().open(this, item, player);
            } else {
                doAction(new AddStateAction(STATE_SELF_NORMAL));
                doAction(new RemoveStateAction(STATE_SELF_BIN));
                doAction(new RemoveStateAction(STATE_THIRD_BIN));
                doAction(new RemoveStateAction(STATE_THIRD_NORMAL));
                new AuctionViewSelfNormal().open(this, item, player);
            }
        } else {
            if (item.isBin()) {
                doAction(new AddStateAction(STATE_THIRD_BIN));
                doAction(new RemoveStateAction(STATE_SELF_BIN));
                doAction(new RemoveStateAction(STATE_SELF_NORMAL));
                doAction(new RemoveStateAction(STATE_THIRD_NORMAL));
                new AuctionViewThirdBin().open(this, item, player);
            } else {
                doAction(new AddStateAction(STATE_THIRD_NORMAL));
                doAction(new RemoveStateAction(STATE_SELF_BIN));
                doAction(new RemoveStateAction(STATE_SELF_NORMAL));
                doAction(new RemoveStateAction(STATE_THIRD_BIN));
                new AuctionViewThirdNormal().open(this, item, player);
            }
        }
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