package net.swofty.type.skyblockgeneric.gui.inventories.auction.view;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUIDList;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;
import java.util.Locale;
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
                        Locale l = p.getLocale();
                        double coins = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();

                        player.sendMessage(I18n.t("gui_auction.view_self_bin.claiming_coins"));
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getBids().getFirst().value());

                        ownedActive.remove(item.getUuid());
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                        ownedInactive.add(item.getUuid());
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                        player.sendMessage(I18n.t("gui_auction.view_self_bin.collected_coins", Component.text(String.valueOf(item.getBids().getFirst().value()))));
                        player.closeInventory();
                    }

                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        return TranslatableItemStackCreator.getStack(
                            "gui_auction.view_self_bin.collect_auction",
                            Material.GOLD_BLOCK,
                            1,
                            "gui_auction.view_self_bin.collect_sold.lore",
                            Component.text(SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid())),
                            Component.text(String.valueOf(item.getBids().getFirst().value()))
                        );
                    }
                });
            } else {
                gui.set(new GUIItem(31) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        return TranslatableItemStackCreator.getStack("gui_auction.view_self_bin.cannot_buy_own", Material.BEDROCK, 1,
                            "gui_auction.view_self_bin.cannot_buy_own_claimed.lore");
                    }
                });
            }
            return;
        }

        if (item.getEndTime() < System.currentTimeMillis()) {
            List<UUID> ownedActive = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
            List<UUID> ownedInactive = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

            if (ownedActive.contains(item.getUuid())) {
                gui.set(new GUIClickableItem(31) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        return TranslatableItemStackCreator.getStack("gui_auction.view_self_bin.claim_item_back", Material.GOLD_INGOT, 1,
                            "gui_auction.view_self_bin.claim_item_back.lore");
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        player.sendMessage(I18n.t("gui_auction.view_self_bin.claiming_item"));
                        ownedActive.remove(item.getUuid());
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                        ownedInactive.add(item.getUuid());
                        player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                        player.addAndUpdateItem(item.getItem());

                        player.sendMessage(I18n.t("gui_auction.view_self_bin.claimed_item"));
                        player.closeInventory();
                    }
                });
            } else {
                gui.set(new GUIItem(31) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        return TranslatableItemStackCreator.getStack("gui_auction.view_self_bin.cannot_buy_own", Material.BEDROCK, 1,
                            "gui_auction.view_self_bin.cannot_buy_own_expired.lore");
                    }
                });
            }
            return;
        }

        gui.set(new GUIItem(31) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return TranslatableItemStackCreator.getStack("gui_auction.view_self_bin.cannot_buy_own", Material.BEDROCK, 1,
                    "gui_auction.view_self_bin.cannot_buy_own_active.lore");
            }
        });
    }
}
