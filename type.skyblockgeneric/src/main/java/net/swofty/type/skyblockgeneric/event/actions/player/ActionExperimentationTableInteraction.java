package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.tag.Tag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.skyblockgeneric.gui.inventories.experiments.GUIExperiments;
import net.swofty.type.skyblockgeneric.block.blocks.BlockExperimentationTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public class ActionExperimentationTableInteraction implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerEntityInteractEvent event) {
        if (!(event.getPlayer() instanceof SkyBlockPlayer player)) return;
        
        // Check if the interacted entity is part of an Experimentation Table structure
        String isExperimentationTable = event.getEntity().getTag(Tag.String("is_experimentation_table"));
        if (!"true".equals(isExperimentationTable)) return;
        
        // Cancel the default interaction
        // Note: PlayerEntityInteractEvent doesn't have setCancelled method in this version
        
        // Right-click: Open the experiments menu
        try {
            new GUIExperiments().open(player);
        } catch (Exception e) {
            player.sendMessage("§cFailed to open experiments menu: " + e.getMessage());
        }
    }

    // Note: PlayerEntityAttackEvent doesn't exist in this Minestom version
    // Left-click removal is handled by the block break event instead
}
