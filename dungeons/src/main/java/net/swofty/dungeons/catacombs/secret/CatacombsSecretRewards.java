package net.swofty.dungeons.catacombs.secret;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.swofty.dungeons.catacombs.CatacombsFloor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CatacombsSecretRewards {
    private static final Set<CatacombsSecretType> COMMON = Set.of(
            CatacombsSecretType.CHEST,
            CatacombsSecretType.GROUND_ITEM,
            CatacombsSecretType.BAT);

    public static List<CatacombsSecretReward> defaults() {
        return List.of(
                new CatacombsSecretReward(SecretRewardType.RANDOM_BLESSING, CatacombsFloor.ENTRANCE,
                        Set.of(CatacombsSecretType.CHEST, CatacombsSecretType.BAT)),
                new CatacombsSecretReward(SecretRewardType.HEALING_POTION, CatacombsFloor.ENTRANCE, COMMON),
                new CatacombsSecretReward(SecretRewardType.DECOY, CatacombsFloor.ENTRANCE, COMMON),
                new CatacombsSecretReward(SecretRewardType.TRAINING_WEIGHTS, CatacombsFloor.ENTRANCE, COMMON),
                new CatacombsSecretReward(SecretRewardType.SPIRIT_LEAP, CatacombsFloor.ENTRANCE, COMMON),
                new CatacombsSecretReward(SecretRewardType.INFLATABLE_JERRY, CatacombsFloor.ENTRANCE, COMMON),
                new CatacombsSecretReward(SecretRewardType.TRAP, CatacombsFloor.ENTRANCE, COMMON),
                new CatacombsSecretReward(SecretRewardType.DEFUSE_KIT, CatacombsFloor.ENTRANCE, COMMON),
                new CatacombsSecretReward(SecretRewardType.DUNGEON_CHEST_KEY, CatacombsFloor.FLOOR_FOUR, COMMON),
                new CatacombsSecretReward(SecretRewardType.TREASURE_TALISMAN, CatacombsFloor.FLOOR_FOUR, COMMON),
                new CatacombsSecretReward(SecretRewardType.ARCHITECTS_FIRST_DRAFT, CatacombsFloor.ENTRANCE,
                        Set.of(CatacombsSecretType.CHEST)),
                new CatacombsSecretReward(SecretRewardType.SECRET_DYE, CatacombsFloor.ENTRANCE,
                        Set.of(CatacombsSecretType.CHEST)),
                new CatacombsSecretReward(SecretRewardType.WITHER_ESSENCE, CatacombsFloor.ENTRANCE,
                        Set.of(CatacombsSecretType.WITHER_ESSENCE)));
    }
}
