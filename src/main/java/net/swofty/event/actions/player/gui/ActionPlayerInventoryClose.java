package net.swofty.event.actions.player.gui;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.gui.SkyBlockAnvilGUI;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Handles when a player closes an InventoryGUI",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
public class ActionPlayerInventoryClose extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return InventoryCloseEvent.class;
    }

    @Override
    public void run(Event event) {
        InventoryCloseEvent inventoryClose = (InventoryCloseEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) inventoryClose.getPlayer();
        player.updateCursor();

        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui == null) return;

            gui.onClose(inventoryClose, SkyBlockInventoryGUI.CloseReason.PLAYER_EXITED);
            SkyBlockInventoryGUI.GUI_MAP.remove(player.getUuid());
        }
    }
}

