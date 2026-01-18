package net.swofty.type.generic.gui.v2.event;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.v2.ViewNavigator;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionInventoryPreClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.INVENTORY, requireDataLoaded = false)
    public void onActionPlayerInventoryPreClick(InventoryPreClickEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        ViewNavigator.find(player).ifPresent(navigator -> {
            navigator.getCurrentSession().onPreClickEvent(event);
        });
    }

}
