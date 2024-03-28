package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.Event;
import net.minestom.server.event.item.ItemUpdateStateEvent;
import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.BowImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;

@EventParameters(description = "Handles drawing back bows",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionUseBow extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return ItemUpdateStateEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        ItemUpdateStateEvent event = (ItemUpdateStateEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.hasHandAnimation()) return;

        SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemInMainHand());
        if (item.getGenericInstance() != null && item.getGenericInstance() instanceof BowImpl bow) {
            if (bow.shouldBeArrow()) {
                bow.onBowShoot(player, item);
                player.getInventory().update();
            }
        }
    }
}
