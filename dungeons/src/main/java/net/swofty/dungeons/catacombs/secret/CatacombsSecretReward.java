package net.swofty.dungeons.catacombs.secret;

import net.swofty.dungeons.catacombs.CatacombsFloor;

import java.util.Set;

public record CatacombsSecretReward(
        SecretRewardType type,
        CatacombsFloor minimumFloor,
        Set<CatacombsSecretType> secretTypes
) {
    public CatacombsSecretReward {
        secretTypes = Set.copyOf(secretTypes);
    }

    public boolean canDrop(CatacombsFloor floor, CatacombsSecretType secretType) {
        return floor.ordinal() >= minimumFloor.ordinal() && secretTypes.contains(secretType);
    }
}
