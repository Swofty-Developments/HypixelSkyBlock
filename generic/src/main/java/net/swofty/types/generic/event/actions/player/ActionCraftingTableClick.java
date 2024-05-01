package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.Material;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUICrafting;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

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
        if (!SkyBlockConst.isIslandServer()) return;

        interactEvent.setBlockingItemUse(true);

        new GUICrafting().open(player);
    }
}

