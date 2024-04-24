package net.swofty.types.generic.event.actions.player.blocks;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.block.impl.BlockInteractable;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles clicking on blocks",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionBlockInteract extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerBlockInteractEvent event = (PlayerBlockInteractEvent) tempEvent;
        if (SkyBlockBlock.isSkyBlockBlock(event.getBlock())) {
            SkyBlockBlock block = new SkyBlockBlock(event.getBlock());
            if (block.getGenericInstance() instanceof BlockInteractable interactable) {
                interactable.onInteract(event, block);
            }
        }
    }
}
