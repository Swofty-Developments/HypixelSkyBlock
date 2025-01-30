package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIAuctionHouse extends SkyBlockAbstractInventory {
    private static final String STATE_HAS_ACTIVE_AUCTIONS = "has_active_auctions";
    private static final String STATE_HAS_ACTIVE_BIDS = "has_active_bids";
    private static final String STATE_OFFLINE = "offline";

    public GUIAuctionHouse() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Auction House")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Check if auction house is online
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            doAction(new AddStateAction(STATE_OFFLINE));
            fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "§cAuction House is currently offline!").build());
            return;
        }

        // Set states based on player data
        if (!player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().isEmpty()) {
            doAction(new AddStateAction(STATE_HAS_ACTIVE_AUCTIONS));
        }
        if (!player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_BIDS, DatapointUUIDList.class).getValue().isEmpty()) {
            doAction(new AddStateAction(STATE_HAS_ACTIVE_BIDS));
        }

        // Basic setup
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Close button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Stats button
        attachItem(GUIItem.builder(32)
                .item(ItemStackCreator.getStack("§aAuction Stats", Material.PAPER, 1,
                        "§7View various statistics about you and",
                        "§7the Auction House.",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIAuctionHouseStats());
                    return true;
                })
                .build());

        // Browser button
        attachItem(GUIItem.builder(11)
                .item(ItemStackCreator.getStack("§6Auctions Browser", Material.GOLD_BLOCK, 1,
                        "§7Find items for sale by players",
                        "§7across Hypixel SkyBlock!",
                        " ",
                        "§7Items offered here are for §6auction§7,",
                        "§7meaning you have to place the top",
                        "§7bid to acquire them!",
                        " ",
                        "§eClick to browse!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIAuctionBrowser());
                    return true;
                })
                .build());

        // Create/Manage Auctions button
        attachItem(GUIItem.builder(15)
                .item(() -> {
                    if (hasState(STATE_HAS_ACTIVE_AUCTIONS)) {
                        return ItemStackCreator.getStack("§aManage Auctions", Material.GOLDEN_HORSE_ARMOR, 1,
                                "§7You own §e" + player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue().size() + " auctions §7in progress or",
                                "§7which recently ended.",
                                " ",
                                "§eClick to manage!").build();
                    } else {
                        return ItemStackCreator.getStack("§aCreate Auction", Material.GOLDEN_HORSE_ARMOR, 1,
                                "§7Set your own items on auction for",
                                "§7other players to purchase.",
                                " ",
                                "§eClick to become rich!").build();
                    }
                })
                .onClick((ctx, item) -> {
                    if (hasState(STATE_HAS_ACTIVE_AUCTIONS)) {
                        ctx.player().openInventory(new GUIManageAuctions());
                    } else {
                        ctx.player().openInventory(new GUIAuctionCreateItem(this));
                    }
                    return true;
                })
                .build());

        // View Bids button
        attachItem(GUIItem.builder(13)
                .item(ItemStackCreator.getStack("§aView Bids", Material.GOLDEN_CARROT, 1,
                        "§7You've placed bids, check up on",
                        "§7them here!",
                        " ",
                        "§eClick to view!").build())
                .requireState(STATE_HAS_ACTIVE_BIDS)
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIViewBids());
                    return true;
                })
                .build());

        // Start refresh loop
        startLoop("refresh", 20, () -> refreshItems(player));
    }

    private void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No cleanup needed
    }
}