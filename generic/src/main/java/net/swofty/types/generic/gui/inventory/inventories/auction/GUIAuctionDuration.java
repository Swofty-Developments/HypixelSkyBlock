package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointAuctionEscrow;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.concurrent.atomic.AtomicBoolean;

public class GUIAuctionDuration extends SkyBlockInventoryGUI {
    public GUIAuctionDuration() {
        super("Auction Duration", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getGoBackItem(31, new GUIAuctionCreateItem(new GUIAuctionHouse())));
        set(createTime(Material.RED_TERRACOTTA, 1, 10, getPlayer()));
        set(createTime(Material.PINK_TERRACOTTA, 6, 11, getPlayer()));
        set(createTime(Material.ORANGE_TERRACOTTA, 12, 12, getPlayer()));
        set(createTime(Material.YELLOW_TERRACOTTA, 24, 13, getPlayer()));
        set(createTime(Material.WHITE_TERRACOTTA, 48, 14, getPlayer()));

        AtomicBoolean right = new AtomicBoolean();
        set(new GUIQueryItem(16) {
            @Override
            public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
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

                player.getDataHandler().get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().setDuration(
                        l * (right.get() ? 60000 : 3600000)
                );

                return new GUIAuctionCreateItem(GUIAuctionDuration.this);
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                right.set(e.getClickType().equals(ClickType.RIGHT_CLICK));
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    private static GUIClickableItem createTime(Material color, int hours, int slot, SkyBlockPlayer user) {
        long millis = hours * 3600000L;
        return new GUIClickableItem(slot) {

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                ItemStack.Builder stack = ItemStackCreator.getStack("§a" + hours + " Hours", color, 1,
                        " ",
                        "§eClick to set this duration!");

                if (user.getDataHandler().get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().getDuration() == millis) {
                    stack = ItemStackCreator.enchant(stack);
                }

                return stack;
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                user.getDataHandler().get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().setDuration(millis);
                new GUIAuctionDuration().open(player);
            }
        };
    }
}
