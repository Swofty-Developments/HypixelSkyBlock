package net.swofty.types.generic.event.actions.custom;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
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

        if (!player.getActiveEffects().stream().map(f -> f.potion().effect()).toList().contains(PotionEffect.MINING_FATIGUE))
            player.addEffect(new Potion(PotionEffect.MINING_FATIGUE, (byte) 255, 9999999));
    }
}
