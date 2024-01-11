package net.swofty.types.generic.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles item interactable use for right clicks",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionItemRightUse extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerUseItemEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerUseItemEvent playerUseItemEvent = (PlayerUseItemEvent) event;
        ItemStack itemStack = playerUseItemEvent.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) playerUseItemEvent.getPlayer();

        if (item.getGenericInstance() != null && item.getGenericInstance() instanceof Interactable interactable) {
            interactable.onRightInteract(player, item);
        }
    }
}
