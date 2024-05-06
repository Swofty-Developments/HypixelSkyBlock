package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerDisableEatingAnimation implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerItemAnimationEvent event) {
        if (event.getItemAnimationType() != PlayerItemAnimationEvent.ItemAnimationType.EAT) return;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        SkyBlockItem skyBlockItem = new SkyBlockItem(player.getItemInMainHand());
        if (skyBlockItem.isNA() || skyBlockItem.isAir()) return;

        if (skyBlockItem.getAttributeHandler().getItemTypeAsType() != ItemType.BOOSTER_COOKIE) return;

        event.setCancelled(true);
    }
}
