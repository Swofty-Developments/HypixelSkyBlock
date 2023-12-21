package net.swofty.event.actions.custom;

import net.minestom.server.event.Event;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.PlayerRegionChangeEvent;
import net.swofty.region.RegionType;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.Groups;

@EventParameters(description = "Handles mining fatigue in mining regions",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class ActionChangeMiningRegion extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerRegionChangeEvent regionChangeEvent = (PlayerRegionChangeEvent) event;
        SkyBlockPlayer player = regionChangeEvent.getPlayer();
        RegionType newRegion = regionChangeEvent.getTo();

        if (newRegion != null && Groups.MINING_REGIONS.contains(newRegion)) {
            if (!player.getActiveEffects().stream().map(f -> f.getPotion().effect()).toList().contains(PotionEffect.MINING_FATIGUE))
                player.addEffect(new Potion(PotionEffect.MINING_FATIGUE, (byte) 255, 9999999));
        } else {
            if (player.getActiveEffects().stream().map(f -> f.getPotion().effect()).toList().contains(PotionEffect.MINING_FATIGUE))
                player.removeEffect(PotionEffect.MINING_FATIGUE);
        }
    }
}
