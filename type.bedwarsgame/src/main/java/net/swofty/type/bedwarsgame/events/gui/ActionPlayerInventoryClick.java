package net.swofty.type.bedwarsgame.events.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerInventoryClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(InventoryPreClickEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        // Check for offhand
        if (event.getSlot() == 45 && event.getInventory() == null) {
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

        if (HypixelInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            HypixelInventoryGUI gui = HypixelInventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui == null) return;

            if (event.getClickType().equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
                return;
            }

            if (event.getInventory() == null) {
                if (!gui.allowHotkeying() && isHotKey(event)) {
                    event.setCancelled(true);
                }
                return;
            }
            if (event.getInventory() instanceof PlayerInventory){
              gui.onBottomClick(event);
            } else {
                int slot = event.getSlot();
                GUIItem item = gui.get(slot);

                if (item == null) return;

                if (!item.canPickup()) {
                    event.setCancelled(true);
                } else if (!gui.allowHotkeying() && isHotKey(event)) {
                    event.setCancelled(true);
                    return;
                }

                if (item instanceof GUIClickableItem clickable) {
                    clickable.run(event, player);
                    if (!(event.getCursorItem().material() == Material.AIR) && player.getOpenInventory() != event.getInventory()
                            && player.getOpenInventory() != null && event.getClickType() != ClickType.CHANGE_HELD) {
                        player.getInventory().addItemStack(event.getCursorItem()); // check
                    }
                }

                if (item instanceof GUIQueryItem query) {
                    gui.onClose(new InventoryCloseEvent(
                            player.getOpenInventory(),
                            player,
                            false
                    ), HypixelInventoryGUI.CloseReason.SIGN_OPENED);

                    new HypixelSignGUI(player).open(query.lines()).thenAccept(string -> {
                        HypixelInventoryGUI nextGui = query.onQueryFinish(string, player);
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
                inventoryClick.getClickType().equals(ClickType.CHANGE_HELD) ||
                inventoryClick.getClickType().equals(ClickType.RIGHT_DRAGGING);
    }
}

