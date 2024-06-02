package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.item.ItemUpdateStateEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.BowImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionUseBow implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(ItemUpdateStateEvent event) {
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
