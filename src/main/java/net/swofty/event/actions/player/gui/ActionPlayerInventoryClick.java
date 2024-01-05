package net.swofty.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.ClickType;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.gui.SkyBlockSignGUI;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIItem;
import net.swofty.gui.inventory.item.GUIQueryItem;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.Interactable;
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
        SkyBlockItem clickedItem = new SkyBlockItem(inventoryClick.getClickedItem());
        SkyBlockItem cursorItem = new SkyBlockItem(inventoryClick.getCursorItem());

        if (clickedItem.getGenericInstance() != null &&
                clickedItem.getGenericInstance() instanceof Interactable interactable) {
            if (interactable.onInventoryInteract(player, clickedItem)) {
                inventoryClick.setCancelled(true);
                return;
            }
        }

        if (cursorItem.getAttributeHandler().getItemTypeAsType() != null &&
                cursorItem.getAttributeHandler().getItemTypeAsType().equals(ItemType.SKYBLOCK_MENU)) {
            inventoryClick.setCancelled(true);
            return;
        }

        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui == null) return;

            if (inventoryClick.getClickType().equals(ClickType.DOUBLE_CLICK)) {
                inventoryClick.setCancelled(true);
                return;
            }

            if (inventoryClick.getInventory() == null) {
                if (!gui.allowHotkeying() && isHotKey(inventoryClick)) {
                    inventoryClick.setCancelled(true);
                    return;
                }
                gui.onBottomClick(inventoryClick);
            } else {
                int slot = inventoryClick.getSlot();
                GUIItem item = gui.get(slot);

                if (item == null) return;

                if (!item.canPickup()) {
                    inventoryClick.setCancelled(true);
                } else if (!gui.allowHotkeying() && isHotKey(inventoryClick)) {
                    inventoryClick.setCancelled(true);
                    return;
                }

                if (item instanceof GUIClickableItem clickable) {
                    clickable.run(inventoryClick, player);
                }

                if (item instanceof GUIQueryItem query) {

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

    public boolean isHotKey(InventoryPreClickEvent inventoryClick) {
        return inventoryClick.getClickType().equals(ClickType.LEFT_DRAGGING) ||
                inventoryClick.getClickType().equals(ClickType.SHIFT_CLICK) ||
                inventoryClick.getClickType().equals(ClickType.START_SHIFT_CLICK) ||
                inventoryClick.getClickType().equals(ClickType.RIGHT_DRAGGING);
    }
}

