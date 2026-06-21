package net.swofty.type.skyblockgeneric.event.actions.item;

import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.item.ItemAnimation;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.ArrowComponent;
import net.swofty.type.skyblockgeneric.item.components.BowComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionUseShortBow implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBeginItemUseEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        ItemAnimation type = event.getAnimation();

        if (type.equals(ItemAnimation.BOW)) {
            SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
            BowComponent bow = item.getComponent(BowComponent.class);

            if (!bow.isShouldBeArrow()) {
                // Bow is a "shortbow", fires instantly at full power
                bow.onBowShoot(player, item, 1.0);
                event.setCancelled(true);
            } else {
                // If there is an arrow, lets make sure they have one
                if (player.getAllOfComponentInInventory(ArrowComponent.class).isEmpty()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
