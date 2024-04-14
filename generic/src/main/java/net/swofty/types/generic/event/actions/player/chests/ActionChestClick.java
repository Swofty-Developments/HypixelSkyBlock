package net.swofty.types.generic.event.actions.player.chests;

import net.minestom.server.coordinate.Point;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.gui.inventory.inventories.GUIChest;
import net.swofty.types.generic.item.ChestImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Handles clicking on the Chest",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionChestClick extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerBlockInteractEvent event = (PlayerBlockInteractEvent) tempEvent;

        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!SkyBlockConst.isIslandServer()) return;
        if (!event.getBlock().name().equals("minecraft:chest")) return;

        Instance instance = event.getInstance();
        Point position = event.getBlockPosition();

        ChestImpl chest = new ChestImpl(instance, position);
        new GUIChest(chest).open(player);
    }
}
