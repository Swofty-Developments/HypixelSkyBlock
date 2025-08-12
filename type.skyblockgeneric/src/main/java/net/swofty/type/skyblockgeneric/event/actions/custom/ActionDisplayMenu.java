package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.skyblockgeneric.event.actions.player.ActionPlayerChangeHypixelMenuDisplay;
import SkyBlockPlayer;

public class ActionDisplayMenu implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(HypixelInventoryGUI.InventoryGUIOpenEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.player();
        ActionPlayerChangeHypixelMenuDisplay.setMainMenu(player);
    }
}
