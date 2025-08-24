package net.swofty.type.skyblockgeneric.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.InteractableComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionItemLeftUse implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerHandAnimationEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!item.hasComponent(InteractableComponent.class)) return;
        InteractableComponent interactableComponent = item.getComponent(InteractableComponent.class);
        interactableComponent.onLeftClick(player, item);
    }
}
