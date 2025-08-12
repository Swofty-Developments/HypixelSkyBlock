package net.swofty.type.skyblockgeneric.event.actions.item;

import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.components.DisableAnimationComponent;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionItemDisableEatingAnimation implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerBeginItemUseEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());

        if (!(item.hasComponent(DisableAnimationComponent.class))) return;
        DisableAnimationComponent disableAnimationImpl = item.getComponent(DisableAnimationComponent.class);

        if (disableAnimationImpl.getDisabledAnimations().isEmpty()) return;
        if (!disableAnimationImpl.getDisabledAnimations().contains(event.getAnimation())) return;

        event.setCancelled(true);
    }
}
