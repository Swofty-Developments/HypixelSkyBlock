package net.swofty.commons.skyblock.event.actions.custom;

import net.minestom.server.event.Event;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.custom.PlayerRegionChangeEvent;
import net.swofty.commons.skyblock.region.RegionType;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.utility.Groups;

@EventParameters(description = "Handles mining fatigue in mining regions",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.HUB,
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
