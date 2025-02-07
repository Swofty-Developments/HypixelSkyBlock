package net.swofty.types.generic.event.actions.player.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.RefreshAction;
import net.swofty.types.generic.gui.inventory.actions.RefreshSlotAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.InteractableComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

public class ActionPlayerInventoryClick implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(InventoryPreClickEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockItem clickedItem = new SkyBlockItem(event.getClickedItem());
        SkyBlockItem cursorItem = new SkyBlockItem(event.getCursorItem());

        // Check for offhand
        if (event.getSlot() == 45 && event.getInventory() == null) {
            event.setCancelled(true);
            return;
        }

        if (clickedItem.hasComponent(InteractableComponent.class)) {
            InteractableComponent interactable = clickedItem.getComponent(InteractableComponent.class);
            if (interactable.onInventoryInteract(player, clickedItem)) {
                event.setCancelled(true);
                return;
            }
        }

        if (player.getOpenInventory() == null && event.getSlot() == 8) {
            event.setCancelled(true);
            return;
        }

        Component displayNameCursor = event.getCursorItem().get(ItemComponent.CUSTOM_NAME);
        Component displayNameClicked = event.getClickedItem().get(ItemComponent.CUSTOM_NAME);
        if ((displayNameCursor != null && StringUtility.getTextFromComponent(displayNameCursor).contains("Switch your held"))
                || (displayNameClicked != null && StringUtility.getTextFromComponent(displayNameClicked).contains("Switch your held"))) {
            event.setCancelled(true);
            return;
        }

        if (SkyBlockAbstractInventory.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockAbstractInventory gui = SkyBlockAbstractInventory.GUI_MAP.get(player.getUuid());
            if (gui == null) return;

            if (event.getClickType().equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
                return;
            }

            if (event.getInventory() == null) {
                if (!gui.allowHotkeying() && isHotKey(event)) {
                    event.setCancelled(true);
                    return;
                }
                gui.onBottomClick(event);
            } else {
                int slot = event.getSlot();
                GUIItem visibleItem = RefreshSlotAction.getItem(gui, slot);

                if (visibleItem == null) return;

                if (!gui.allowHotkeying() && isHotKey(event)) {
                    event.setCancelled(true);
                    return;
                }

                GUIItem.ClickContext context = new GUIItem.ClickContext(
                        event.getCursorItem(),
                        player,
                        event.getClickType()
                );

                boolean canPickup = visibleItem.handleClickAndShouldAllow(context, event.getClickedItem());
                if (!canPickup) {
                    event.setCancelled(true);
                } else {
                    // Check that the same GUI is still open
                    if (player.getOpenInventory() != event.getInventory()) {
                        event.setCancelled(true);
                    }
                }
                new RefreshAction().execute(gui);

                if (!cursorItem.isNA() && player.getOpenInventory() != event.getInventory()
                        && player.getOpenInventory() != null && event.getClickType() != ClickType.CHANGE_HELD) {
                    player.addAndUpdateItem(cursorItem);
                }
            }
        }
    }

    public boolean isHotKey(InventoryPreClickEvent inventoryClick) {
        return inventoryClick.getClickType().equals(ClickType.LEFT_DRAGGING) ||
                inventoryClick.getClickType().equals(ClickType.SHIFT_CLICK) ||
                inventoryClick.getClickType().equals(ClickType.START_SHIFT_CLICK) ||
                inventoryClick.getClickType().equals(ClickType.CHANGE_HELD) ||
                inventoryClick.getClickType().equals(ClickType.RIGHT_DRAGGING);
    }
}