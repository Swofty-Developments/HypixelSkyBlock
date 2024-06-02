package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.BowImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionUseShortBow implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerItemAnimationEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        PlayerItemAnimationEvent.ItemAnimationType type = event.getItemAnimationType();

        if (type.equals(PlayerItemAnimationEvent.ItemAnimationType.BOW)) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemInMainHand());
            BowImpl bow = (BowImpl) item.getGenericInstance();

            if (!bow.shouldBeArrow()) {
                // Bow is a "shortbow", call event now
                bow.onBowShoot(player, item);
                event.setCancelled(true);
            } else {
                // If there's no arrow, also cancel the event
                if (player.getArrow() == null) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
