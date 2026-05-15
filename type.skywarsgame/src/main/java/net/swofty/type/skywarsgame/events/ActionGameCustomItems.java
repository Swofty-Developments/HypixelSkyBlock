package net.swofty.type.skywarsgame.events;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemHandler;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class ActionGameCustomItems implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerUseItemOnBlockEvent event) {
        TypeSkywarsGameLoader.getItemHandler().onItemUseOnBlock(event);

        if (event.getPlayer() instanceof SkywarsPlayer player) {
            LuckyBlockItemHandler.processItemUse(player, event.getItemStack());
        }
    }

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerUseItemEvent event) {
        TypeSkywarsGameLoader.getItemHandler().onItemUse(event);

        if (event.getPlayer() instanceof SkywarsPlayer player) {
            LuckyBlockItemHandler.processItemUse(player, event.getItemStack());
        }
    }

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBlockPlaceEvent event) {
        TypeSkywarsGameLoader.getItemHandler().onBlockPlace(event);
    }
}
