package net.swofty.type.skyblockgeneric.slayer;

import java.util.Optional;
import net.swofty.commons.skyblock.item.ItemType;

public record SlayerTierDefinition(
    SlayerTier tier,
    int cost,
    int requiredCombatXp,
    int slayerXp,
    int bossLevel,
    double bossHealth,
    double bossDamage,
    double bossSpeed,
    int tokenDrops,
    Optional<ItemType> tokenItem
) {
    public String displayName(SlayerType type) {
        return type.displayName() + " " + tier.numeral();
    }
}
