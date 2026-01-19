package net.swofty.type.generic.gui.v2.event;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.v2.ViewNavigator;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionInventoryPostClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.INVENTORY, requireDataLoaded = false)
    public void onInventoryPostClick(InventoryClickEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        ViewNavigator.find(player).ifPresent(navigator -> navigator.getCurrentSession().onPostClickEvent(event));
    }

}
