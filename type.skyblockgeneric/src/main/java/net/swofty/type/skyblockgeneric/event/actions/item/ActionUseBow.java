package net.swofty.type.skyblockgeneric.event.actions.item;

import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.BowComponent;
import SkyBlockPlayer;

public class ActionUseBow implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
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
