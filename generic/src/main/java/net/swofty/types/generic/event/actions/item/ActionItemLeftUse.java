package net.swofty.types.generic.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles item interactable use for left clicks",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionItemLeftUse extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerHandAnimationEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerHandAnimationEvent playerUseItemEvent = (PlayerHandAnimationEvent) event;
        ItemStack itemStack = playerUseItemEvent.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) playerUseItemEvent.getPlayer();

        if (item.clazz != null && item.clazz.newInstance() instanceof Interactable interactable) {
            interactable.onLeftInteract(player, item);
        }
    }
}
