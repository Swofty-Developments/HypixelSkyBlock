package net.swofty.type.island.events.traditional;

import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.island.gui.GUIMinion;
import net.swofty.types.generic.entity.MinionEntityImpl;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Handles when a minion is clicked",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerClickMinion extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerEntityInteractEvent event = (PlayerEntityInteractEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof MinionEntityImpl minion) {
            new GUIMinion(minion.getIslandMinion()).open(player);
        }
    }
}
