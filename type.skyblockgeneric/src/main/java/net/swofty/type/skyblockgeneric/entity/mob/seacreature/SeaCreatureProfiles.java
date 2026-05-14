package net.swofty.type.skyblockgeneric.entity.mob.seacreature;

import net.minestom.server.entity.EntityType;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;

import java.util.List;

/**
 * Canonical Hypixel sea creature profiles. Each constant is the entire
 * declarative spec for one creature: stats, AI flavour, fishing rewards.
 * Adding a new creature is one builder chain, not a new class.
 */
public final class SeaCreatureProfiles {

    public static final SeaCreatureProfile SQUID = SeaCreatureProfile.builder("SQUID")
            .displayName("Squid")
            .level(1)
            .entityType(EntityType.SQUID)
            .health(145D).damage(5D).speed(30D)
            .behaviour(SeaCreatureBehaviour.passive(8))
            .mobTypes(MobType.AQUATIC)
            .fishingXpReward(20).xpOrbs(4)
            .build();

    public static final SeaCreatureProfile SEA_WALKER = SeaCreatureProfile.builder("SEA_WALKER")
            .displayName("Sea Walker")
            .level(4)
            .entityType(EntityType.ZOMBIE)
            .health(240D).damage(25D).speed(70D)
            .behaviour(SeaCreatureBehaviour.aggressive(1.6, 20, 16))
            .mobTypes(MobType.AQUATIC, MobType.UNDEAD)
            .damageCooldownMs(400)
            .fishingXpReward(40).xpOrbs(6)
            .build();

    public static final SeaCreatureProfile NIGHT_SQUID = SeaCreatureProfile.builder("NIGHT_SQUID")
            .displayName("Night Squid")
            .level(6)
            .entityType(EntityType.GLOW_SQUID)
            .health(400D).damage(15D).speed(30D)
            .behaviour(SeaCreatureBehaviour.passive(6))
            .mobTypes(MobType.AQUATIC)
            .fishingXpReward(80).xpOrbs(8)
            .build();

    public static final SeaCreatureProfile SEA_GUARDIAN = SeaCreatureProfile.builder("SEA_GUARDIAN")
            .displayName("Sea Guardian")
            .level(8)
            .entityType(EntityType.GUARDIAN)
            .health(600D).damage(40D).speed(60D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.0, 25, 18))
            .mobTypes(MobType.AQUATIC)
            .damageCooldownMs(400)
            .fishingXpReward(120).xpOrbs(10)
            .build();

    public static final SeaCreatureProfile MAGMA_SLUG = SeaCreatureProfile.builder("MAGMA_SLUG")
            .displayName("Magma Slug")
            .level(21)
            .entityType(EntityType.MAGMA_CUBE)
            .health(850D).damage(170D).speed(50D)
            .behaviour(SeaCreatureBehaviour.aggressive(1.6, 20, 14))
            .mobTypes(MobType.MAGMATIC, MobType.CUBIC)
            .damageCooldownMs(400)
            .fishingXpReward(450).xpOrbs(12)
            .build();

    public static final SeaCreatureProfile LAVA_BLAZE = SeaCreatureProfile.builder("LAVA_BLAZE")
            .displayName("Lava Blaze")
            .level(30)
            .entityType(EntityType.BLAZE)
            .health(1500D).damage(250D).speed(90D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.5, 20, 20))
            .mobTypes(MobType.MAGMATIC)
            .damageCooldownMs(350)
            .fishingXpReward(1000).xpOrbs(18)
            .build();

    public static final List<SeaCreatureProfile> CANONICAL = List.of(
            SQUID, SEA_WALKER, NIGHT_SQUID, SEA_GUARDIAN, MAGMA_SLUG, LAVA_BLAZE
    );

    private SeaCreatureProfiles() {
    }
}
