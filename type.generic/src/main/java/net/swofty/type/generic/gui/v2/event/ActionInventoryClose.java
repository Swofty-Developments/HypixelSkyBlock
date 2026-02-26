package net.swofty.type.generic.gui.v2.event;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.v2.ViewNavigator;
import net.swofty.type.generic.gui.v2.ViewSession;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionInventoryClose implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.INVENTORY, requireDataLoaded = false)
    public void onPlayerInventoryClose(InventoryCloseEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        ViewNavigator.find(player).ifPresent(navigator -> {
            ViewSession<?> session = navigator.getCurrentSession();
            if (event.getInventory() != session.inventory() || session.isSuppressCloseEvent()) {
                session.setSuppressCloseEvent(false);
                return;
            }
            session.close(ViewSession.CloseReason.PLAYER_EXITED);
        });
    }

}
