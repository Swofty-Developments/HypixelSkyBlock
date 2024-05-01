package net.swofty.types.generic.event.actions.custom;

import net.minestom.server.event.Event;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;

public class ActionChangeMiningRegion extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerRegionChangeEvent regionChangeEvent = (PlayerRegionChangeEvent) event;
        SkyBlockPlayer player = regionChangeEvent.getPlayer();

        if (SkyBlockConst.isIslandServer()) return;

        if (!player.getActiveEffects().stream().map(f -> f.potion().effect()).toList().contains(PotionEffect.MINING_FATIGUE))
            player.addEffect(new Potion(PotionEffect.MINING_FATIGUE, (byte) 255, 9999999));
    }
}
