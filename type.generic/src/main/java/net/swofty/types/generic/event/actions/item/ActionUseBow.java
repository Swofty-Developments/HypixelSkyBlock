package net.swofty.types.generic.event.actions.item;

import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.BowComponent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionUseBow implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerFinishItemUseEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
        if (item.hasComponent(BowComponent.class)) {
            BowComponent bow = item.getComponent(BowComponent.class);
            if (bow.isShouldBeArrow()) {
                bow.onBowShoot(player, item);
                player.getInventory().update();
            }
        }
    }
}
