package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.tag.Tag;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.block.blocks.BlockExperimentationTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public final class ActionExperimentationTableEntityDestroy implements HypixelEventClass {
    private static final Tag<String> TABLE_ID_TAG = Tag.String("experimentation_table");

    @PhasedEvent(node = EventNodes.ENTITY, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(EntityAttackEvent event) {
        if (!(event.getEntity() instanceof SkyBlockPlayer player)) return;

        String tableId = event.getTarget().getTag(TABLE_ID_TAG);
        if (tableId != null) {
            BlockExperimentationTable.destroyFromPart(player, tableId);
        }
    }
}
