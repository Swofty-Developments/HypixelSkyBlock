package net.swofty.type.skyblockgeneric.gui.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointAuctionEscrow;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Locale;
import java.util.Map;

import java.util.concurrent.atomic.AtomicBoolean;

public class GUIAuctionDuration extends HypixelInventoryGUI {
    public GUIAuctionDuration() {
        super(I18n.string("gui_auction.duration.title"), InventoryType.CHEST_4_ROW);
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
                Locale loc = player.getLocale();
                long val;
                try {
                    val = Long.parseLong(query);
                } catch (NumberFormatException ex) {
                    player.sendMessage(I18n.string("gui_auction.duration.number_parse_error", loc));
                    return null;
                }
                if (val <= 1) {
                    player.sendMessage(I18n.string("gui_auction.duration.invalid_time", loc));
                    return null;
                }
                if (val >= 336 && !right.get()) {
                    player.sendMessage(I18n.string("gui_auction.duration.max_exceeded", loc));
                    return null;
                }

                ((SkyBlockPlayer) player).getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().setDuration(
                        val * (right.get() ? 60000 : 3600000)
                );

                return new GUIAuctionCreateItem(GUIAuctionDuration.this);
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                right.set(e.getClick() instanceof Click.Right);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return TranslatableItemStackCreator.getStack(p, "gui_auction.duration.custom", Material.COMPASS, 1,
                        "gui_auction.duration.custom.lore");
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
                Locale l = p.getLocale();
                ItemStack.Builder stack = ItemStackCreator.getStack(
                        I18n.string("gui_auction.duration.hours", l, Map.of("hours", String.valueOf(hours))),
                        color, 1,
                        " ",
                        I18n.string("gui_auction.duration.hours_click", l));

                if (user.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().getDuration() == millis) {
                    stack = ItemStackCreator.enchant(stack);
                }

                return stack;
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                user.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue().setDuration(millis);
                new GUIAuctionDuration().open(player);
            }
        };
    }
}
