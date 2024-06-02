package net.swofty.types.generic.gui.inventory.inventories.auction.view;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointUUIDList;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionViewItem;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;
import java.util.UUID;

public class AuctionViewSelfBIN implements AuctionView {
    @Override
    public void open(GUIAuctionViewItem gui, AuctionItem item, SkyBlockPlayer player) {
        if (!item.getBids().isEmpty()) {
            List<UUID> ownedActive = player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
            List<UUID> ownedInactive = player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

            if (ownedActive.contains(item.getUuid())) {
                gui.set(new GUIClickableItem(31) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        double coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();

                        player.sendMessage("§8Claiming your coins...");
                        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(coins + item.getBids().getFirst().value());

                        ownedActive.remove(item.getUuid());
                        player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                        ownedInactive.add(item.getUuid());
                        player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                        player.sendMessage("§eYou collected §6" + item.getBids().getFirst().value() + " coins §efrom the auction!");
                        player.closeInventory();
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§6Collect Auction", Material.GOLD_BLOCK, 1,
                                " ",
                                "§7This item has been sold!",
                                " ",
                                SkyBlockPlayer.getDisplayName(item.getBids().getFirst().uuid()) + " §7bought it for §6" + item.getBids().getFirst().value() + " coins",
                                " ",
                                "§eClick to collect coins!");
                    }
                });
            } else {
                gui.set(new GUIItem(31) {
                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§cYou Cannot Buy Your Own Item", Material.BEDROCK, 1,
                                " ",
                                "§7You cannot buy your own item!",
                                " ",
                                "§cYou have already claimed your item!");
                    }
                });
            }
            return;
        }

        if (item.getEndTime() < System.currentTimeMillis()) {
            // Noone bought the item, so give it back to the player if in active
            List<UUID> ownedActive = player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).getValue();
            List<UUID> ownedInactive = player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).getValue();

            if (ownedActive.contains(item.getUuid())) {
                gui.set(new GUIClickableItem(31) {
                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§eClaim Item Back", Material.GOLD_INGOT, 1,
                                " ",
                                "§7This auction has ended!",
                                "§7Nobody bought it :(",
                                " ",
                                "§eClick to claim it back!");
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        player.sendMessage("§8Claiming your item...");
                        ownedActive.remove(item.getUuid());
                        player.getDataHandler().get(DataHandler.Data.AUCTION_ACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedActive);
                        ownedInactive.add(item.getUuid());
                        player.getDataHandler().get(DataHandler.Data.AUCTION_INACTIVE_OWNED, DatapointUUIDList.class).setValue(ownedInactive);

                        player.addAndUpdateItem(item.getItem());

                        player.sendMessage("§aYou have claimed your item.");
                        player.closeInventory();
                    }
                });
            } else {
                gui.set(new GUIItem(31) {
                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack("§cYou Cannot Buy Your Own Item", Material.BEDROCK, 1,
                                " ",
                                "§7You cannot buy your own item!",
                                " ",
                                "§cThis item has expired and has been returned to you.");
                    }
                });
            }
            return;
        }

        gui.set(new GUIItem(31) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§cYou Cannot Buy Your Own Item", Material.BEDROCK, 1,
                        " ",
                        "§7You cannot buy your own item!",
                        " ",
                        "§7You can only buy items from other players.");
            }
        });
    }
}
