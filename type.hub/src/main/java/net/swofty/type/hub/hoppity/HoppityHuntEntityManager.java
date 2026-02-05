package net.swofty.type.hub.hoppity;

import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.skyblockgeneric.chocolatefactory.HoppityEggLocations;
import net.swofty.type.skyblockgeneric.chocolatefactory.HoppityEggType;
import net.swofty.type.skyblockgeneric.chocolatefactory.HoppityHuntManager;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HoppityHuntEntityManager {

    private final List<HoppityEggEntity> spawnedEggs = new ArrayList<>();
    private final List<InteractionEntity> spawnedInteractions = new ArrayList<>();

    public void spawnAllEggs() {
        Map<HoppityEggLocations, HoppityEggType> locationEggTypes = HoppityHuntManager.getInstance().getLocationEggTypes();

        for (Map.Entry<HoppityEggLocations, HoppityEggType> entry : locationEggTypes.entrySet()) {
            HoppityEggLocations location = entry.getKey();
            HoppityEggType eggType = entry.getValue();

            HoppityEggEntity eggEntity = new HoppityEggEntity(location, eggType);
            eggEntity.spawn(HypixelConst.getInstanceContainer());
            spawnedEggs.add(eggEntity);

            InteractionEntity interaction = new InteractionEntity(1.0f, 1.0f, (player, event) -> {
                if (player instanceof SkyBlockPlayer skyBlockPlayer) {
                    HoppityHuntManager.getInstance().claimEgg(skyBlockPlayer, location);
                }
            });
            interaction.setInstance(HypixelConst.getInstanceContainer(), location.getPosition());
            spawnedInteractions.add(interaction);
        }
    }

    public void despawnAllEggs() {
        for (HoppityEggEntity egg : spawnedEggs) {
            egg.remove();
        }
        spawnedEggs.clear();

        for (InteractionEntity interaction : spawnedInteractions) {
            interaction.remove();
        }
        spawnedInteractions.clear();
    }
}
