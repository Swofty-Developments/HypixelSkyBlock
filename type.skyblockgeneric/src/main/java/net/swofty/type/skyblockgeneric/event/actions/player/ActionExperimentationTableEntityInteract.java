package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.tag.Tag;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.block.blocks.BlockExperimentationTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public final class ActionExperimentationTableEntityInteract implements HypixelEventClass {
    private static final Tag<String> TABLE_ID_TAG = Tag.String("experimentation_table");

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerEntityInteractEvent event) {
        if (event.getHand() != PlayerHand.MAIN || !(event.getPlayer() instanceof SkyBlockPlayer player)) return;

        String tableId = event.getTarget().getTag(TABLE_ID_TAG);
        if (tableId != null) {
            BlockExperimentationTable.interactWithPart(player, tableId);
        }
    }
}
