package net.swofty.type.skyblockgeneric.gui.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointAuctionEscrow;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.concurrent.atomic.AtomicBoolean;

public class GUIAuctionDuration extends HypixelInventoryGUI {
    public GUIAuctionDuration() {
        super("Auction Duration", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getGoBackItem(31, new GUIAuctionCreateItem(new GUIAuctionHouse())));
        set(createTime(Material.RED_TERRACOTTA, 1, 10, (SkyBlockPlayer) getPlayer()));
        set(createTime(Material.PINK_TERRACOTTA, 6, 11, (SkyBlockPlayer) getPlayer()));
        set(createTime(Material.ORANGE_TERRACOTTA, 12, 12, (SkyBlockPlayer) getPlayer()));
        set(createTime(Material.YELLOW_TERRACOTTA, 24, 13, (SkyBlockPlayer) getPlayer()));
        set(createTime(Material.WHITE_TERRACOTTA, 48, 14, (SkyBlockPlayer) getPlayer()));

        AtomicBoolean right = new AtomicBoolean();
        set(new GUIQueryItem(16) {
            @Override
            public HypixelInventoryGUI onQueryFinish(String query, HypixelPlayer player) {
                long l;
                try {
                    l = Long.parseLong(query);
                } catch (NumberFormatException ex) {
                    player.sendMessage("§cCould not read this number!");
                    return null;
                }
                if (l <= 1) {
                    player.sendMessage("§cInvalid time amount!");
                    return null;
                }
                if (l >= 336 && !right.get()) {
                    player.sendMessage("§cYou can only put an auction up to a maximum of 14 days!");
                    return null;
                }

                ((SkyBlockPlayer) player).getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().setDuration(
                        l * (right.get() ? 60000 : 3600000)
                );

                return new GUIAuctionCreateItem(GUIAuctionDuration.this);
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                right.set(e.getClickType().equals(ClickType.RIGHT_CLICK));
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aCustom Duration", Material.COMPASS, 1,
                        "§7Specify how long you want",
                        "§7the auction to last.",
                        " ",
                        "§bRight-click for minutes!",
                        "§eLeft-click to set hours!");
            }
        });

        updateItemStacks(getInventory(), getPlayer());
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

    private static GUIClickableItem createTime(Material color, int hours, int slot, SkyBlockPlayer user) {
        long millis = hours * 3600000L;
        return new GUIClickableItem(slot) {

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStack.Builder stack = ItemStackCreator.getStack("§a" + hours + " Hours", color, 1,
                        " ",
                        "§eClick to set this duration!");

                if (user.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().getDuration() == millis) {
                    stack = ItemStackCreator.enchant(stack);
                }

                return stack;
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                user.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().setDuration(millis);
                new GUIAuctionDuration().open(player);
            }
        };
    }
}
