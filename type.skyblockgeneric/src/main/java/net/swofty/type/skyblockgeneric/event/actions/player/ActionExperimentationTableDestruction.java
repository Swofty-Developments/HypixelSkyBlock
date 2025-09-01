package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.skyblockgeneric.block.blocks.BlockExperimentationTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public class ActionExperimentationTableDestruction implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ENTITY, requireDataLoaded = false)
    public void run(EntityAttackEvent event) {
        if (!(event.getEntity() instanceof SkyBlockPlayer player)) return;
        
        Entity attackedEntity = event.getTarget();
        
        // Check if the attacked entity is part of an Experimentation Table structure
        String masterUuidString = attackedEntity.getTag(Tag.String("master_uuid"));
        if (masterUuidString == null) return;
        
        try {
            UUID masterUuid = UUID.fromString(masterUuidString);
            
            // Remove the entire structure
            BlockExperimentationTable.removeStructure(masterUuid);
            
            // Optionally drop an Experimentation Table item back
            if (!player.getGameMode().equals(net.minestom.server.entity.GameMode.CREATIVE)) {
                // You can implement item dropping here if desired
                // For now, just send a message
                player.sendMessage("§aExperimentation Table removed!");
            }
            
        } catch (IllegalArgumentException e) {
            // Invalid UUID format, ignore
        }
    }
}
