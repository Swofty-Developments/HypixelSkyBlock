package net.swofty.type.murdermysterygame.events;

import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionGameCustomItems implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerUseItemOnBlockEvent event) {
        TypeMurderMysteryGameLoader.getItemHandler().onItemUseOnBlock(event);
    }

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerFinishItemUseEvent event) {
        TypeMurderMysteryGameLoader.getItemHandler().onItemFinishUse(event);
    }

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerUseItemEvent event) {
        TypeMurderMysteryGameLoader.getItemHandler().onItemUse(event);
    }

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerBlockPlaceEvent event) {
        TypeMurderMysteryGameLoader.getItemHandler().onBlockPlace(event);
    }
}
