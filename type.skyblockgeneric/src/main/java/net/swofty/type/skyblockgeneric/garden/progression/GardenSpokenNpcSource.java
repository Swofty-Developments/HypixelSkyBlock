package net.swofty.type.skyblockgeneric.garden.progression;

import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public interface GardenSpokenNpcSource extends GardenProgressionSource {
    String gardenSpokenNpcId();

    @Override
    default List<GardenProgressionReward> gardenProgressionRewards(SkyBlockPlayer player) {
        return List.of(GardenProgressionReward.spokenNpc(gardenSpokenNpcId()));
    }
}
