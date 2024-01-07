package net.swofty.commons.skyblock.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.Interactable;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Handles item interactable use for right clicks",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
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

        if (item.clazz != null && item.clazz.newInstance() instanceof Interactable interactable) {
            interactable.onRightInteract(player, item);
        }
    }
}
