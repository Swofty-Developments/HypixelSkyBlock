package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.extension.MinionExtension;
import net.swofty.types.generic.minion.extension.extensions.MinionSkinExtension;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class ActionItemDisableEatingAnimation implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void onEat(PlayerItemAnimationEvent event) {
        if (!event.getItemAnimationType().equals(PlayerItemAnimationEvent.ItemAnimationType.EAT)) return;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        SkyBlockItem skyBlockItem = new SkyBlockItem(player.getItemInMainHand());

        if (skyBlockItem.isNA() || skyBlockItem.isAir()) return;

        ItemType type = skyBlockItem.getAttributeHandler().getItemTypeAsType();

        List<MinionExtension> extensions = new ArrayList<>();

        if (type == ItemType.BOOSTER_COOKIE || type == ItemType.EDIBLE_MACE)
            event.setCancelled(true);
    }
}
