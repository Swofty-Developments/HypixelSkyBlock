package net.swofty.type.ravengardgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.ravengardgeneric.entity.animation.AnimReviewService;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

public class ActionPlayerAnimReview implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onUse(PlayerUseItemEvent event) {
        if (!(event.getPlayer() instanceof RavengardPlayer player)) return;
        AnimReviewService.Session session = AnimReviewService.session(player);
        if (session == null) return;
        String action = event.getItemStack().getTag(AnimReviewService.CONTROL);
        if (action != null) {
            event.setCancelled(true);
            session.control(action);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onSwing(PlayerHandAnimationEvent event) {
        if (!(event.getPlayer() instanceof RavengardPlayer player)) return;
        AnimReviewService.Session session = AnimReviewService.session(player);
        if (session == null) return;
        String action = player.getItemInMainHand().getTag(AnimReviewService.CONTROL);
        if (action != null) {
            session.control(action);
        }
    }
}
