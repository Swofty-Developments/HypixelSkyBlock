package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AuctionViewSelfBIN implements AuctionView {
    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        if (!item.getBids().isEmpty()) {
            List<UUID> ownedActive = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
            List<UUID> ownedInactive = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

            if (ownedActive.contains(item.getUuid())) {
                gui.set(new GUIClickableItem(31) {
                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        double coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();

                        player.sendMessage(I18n.string("gui_auction.view_self_bin.claiming_coins"));
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getBids().getFirst().value());

                        ownedActive.remove(item.getUuid());
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                        ownedInactive.add(item.getUuid());
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                        player.sendMessage(I18n.string("gui_auction.view_self_bin.collected_coins", Map.of("amount", String.valueOf(item.getBids().getFirst().value()))));
                        player.closeInventory();
                    }

                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStackCreator.getStack(I18n.string("gui_auction.view_self_bin.collect_auction"), Material.GOLD_BLOCK, 1,
                                I18n.lore("gui_auction.view_self_bin.collect_sold.lore", Map.of(
                                        "buyer_name", SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()),
                                        "price", String.valueOf(item.getBids().getFirst().value())
                                )));
                    }
                });
            } else {
                gui.set(new GUIItem(31) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStackCreator.getStack(I18n.string("gui_auction.view_self_bin.cannot_buy_own"), Material.BEDROCK, 1,
                                I18n.lore("gui_auction.view_self_bin.cannot_buy_own_claimed.lore"));
                    }
                });
            }
            return;
        }

        if (item.getEndTime() < System.currentTimeMillis()) {
            // Noone bought the item, so give it back to the player if in active
            List<UUID> ownedActive = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
            List<UUID> ownedInactive = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

            if (ownedActive.contains(item.getUuid())) {
                gui.set(new GUIClickableItem(31) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStackCreator.getStack(I18n.string("gui_auction.view_self_bin.claim_item_back"), Material.GOLD_INGOT, 1,
                                I18n.lore("gui_auction.view_self_bin.claim_item_back.lore"));
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        player.sendMessage(I18n.string("gui_auction.view_self_bin.claiming_item"));
                        ownedActive.remove(item.getUuid());
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                        ownedInactive.add(item.getUuid());
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                        player.addAndUpdateItem(item.getItem());

                        player.sendMessage(I18n.string("gui_auction.view_self_bin.claimed_item"));
                        player.closeInventory();
                    }
                });
            } else {
                gui.set(new GUIItem(31) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        return ItemStackCreator.getStack(I18n.string("gui_auction.view_self_bin.cannot_buy_own"), Material.BEDROCK, 1,
                                I18n.lore("gui_auction.view_self_bin.cannot_buy_own_expired.lore"));
                    }
                });
            }
            return;
        }

        gui.set(new GUIItem(31) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_auction.view_self_bin.cannot_buy_own"), Material.BEDROCK, 1,
                        I18n.lore("gui_auction.view_self_bin.cannot_buy_own_active.lore"));
            }
        });
    }
}
