package net.swofty.dungeons.catacombs.run;

import net.swofty.dungeons.catacombs.CatacombsFloorDefinition;
import net.swofty.dungeons.catacombs.classes.DungeonClassType;

import java.util.Map;
import java.util.UUID;

public record CatacombsRunConfig(
        CatacombsFloorDefinition floor,
        Map<UUID, DungeonClassType> partyClasses,
        int totalRooms,
        int totalSecrets
) {
    public CatacombsRunConfig {
        partyClasses = Map.copyOf(partyClasses);
        if (!floor.rules().allowsPartySize(partyClasses.size())) {
            throw new IllegalArgumentException("Invalid Catacombs party size " + partyClasses.size());
        }
    }
}
