package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.minestom.server.entity.attribute.Attribute;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionChangeMiningRegion implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (HypixelConst.isIslandServer()) return;

        player.getAttribute(Attribute.MINING_EFFICIENCY).setBaseValue(0D);
    }
}
