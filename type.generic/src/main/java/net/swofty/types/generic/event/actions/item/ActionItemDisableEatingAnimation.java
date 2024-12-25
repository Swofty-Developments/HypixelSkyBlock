package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.DisableAnimationComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionItemDisableEatingAnimation implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerItemAnimationEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());

        if (!(item.hasComponent(DisableAnimationComponent.class))) return;
        DisableAnimationComponent disableAnimationImpl = item.getComponent(DisableAnimationComponent.class);

        if (disableAnimationImpl.getDisabledAnimations().isEmpty()) return;
        if (!disableAnimationImpl.getDisabledAnimations().contains(event.getItemAnimationType())) return;

        event.setCancelled(true);
    }
}
