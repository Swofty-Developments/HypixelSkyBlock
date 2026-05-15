package net.swofty.type.murdermysterygame.events;

import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class ActionGameCustomItems implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerUseItemOnBlockEvent event) {
        TypeMurderMysteryGameLoader.getItemHandler().onItemUseOnBlock(event);
    }

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerFinishItemUseEvent event) {
        TypeMurderMysteryGameLoader.getItemHandler().onItemFinishUse(event);
    }

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerUseItemEvent event) {
        TypeMurderMysteryGameLoader.getItemHandler().onItemUse(event);
    }

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBlockPlaceEvent event) {
        TypeMurderMysteryGameLoader.getItemHandler().onBlockPlace(event);
    }
}
