package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.entity.EntityFairySoul;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionPlayerInteractFairySoul implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof EntityFairySoul entitySoul) {
            FairySoul fairySoul = entitySoul.parent;
            if (fairySoul == null) return;

            fairySoul.collect(player);
        }
    }
}
