package net.swofty.event.actions.player.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.network.packet.server.play.WindowPropertyPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.gui.SkyBlockAnvilGUI;
import net.swofty.gui.SkyBlockSignGUI;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIItem;
import net.swofty.gui.inventory.item.GUIQueryItem;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Handles when a player clicks on an InventoryGUI",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
public class ActionPlayerInventoryClick extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return InventoryPreClickEvent.class;
    }

    @Override
    public void run(Event event) {
        InventoryPreClickEvent inventoryClick = (InventoryPreClickEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) inventoryClick.getPlayer();

        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui == null) return;

            if (!gui.allowHotkeying() &&
                    (inventoryClick.getClickType().equals(ClickType.LEFT_DRAGGING) ||
                     inventoryClick.getClickType().equals(ClickType.SHIFT_CLICK)   ||
                     inventoryClick.getClickType().equals(ClickType.RIGHT_DRAGGING))) {
                inventoryClick.setCancelled(true);
                return;
            }

            if (inventoryClick.getClickType().equals(ClickType.DOUBLE_CLICK)) {
                inventoryClick.setCancelled(true);
                return;
            }

            if (inventoryClick.getInventory() == null) {
                gui.onBottomClick(inventoryClick);
            } else {
                int slot = inventoryClick.getSlot();
                GUIItem item = gui.get(slot);

                if (item == null) return;

                if (!item.canPickup()) {
                    inventoryClick.setCancelled(true);
                }

                if (item instanceof GUIClickableItem) {
                    GUIClickableItem clickable = (GUIClickableItem) item;
                    clickable.run(inventoryClick, player);
                }

                if (item instanceof GUIQueryItem) {
                    GUIQueryItem query = (GUIQueryItem) item;

                    gui.onClose(null, SkyBlockInventoryGUI.CloseReason.SIGN_OPENED);

                    new SkyBlockSignGUI(player).open(query.lines()).thenAccept(string -> {
                        SkyBlockInventoryGUI nextGui = query.onQueryFinish(string, player);
                        if (nextGui != null && string != null)
                            nextGui.open(player);
                    });
                }
            }
        }
    }
}

