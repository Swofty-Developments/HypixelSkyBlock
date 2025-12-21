package net.swofty.type.hub.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.data.datapoints.DatapointInteger;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.CalendarEvent;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.NewYearCakeComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;


public class GUIClaimCake extends HypixelInventoryGUI {
    public GUIClaimCake() {
        super("Claim Reward", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                List<CalendarEvent> events = SkyBlockCalendar.getCurrentEvents();
                if (!events.contains(CalendarEvent.NEW_YEAR)) {
                    p.closeInventory();
                    return;
                }
                final SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();
                if (dataHandler.get(SkyBlockDataHandler.Data.LATEST_NEW_YEAR_CAKE_YEAR, DatapointInteger.class).getValue() >= SkyBlockCalendar.getYear()) {
                    p.closeInventory();
                    return;
                }
                dataHandler.get(SkyBlockDataHandler.Data.LATEST_NEW_YEAR_CAKE_YEAR, DatapointInteger.class).setValue(SkyBlockCalendar.getYear());

                SkyBlockItem item = new SkyBlockItem(ItemType.NEW_YEAR_CAKE);
                item.getConfig().addComponent(new NewYearCakeComponent(SkyBlockCalendar.getYear()), false);
                player.addAndUpdateItem(item);
                p.closeInventory();
                player.sendMessage("§aYou claimed §cNew Year Cake§a!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§cNew Year Cake (Year " + SkyBlockCalendar.getYear() + ")", Material.CAKE, 1, List.of(
                        "§7Given to every player as a",
                        "§7celebration for the " + StringUtility.ntify(SkyBlockCalendar.getYear()) + " Skyblock",
                        "§7year!",
                        " ",
                        "§c§lSPECIAL",
                        " ",
                        "§eClick to claim!"
                ));
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}

