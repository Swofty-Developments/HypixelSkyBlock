package net.swofty.types.generic.event.actions.player;


import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.gui.inventory.inventories.GUIViewPlayerProfile;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Checks to see if a player clicks on a player and opens the viewPlayerProfileGui",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerRightClickOnPlayer extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerEntityInteractEvent playerEvent = (PlayerEntityInteractEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerEvent.getPlayer();

        if (playerEvent.getTarget() instanceof SkyBlockPlayer skyBlockPlayer) {
            new GUIViewPlayerProfile(skyBlockPlayer).open(player);
        }
    }
}
