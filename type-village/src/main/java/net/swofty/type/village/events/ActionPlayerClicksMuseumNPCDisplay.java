package net.swofty.type.village.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.gui.inventory.inventories.museum.GUIYourMuseum;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Player clicks on a museum display block",
        node = EventNodes.PLAYER,
        requireDataLoaded = true,
        isAsync = true)
public class ActionPlayerClicksMuseumNPCDisplay extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerBlockInteractEvent event = (PlayerBlockInteractEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        Pos displayPosition = new Pos(-23, 67, 80);

        if (event.getBlockPosition().distance(displayPosition) > 2) return;

        new GUIYourMuseum().open(player);
    }
}
