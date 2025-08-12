package net.swofty.type.skyblockgeneric.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.components.InteractableComponent;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionItemLeftUse implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerHandAnimationEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (!item.hasComponent(InteractableComponent.class)) return;
        InteractableComponent interactableComponent = item.getComponent(InteractableComponent.class);
        interactableComponent.onLeftClick(player, item);
    }
}
