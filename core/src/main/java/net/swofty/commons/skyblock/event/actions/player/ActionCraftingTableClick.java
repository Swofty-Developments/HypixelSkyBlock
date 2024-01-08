package net.swofty.commons.skyblock.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.gui.inventory.inventories.sbmenu.GUICrafting;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;

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
        ((PlayerBlockInteractEvent) event).setBlockingItemUse(true);

        new GUICrafting().open(player);
    }
}

