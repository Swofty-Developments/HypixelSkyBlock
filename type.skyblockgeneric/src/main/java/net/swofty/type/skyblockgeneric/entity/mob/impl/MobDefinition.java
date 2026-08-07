package net.swofty.type.skyblockgeneric.entity.mob.impl;

import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.region.RegionType;

import java.util.List;

public record MobDefinition(
        String displayName,
        String mobId,
        EntityType entityType,
        int level,
        double health,
        double damage,
        double speed,
        long combatXp,
        int coins,
        int xpOrbs,
        List<MobType> mobTypes,
        List<LootDrop> drops,
        Material guiMaterial,
        int maxBestiaryTier,
        int bestiaryBracket,
        RegionType targetRegion,
        boolean hostile,
        boolean targetsPlayers
) {
    public MobDefinition {
        mobTypes = List.copyOf(mobTypes);
        drops = List.copyOf(drops);
    }

    public record LootDrop(net.swofty.commons.skyblock.item.ItemType itemType, int minimum, int maximum,
                           double chancePercent) {
        public LootDrop(net.swofty.commons.skyblock.item.ItemType itemType, int amount, double chancePercent) {
            this(itemType, amount, amount, chancePercent);
        }
    }
}
