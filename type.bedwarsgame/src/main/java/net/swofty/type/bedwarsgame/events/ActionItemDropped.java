package net.swofty.type.bedwarsgame.events;

import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.entity.EntitySpawnEvent;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionItemDropped implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false, isAsync = false)
    public void run(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            BedWarsGame game = TypeBedWarsGameLoader.getGameByInstance(event.getInstance());
            if (game == null) return;

            if (!game.getReplayManager().isRecording()) return;

            game.getReplayManager().recordDroppedItem(itemEntity);
        }
    }

}
