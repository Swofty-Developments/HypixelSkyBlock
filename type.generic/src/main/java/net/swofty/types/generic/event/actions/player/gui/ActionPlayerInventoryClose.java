package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.actions.player.ActionPlayerChangeSkyBlockMenuDisplay;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerInventoryClose implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(InventoryCloseEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        ActionPlayerChangeSkyBlockMenuDisplay.runCheck(player);

        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui == null) return;

            gui.onClose(event, SkyBlockInventoryGUI.CloseReason.PLAYER_EXITED);
            SkyBlockInventoryGUI.GUI_MAP.remove(player.getUuid());
        }
    }
}

