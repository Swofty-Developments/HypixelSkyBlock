package net.swofty.types.generic.event.actions.custom;

import net.minestom.server.entity.attribute.Attribute;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionChangeMiningRegion implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (SkyBlockConst.isIslandServer()) return;

        player.getAttribute(Attribute.MINING_EFFICIENCY).setBaseValue(0D);
    }
}
