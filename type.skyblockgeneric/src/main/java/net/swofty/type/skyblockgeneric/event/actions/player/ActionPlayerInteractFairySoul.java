package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.entity.EntityFairySoul;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.fairysouls.FairySoul;

public class ActionPlayerInteractFairySoul implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerEntityInteractEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (event.getTarget() instanceof EntityFairySoul entitySoul) {
            FairySoul fairySoul = entitySoul.parent;
            if (fairySoul == null) return;

            fairySoul.collect(player);
        }
    }
}
