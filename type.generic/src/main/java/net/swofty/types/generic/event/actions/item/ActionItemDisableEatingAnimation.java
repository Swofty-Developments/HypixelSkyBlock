package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.DisableAnimationComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionItemDisableEatingAnimation implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerBeginItemUseEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());

        if (!(item.hasComponent(DisableAnimationComponent.class))) return;
        DisableAnimationComponent disableAnimationImpl = item.getComponent(DisableAnimationComponent.class);

        if (disableAnimationImpl.getDisabledAnimations().isEmpty()) return;
        if (!disableAnimationImpl.getDisabledAnimations().contains(event.getAnimation())) return;

        event.setCancelled(true);
    }
}
