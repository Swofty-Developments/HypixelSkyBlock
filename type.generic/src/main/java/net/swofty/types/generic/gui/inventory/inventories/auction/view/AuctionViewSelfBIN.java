package net.swofty.types.generic.gui.inventory.inventories.auction.view;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.auctions.AuctionItem;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionViewItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;
import java.util.UUID;

public class AuctionViewSelfBIN implements AuctionView {
    private static final String STATE_SOLD = "sold";
    private static final String STATE_SOLD_CLAIMED = "sold_claimed";
    private static final String STATE_EXPIRED = "expired";
    private static final String STATE_EXPIRED_CLAIMED = "expired_claimed";
    private static final String STATE_ACTIVE = "active";

    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        List<UUID> ownedActive = player.getDataHandler()
                .get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
        List<UUID> ownedInactive = player.getDataHandler()
                .get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

        // Determine the current state
        if (!item.getBids().isEmpty()) {
            if (ownedActive.contains(item.getUuid())) {
                gui.getStates().add(STATE_SOLD);
            } else {
                gui.getStates().add(STATE_SOLD_CLAIMED);
            }
        } else if (item.getEndTime() < System.currentTimeMillis()) {
            if (ownedActive.contains(item.getUuid())) {
                gui.getStates().add(STATE_EXPIRED);
            } else {
                gui.getStates().add(STATE_EXPIRED_CLAIMED);
            }
        } else {
            gui.getStates().add(STATE_ACTIVE);
        }

        // Sold auction - collect coins
        gui.attachItem(GUIItem.builder(31)
                .item(() -> ItemStackCreator.getStack("§6Collect Auction", Material.GOLD_BLOCK, 1,
                        " ",
                        "§7This item has been sold!",
                        " ",
                        SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" + item.getBids().getFirst().value() + " coins",
                        " ",
                        "§eClick to collect coins!").build())
                .requireState(STATE_SOLD)
                .onClick((ctx, clickedItem) -> {
                    double coins = ctx.player().getDataHandler()
                            .get(DataHandler.Data.COINS, DatapointDouble.class).getValue();

                    ctx.player().sendMessage("§8Claiming your coins...");
                    ctx.player().getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class)
                            .setValue(coins + item.getBids().getFirst().value());

                    ownedActive.remove(item.getUuid());
                    ctx.player().getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class)
                            .setValue(ownedActive);
                    ownedInactive.add(item.getUuid());
                    ctx.player().getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class)
                            .setValue(ownedInactive);

                    ctx.player().sendMessage("§eYou collected §6" + item.getBids().getFirst().value() + " coins §efrom the auction!");
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Expired auction - claim item back
        gui.attachItem(GUIItem.builder(31)
                .item(() -> ItemStackCreator.getStack("§eClaim Item Back", Material.GOLD_INGOT, 1,
                        " ",
                        "§7This auction has ended!",
                        "§7Nobody bought it :(",
                        " ",
                        "§eClick to claim it back!").build())
                .requireState(STATE_EXPIRED)
                .onClick((ctx, clickedItem) -> {
                    ctx.player().sendMessage("§8Claiming your item...");
                    ownedActive.remove(item.getUuid());
                    ctx.player().getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class)
                            .setValue(ownedActive);
                    ownedInactive.add(item.getUuid());
                    ctx.player().getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class)
                            .setValue(ownedInactive);

                    ctx.player().addAndUpdateItem(item.getItem());
                    ctx.player().sendMessage("§aYou have claimed your item.");
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Already claimed auction
        gui.attachItem(GUIItem.builder(31)
                .item(() -> ItemStackCreator.getStack("§cYou Cannot Buy Your Own Item", Material.BEDROCK, 1,
                        " ",
                        "§7You cannot buy your own item!",
                        " ",
                        "§cYou have already claimed your item!").build())
                .requireState(STATE_SOLD_CLAIMED)
                .build());

        // Expired and claimed auction
        gui.attachItem(GUIItem.builder(31)
                .item(() -> ItemStackCreator.getStack("§cYou Cannot Buy Your Own Item", Material.BEDROCK, 1,
                        " ",
                        "§7You cannot buy your own item!",
                        " ",
                        "§cThis item has expired and has been returned to you.").build())
                .requireState(STATE_EXPIRED_CLAIMED)
                .build());

        // Active auction
        gui.attachItem(GUIItem.builder(31)
                .item(() -> ItemStackCreator.getStack("§cYou Cannot Buy Your Own Item", Material.BEDROCK, 1,
                        " ",
                        "§7You cannot buy your own item!",
                        " ",
                        "§7You can only buy items from other players.").build())
                .requireState(STATE_ACTIVE)
                .build());
    }
}