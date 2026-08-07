package net.swofty.type.skyblockgeneric.entity.mob.impl;

import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.region.RegionType;

import java.util.List;

public final class StandardMobDefinitions {
    private StandardMobDefinitions() {
    }

    public static MobDefinition zombieVillager() {
        return hostile("Zombie Villager", "ZOMBIE_VILLAGER", EntityType.ZOMBIE_VILLAGER, 1, 120, 24, 7,
                List.of(MobType.UNDEAD), Material.ZOMBIE_VILLAGER_SPAWN_EGG,
                List.of(new MobDefinition.LootDrop(ItemType.ROTTEN_FLESH, 1, 100),
                        new MobDefinition.LootDrop(ItemType.POISONOUS_POTATO, 1, 20),
                        new MobDefinition.LootDrop(ItemType.CARROT, 1, 20),
                        new MobDefinition.LootDrop(ItemType.POTATO, 1, 20)),
                RegionType.GRAVEYARD);
    }

    public static MobDefinition cryptGhoul() {
        return hostile("Crypt Ghoul", "CRYPT_GHOUL", EntityType.ZOMBIE, 30, 2000, 350, 32,
                List.of(MobType.UNDEAD), Material.ZOMBIE_HEAD,
                List.of(new MobDefinition.LootDrop(ItemType.ROTTEN_FLESH, 1, 100)), RegionType.CRYPTS);
    }

    public static MobDefinition goldenGhoul() {
        return hostile("Golden Ghoul", "GOLDEN_GHOUL", EntityType.ZOMBIE, 60, 45000, 800, 50,
                List.of(MobType.UNDEAD), Material.ZOMBIE_HEAD,
                List.of(new MobDefinition.LootDrop(ItemType.ROTTEN_FLESH, 2, 100)), RegionType.CRYPTS);
    }

    public static MobDefinition hubSkeleton() {
        return hostile("Skeleton", "SKELETON_02", EntityType.SKELETON, 2, 100, 35, 9,
                List.of(MobType.SKELETAL), Material.SKELETON_SKULL,
                List.of(new MobDefinition.LootDrop(ItemType.BONE, 1, 2, 100)), RegionType.SPIDERS_DEN);
    }

    public static MobDefinition splitterSpider() {
        return hostile("Splitter Spider", "SPLITTER_SPIDER_04", EntityType.SPIDER, 4, 220, 40, 0,
                List.of(MobType.ARTHROPOD), Material.SPIDER_EYE,
                List.of(new MobDefinition.LootDrop(ItemType.STRING, 1, 100),
                        new MobDefinition.LootDrop(ItemType.SPIDER_EYE, 1, 100)), RegionType.SPIDERS_DEN);
    }

    public static MobDefinition dasherSpider() {
        return hostile("Dasher Spider", "DASHER_SPIDER_04", EntityType.SPIDER, 4, 170, 55, 10,
                List.of(MobType.ARTHROPOD), Material.SPIDER_EYE, List.of(), RegionType.SPIDERS_DEN);
    }

    public static MobDefinition weaverSpider() {
        return hostile("Weaver Spider", "WEAVER_SPIDER", EntityType.SPIDER, 3, 160, 35, 6,
                List.of(MobType.ARTHROPOD), Material.SPIDER_EYE, List.of(), RegionType.SPIDERS_DEN);
    }

    public static MobDefinition silverfish() {
        return hostile("Silverfish", "SILVERFISH", EntityType.SILVERFISH, 1, 50, 20, 5,
                List.of(MobType.ARTHROPOD), Material.STRING,
                List.of(new MobDefinition.LootDrop(ItemType.STRING, 1, 100)), RegionType.SPIDERS_DEN);
    }

    public static MobDefinition voraciousSpider() {
        return hostile("Voracious Spider", "VORACIOUS_SPIDER", EntityType.SPIDER, 10, 1000, 100, 3,
                List.of(MobType.ARTHROPOD), Material.SPIDER_EYE,
                List.of(new MobDefinition.LootDrop(ItemType.STRING, 1, 100),
                        new MobDefinition.LootDrop(ItemType.SPIDER_EYE, 1, 50)), RegionType.SPIDERS_DEN);
    }

