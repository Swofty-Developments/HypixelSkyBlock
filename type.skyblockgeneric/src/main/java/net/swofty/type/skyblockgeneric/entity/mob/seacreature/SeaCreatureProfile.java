package net.swofty.type.skyblockgeneric.entity.mob.seacreature;

import net.minestom.server.entity.EntityType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;

import java.util.List;

/**
 * Immutable, declarative description of a sea creature. The concrete mob
 * class just hands one of these to {@link SeaCreatureMob} and the base
 * class does the rest — no abstract-method ceremony, no per-creature
 * duplicated builder boilerplate.
 */
public record SeaCreatureProfile(
        String id,
        String displayName,
        int level,
        EntityType entityType,
        double health,
        double damage,
        double speed,
        long damageCooldownMs,
        long fishingXpReward,
        int xpOrbs,
        List<MobType> mobTypes,
        SeaCreatureBehaviour behaviour,
        SkyBlockLootTable lootTable
) {

    public ItemStatistics asBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, health)
                .withBase(ItemStatistic.DAMAGE, damage)
                .withBase(ItemStatistic.SPEED, speed)
                .build();
    }

    public OtherLoot asOtherLoot() {
        return new OtherLoot(fishingXpReward, 0, xpOrbs);
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static final class Builder {
        private final String id;
        private String displayName;
        private int level = 1;
        private EntityType entityType = EntityType.SQUID;
        private double health = 100D;
        private double damage = 0D;
        private double speed = 30D;
        private long damageCooldownMs = 500L;
        private long fishingXpReward = 0L;
        private int xpOrbs = 0;
        private List<MobType> mobTypes = List.of(MobType.AQUATIC);
        private SeaCreatureBehaviour behaviour = SeaCreatureBehaviour.passive(8);
        private SkyBlockLootTable lootTable = null;

        private Builder(String id) {
            this.id = id;
            this.displayName = id;
        }

        public Builder displayName(String displayName) { this.displayName = displayName; return this; }
        public Builder level(int level) { this.level = level; return this; }
        public Builder entityType(EntityType entityType) { this.entityType = entityType; return this; }
        public Builder health(double health) { this.health = health; return this; }
        public Builder damage(double damage) { this.damage = damage; return this; }
        public Builder speed(double speed) { this.speed = speed; return this; }
        public Builder damageCooldownMs(long damageCooldownMs) { this.damageCooldownMs = damageCooldownMs; return this; }
        public Builder fishingXpReward(long fishingXpReward) { this.fishingXpReward = fishingXpReward; return this; }
        public Builder xpOrbs(int xpOrbs) { this.xpOrbs = xpOrbs; return this; }
        public Builder mobTypes(MobType... mobTypes) { this.mobTypes = List.of(mobTypes); return this; }
        public Builder behaviour(SeaCreatureBehaviour behaviour) { this.behaviour = behaviour; return this; }
        public Builder lootTable(SkyBlockLootTable lootTable) { this.lootTable = lootTable; return this; }

        public SeaCreatureProfile build() {
            return new SeaCreatureProfile(
                    id, displayName, level, entityType,
                    health, damage, speed, damageCooldownMs,
                    fishingXpReward, xpOrbs, mobTypes, behaviour, lootTable
            );
        }
    }
}
