package net.swofty.type.skyblockgeneric.event.actions.player.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.inventory.click.ClickType;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.InteractableComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerInventoryClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(InventoryPreClickEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockItem clickedItem = new SkyBlockItem(event.getClickedItem());
        SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());

        // Check for offhand
        if (event.getSlot() == 45 && event.getInventory() instanceof PlayerInventory) {
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

        // Check for SkyBlock menu
        if (event.getInventory() instanceof PlayerInventory && event.getSlot() == 8) {
            event.setCancelled(true);
            return;
        }

        Component displayNameCursor = player.getInventory().getCursorItem().get(DataComponents.CUSTOM_NAME);
        Component displayNameClicked = event.getClickedItem().get(DataComponents.CUSTOM_NAME);
        if ((displayNameCursor != null && StringUtility.getTextFromComponent(displayNameCursor).contains("Switch your held"))
        || (displayNameClicked != null && StringUtility.getTextFromComponent(displayNameClicked).contains("Switch your held"))) {
            event.setCancelled(true);
            return;
        }

        if (HypixelInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            HypixelInventoryGUI gui = net.swofty.type.generic.gui.inventory.HypixelInventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui == null) return;

            if (event.getClick() instanceof Click.Double) {
                event.setCancelled(true);
                return;
            }

            if (!gui.allowHotkeying() && isHotKey(event)) {
                event.setCancelled(true);
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
                    if (!cursorItem.isNA() && player.getOpenInventory() != event.getInventory()
                            && player.getOpenInventory() != null && event.getClick() instanceof Click.HotbarSwap) {
                        player.addAndUpdateItem(cursorItem);
                    }
                }

                if (item instanceof GUIQueryItem query) {
                    gui.onClose(new InventoryCloseEvent(
                            player.getOpenInventory(),
                            player,
                            false
                    ), net.swofty.type.generic.gui.inventory.HypixelInventoryGUI.CloseReason.SIGN_OPENED);

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
        return inventoryClick.getClick() instanceof Click.Drag ||
                inventoryClick.getClick() instanceof Click.HotbarSwap ||
                inventoryClick.getClick() instanceof Click.LeftShift ||
                inventoryClick.getClick() instanceof Click.RightShift;
    }
}

