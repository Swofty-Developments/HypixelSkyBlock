package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.skyblockgeneric.block.blocks.BlockExperimentationTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionExperimentationTablePlacement implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerUseItemEvent event) {
        if (!(event.getPlayer() instanceof SkyBlockPlayer player)) return;
        
        ItemStack item = event.getItemStack();
        
        // Check if this is the Experimentation Table item
        if (!BlockExperimentationTable.isExperimentationTable(item)) return;
        
        // Cancel the default item use
        // Not cancellable in this event version; just handle placement
        
        // Calculate placement position (one block forward from player)
        Pos playerPos = player.getPosition();
        Pos placementPos = playerPos.add(
            Math.sin(Math.toRadians(-player.getPosition().yaw())) * 2,
            0,
            Math.cos(Math.toRadians(-player.getPosition().yaw())) * 2
        );
        
        // Handle the placement
        BlockExperimentationTable.handlePlacement(player, placementPos);
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerUseItemOnBlockEvent event) {
        if (!(event.getPlayer() instanceof SkyBlockPlayer player)) return;

        ItemStack item = event.getPlayer().getItemInMainHand();

        if (!BlockExperimentationTable.isExperimentationTable(item)) return;

        // Not cancellable here; proceed with placement

        Pos playerPos = player.getPosition();
        Pos placementPos = playerPos.add(
                Math.sin(Math.toRadians(-player.getPosition().yaw())) * 2,
                0,
                Math.cos(Math.toRadians(-player.getPosition().yaw())) * 2
        );

        BlockExperimentationTable.handlePlacement(player, placementPos);
    }
}
