package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.item.ItemAnimation;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.BowComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionUseShortBow implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerBeginItemUseEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        ItemAnimation type = event.getAnimation();

        if (type.equals(ItemAnimation.BOW)) {
            SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
            BowComponent bow = item.getComponent(BowComponent.class);

            if (!bow.isShouldBeArrow()) {
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
