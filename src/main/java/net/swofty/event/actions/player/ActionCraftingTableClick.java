package net.swofty.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.Material;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.gui.inventory.inventories.sbmenu.GUICrafting;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Handles clicking on the crafting table",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
public class ActionCraftingTableClick extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockInteractEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerBlockInteractEvent interactEvent = (PlayerBlockInteractEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) interactEvent.getPlayer();

        if (Material.fromNamespaceId(interactEvent.getBlock().namespace()) != Material.CRAFTING_TABLE) {
            return;
        }

        new GUICrafting().open(player);
    }
}

