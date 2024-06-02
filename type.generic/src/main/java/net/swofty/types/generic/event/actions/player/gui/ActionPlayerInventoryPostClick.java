package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.actions.player.ActionPlayerChangeSkyBlockMenuDisplay;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerInventoryPostClick implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(InventoryClickEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (player.getOpenInventory() == null)
            ActionPlayerChangeSkyBlockMenuDisplay.runCheck(player);

        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockInventoryGUI gui = SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui == null) return;

            if (event.getInventory() != null) {
                int slot = event.getSlot();
                GUIItem item = gui.get(slot);

                if (item == null) return;

                if (item instanceof GUIClickableItem clickable) {
                    clickable.runPost(event, player);
                }
            }
        }
    }
}

