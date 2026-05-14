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

    /* ---------- water ---------- */

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

    public static final SeaCreatureProfile SEA_WITCH = SeaCreatureProfile.builder("SEA_WITCH")
            .displayName("Sea Witch")
            .level(10)
            .entityType(EntityType.WITCH)
            .health(800D).damage(60D).speed(60D)
            .behaviour(SeaCreatureBehaviour.aggressive(1.5, 30, 16))
            .mobTypes(MobType.AQUATIC, MobType.ARCANE)
            .damageCooldownMs(450)
            .fishingXpReward(150).xpOrbs(12)
            .build();

    public static final SeaCreatureProfile SEA_ARCHER = SeaCreatureProfile.builder("SEA_ARCHER")
            .displayName("Sea Archer")
            .level(15)
            .entityType(EntityType.SKELETON)
            .health(1000D).damage(80D).speed(75D)
            .behaviour(SeaCreatureBehaviour.aggressive(1.7, 25, 20))
            .mobTypes(MobType.AQUATIC, MobType.SKELETAL)
            .damageCooldownMs(400)
            .fishingXpReward(200).xpOrbs(14)
            .build();

    public static final SeaCreatureProfile MONSTER_OF_THE_DEEP = SeaCreatureProfile.builder("MONSTER_OF_THE_DEEP")
            .displayName("Monster of the Deep")
            .level(20)
            .entityType(EntityType.DROWNED)
            .health(1500D).damage(120D).speed(80D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.0, 22, 20))
            .mobTypes(MobType.AQUATIC, MobType.UNDEAD)
            .damageCooldownMs(400)
            .fishingXpReward(300).xpOrbs(16)
            .build();

    public static final SeaCreatureProfile CATFISH = SeaCreatureProfile.builder("CATFISH")
            .displayName("Catfish")
            .level(24)
            .entityType(EntityType.OCELOT)
            .health(2000D).damage(150D).speed(90D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.2, 18, 18))
            .mobTypes(MobType.AQUATIC, MobType.ANIMAL)
            .damageCooldownMs(350)
            .fishingXpReward(400).xpOrbs(18)
            .build();

    public static final SeaCreatureProfile CARROT_KING = SeaCreatureProfile.builder("CARROT_KING")
            .displayName("Carrot King")
            .level(26)
            .entityType(EntityType.RABBIT)
            .health(2500D).damage(170D).speed(95D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.0, 25, 16))
            .mobTypes(MobType.AQUATIC, MobType.ANIMAL)
            .damageCooldownMs(400)
            .fishingXpReward(500).xpOrbs(20)
            .build();

    public static final SeaCreatureProfile SEA_LEECH = SeaCreatureProfile.builder("SEA_LEECH")
            .displayName("Sea Leech")
            .level(30)
            .entityType(EntityType.SILVERFISH)
            .health(3000D).damage(200D).speed(110D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.4, 18, 14))
            .mobTypes(MobType.AQUATIC, MobType.ARTHROPOD)
            .damageCooldownMs(350)
            .fishingXpReward(650).xpOrbs(22)
            .build();

    public static final SeaCreatureProfile GUARDIAN_DEFENDER = SeaCreatureProfile.builder("GUARDIAN_DEFENDER")
            .displayName("Guardian Defender")
            .level(35)
            .entityType(EntityType.GUARDIAN)
            .health(5000D).damage(250D).speed(70D)
            .behaviour(SeaCreatureBehaviour.aggressive(1.8, 30, 20))
            .mobTypes(MobType.AQUATIC, MobType.SHIELDED)
            .damageCooldownMs(450)
            .fishingXpReward(900).xpOrbs(26)
            .build();

    public static final SeaCreatureProfile WATER_HYDRA = SeaCreatureProfile.builder("WATER_HYDRA")
            .displayName("Water Hydra")
            .level(60)
            .entityType(EntityType.WITHER)
            .health(50000D).damage(800D).speed(80D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.5, 20, 28))
            .mobTypes(MobType.AQUATIC, MobType.ELUSIVE)
            .damageCooldownMs(300)
            .fishingXpReward(7500).xpOrbs(60)
            .build();

    public static final SeaCreatureProfile THE_SEA_EMPEROR = SeaCreatureProfile.builder("THE_SEA_EMPEROR")
            .displayName("The Sea Emperor")
            .level(80)
            .entityType(EntityType.ELDER_GUARDIAN)
            .health(150000D).damage(1500D).speed(60D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.0, 30, 32))
            .mobTypes(MobType.AQUATIC, MobType.MYTHOLOGICAL, MobType.ELUSIVE)
            .damageCooldownMs(400)
            .fishingXpReward(20000).xpOrbs(120)
            .build();

    /* ---------- lava (Crimson Isle) ---------- */

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

    public static final SeaCreatureProfile MOOGMA = SeaCreatureProfile.builder("MOOGMA")
            .displayName("Moogma")
            .level(22)
            .entityType(EntityType.STRIDER)
            .health(900D).damage(180D).speed(75D)
            .behaviour(SeaCreatureBehaviour.aggressive(1.8, 22, 16))
            .mobTypes(MobType.MAGMATIC, MobType.ANIMAL)
            .damageCooldownMs(400)
            .fishingXpReward(500).xpOrbs(14)
            .build();

    public static final SeaCreatureProfile LAVA_LEECH = SeaCreatureProfile.builder("LAVA_LEECH")
            .displayName("Lava Leech")
            .level(24)
            .entityType(EntityType.SILVERFISH)
            .health(1100D).damage(200D).speed(100D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.4, 18, 14))
            .mobTypes(MobType.MAGMATIC, MobType.ARTHROPOD)
            .damageCooldownMs(350)
            .fishingXpReward(550).xpOrbs(14)
            .build();

    public static final SeaCreatureProfile PYROCLASTIC_WORM = SeaCreatureProfile.builder("PYROCLASTIC_WORM")
            .displayName("Pyroclastic Worm")
            .level(28)
            .entityType(EntityType.ENDERMITE)
            .health(1400D).damage(240D).speed(95D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.2, 18, 16))
            .mobTypes(MobType.MAGMATIC, MobType.ARTHROPOD)
            .damageCooldownMs(350)
            .fishingXpReward(800).xpOrbs(16)
            .build();

    public static final SeaCreatureProfile FIRE_EEL = SeaCreatureProfile.builder("FIRE_EEL")
            .displayName("Fire Eel")
            .level(28)
            .entityType(EntityType.PHANTOM)
            .health(1300D).damage(220D).speed(100D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.5, 18, 18))
            .mobTypes(MobType.MAGMATIC, MobType.AIRBORNE)
            .damageCooldownMs(350)
            .fishingXpReward(800).xpOrbs(16)
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

    public static final SeaCreatureProfile TAURUS = SeaCreatureProfile.builder("TAURUS")
            .displayName("Taurus")
            .level(36)
            .entityType(EntityType.HOGLIN)
            .health(6000D).damage(400D).speed(85D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.4, 22, 22))
            .mobTypes(MobType.MAGMATIC, MobType.ANIMAL)
            .damageCooldownMs(400)
            .fishingXpReward(2500).xpOrbs(32)
            .build();

    public static final SeaCreatureProfile LORD_JAWBUS = SeaCreatureProfile.builder("LORD_JAWBUS")
            .displayName("Lord Jawbus")
            .level(45)
            .entityType(EntityType.GHAST)
            .health(40000D).damage(900D).speed(70D)
            .behaviour(SeaCreatureBehaviour.aggressive(2.0, 25, 30))
            .mobTypes(MobType.MAGMATIC, MobType.INFERNAL, MobType.ELUSIVE)
            .damageCooldownMs(350)
            .fishingXpReward(15000).xpOrbs(80)
            .build();

    public static final List<SeaCreatureProfile> CANONICAL = List.of(
            // Water
            SQUID, SEA_WALKER, NIGHT_SQUID, SEA_GUARDIAN,
            SEA_WITCH, SEA_ARCHER, MONSTER_OF_THE_DEEP, CATFISH, CARROT_KING,
            SEA_LEECH, GUARDIAN_DEFENDER, WATER_HYDRA, THE_SEA_EMPEROR,
            // Lava
            MAGMA_SLUG, MOOGMA, LAVA_LEECH, PYROCLASTIC_WORM, FIRE_EEL,
            LAVA_BLAZE, TAURUS, LORD_JAWBUS
    );

    private SeaCreatureProfiles() {
    }
}