    public static MobDefinition rainSlime() {
        return hostile("Rain Slime", "RAIN_SLIME", EntityType.SLIME, 8, 200, 100, 4,
                List.of(MobType.CUBIC), Material.SLIME_BALL,
                List.of(new MobDefinition.LootDrop(ItemType.SLIME_BALL, 1, 100)), RegionType.SPIDERS_DEN);
    }

    public static MobDefinition enderman(int level, double health, double damage, long combatXp) {
        return new MobDefinition("Enderman", "ENDERMAN_" + level, EntityType.ENDERMAN, level, health, damage, 100,
                combatXp, 10, 12, List.of(MobType.ENDER),
                List.of(new MobDefinition.LootDrop(ItemType.ENDER_PEARL, 1, 3, 100)),
                Material.ENDER_PEARL, 5, 1, RegionType.THE_END, true, false);
    }

    public static MobDefinition endermite(int level, double health, double damage) {
        return hostile("Endermite", "ENDERMITE_" + level, EntityType.ENDERMITE, level, health, damage, 25,
                List.of(MobType.ENDER), Material.ENDERMITE_SPAWN_EGG,
                List.of(new MobDefinition.LootDrop(ItemType.ENDER_PEARL, 1, 100)), RegionType.THE_END);
    }

    public static MobDefinition nestEndermite() {
        return hostile("Nest Endermite", "NEST_ENDERMITE", EntityType.ENDERMITE, 50, 4500, 1000, 0,
                List.of(MobType.ENDER), Material.ENDERMITE_SPAWN_EGG,
                List.of(new MobDefinition.LootDrop(ItemType.ENDER_PEARL, 1, 100)), RegionType.THE_END);
    }

    public static MobDefinition witherSkeleton() {
        return hostile("Wither Skeleton", "WITHER_SKELETON", EntityType.WITHER_SKELETON, 70, 600000, 3000, 120,
                List.of(MobType.WITHER, MobType.SKELETAL), Material.WITHER_SKELETON_SKULL,
                List.of(new MobDefinition.LootDrop(ItemType.BONE, 1, 100)), RegionType.STRONGHOLD);
    }

    public static MobDefinition mushroomBull() {
        return hostile("Mushroom Bull", "MUSHROOM_BULL", EntityType.MOOSHROOM, 80, 2500000, 5000, 120,
                List.of(MobType.ANIMAL), Material.RED_MUSHROOM,
                List.of(new MobDefinition.LootDrop(ItemType.RAW_BEEF, 1, 100),
                        new MobDefinition.LootDrop(ItemType.RED_MUSHROOM, 4, 100),
                        new MobDefinition.LootDrop(ItemType.LEATHER, 1, 100)), RegionType.MYSTIC_MARSH);
    }

    public static MobDefinition packSpirit() {
        return hostile("Pack Spirit", "PACK_SPIRIT", EntityType.WOLF, 30, 6000, 240, 15,
                List.of(MobType.ANIMAL, MobType.SPOOKY), Material.BONE,
                List.of(), RegionType.HOWLING_CAVE);
    }

    public static MobDefinition howlingSpirit() {
        return hostile("Howling Spirit", "HOWLING_SPIRIT", EntityType.WOLF, 35, 7000, 400, 0,
                List.of(MobType.ANIMAL, MobType.SPOOKY), Material.BONE,
                List.of(), RegionType.HOWLING_CAVE);
    }

    public static MobDefinition barbarian() {
        return new MobDefinition("Barbarian", "BARBARIAN", EntityType.PIGLIN, 75, 2000000, 3500, 100,
                120, 0, 0, List.of(MobType.HUMANOID, MobType.INFERNAL), List.of(),
                Material.PIGLIN_SPAWN_EGG, 5, 1, RegionType.CRIMSON_ISLE, true, false);
    }

    private static MobDefinition hostile(String name, String id, EntityType entityType, int level,
                                         double health, double damage, long xp, List<MobType> types,
                                         Material material, List<MobDefinition.LootDrop> drops, RegionType region) {
        return new MobDefinition(name, id, entityType, level, health, damage, 100, xp, 1, 1,
                types, drops, material, 5, 1, region, true, true);
    }
}
