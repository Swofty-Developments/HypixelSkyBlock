package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.gui.inventory.inventories.GUIAnvil;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Handles clicking on the Anvil",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionAnvilClick extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerBlockInteractEvent event = (PlayerBlockInteractEvent) tempEvent;

        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!event.getBlock().name().equals("minecraft:anvil")) return;

        new GUIAnvil().open(player);
    }
}
