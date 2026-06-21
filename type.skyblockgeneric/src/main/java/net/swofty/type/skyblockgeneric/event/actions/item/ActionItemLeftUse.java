package net.swofty.type.skyblockgeneric.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.InteractableComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionItemLeftUse implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerHandAnimationEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!item.hasComponent(InteractableComponent.class)) return;
        InteractableComponent interactableComponent = item.getComponent(InteractableComponent.class);
        interactableComponent.onLeftClick(player, item);
    }
}
