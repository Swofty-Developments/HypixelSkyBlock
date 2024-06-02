package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.Material;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUICrafting;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionCraftingTableClick implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerBlockInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (Material.fromNamespaceId(event.getBlock().namespace()) != Material.CRAFTING_TABLE) {
            return;
        }
        if (!SkyBlockConst.isIslandServer()) return;

        event.setBlockingItemUse(true);

        new GUICrafting().open(player);
    }
}

