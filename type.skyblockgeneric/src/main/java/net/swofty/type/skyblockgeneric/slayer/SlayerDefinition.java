package net.swofty.type.skyblockgeneric.slayer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;

public record SlayerDefinition(
    SlayerType type,
    boolean enabled,
    Optional<SlayerUnlockRequirement> unlockRequirement,
    List<MobType> targetMobTypes,
    List<SlayerLevelReward> levels,
    Map<SlayerTier, SlayerTierDefinition> tiers
) {
    public boolean accepts(List<MobType> mobTypes) {
        return mobTypes.stream().anyMatch(targetMobTypes::contains);
    }

    public int levelForXp(int xp) {
        int level = 0;
        for (SlayerLevelReward reward : levels) {
            if (xp >= reward.requiredXp()) {
                level = reward.level();
            }
        }
        return level;
    }
}
