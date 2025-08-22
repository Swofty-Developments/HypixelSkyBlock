package net.swofty.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.skyblockgeneric.event.actions.player.ActionPlayerChangeHypixelMenuDisplay;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerInventoryClose implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(InventoryCloseEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        ActionPlayerChangeHypixelMenuDisplay.runCheck(player);

        if (HypixelInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            HypixelInventoryGUI gui = net.swofty.type.generic.gui.inventory.HypixelInventoryGUI.GUI_MAP.get(player.getUuid());

            if (gui == null) return;

            gui.onClose(event, net.swofty.type.generic.gui.inventory.HypixelInventoryGUI.CloseReason.PLAYER_EXITED);
            net.swofty.type.generic.gui.inventory.HypixelInventoryGUI.GUI_MAP.remove(player.getUuid());
        }
    }
}

