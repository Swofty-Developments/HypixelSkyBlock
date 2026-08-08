package net.swofty.type.skyblockgeneric.entity.mob.impl;

import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.region.RegionType;

import java.util.List;

public final class PrivateIslandMobDefinitions {
    private PrivateIslandMobDefinitions() {
    }

    public static MobDefinition zombie(int level, String id) {
        return hostile("Zombie", id, EntityType.ZOMBIE, level, 6, 1,
                List.of(MobType.UNDEAD), Material.ZOMBIE_HEAD,
                List.of(new MobDefinition.LootDrop(ItemType.ROTTEN_FLESH, 1, 100),
                        new MobDefinition.LootDrop(ItemType.POISONOUS_POTATO, 1, 2),
                        new MobDefinition.LootDrop(ItemType.POTATO, 1, 1),
                        new MobDefinition.LootDrop(ItemType.CARROT, 1, 1)),
                5, 1);
    }

    public static MobDefinition skeleton(int level) {
        return hostile("Skeleton", "SKELETON", EntityType.SKELETON, level, 0, 1,
                List.of(MobType.SKELETAL), Material.SKELETON_SKULL,
                List.of(new MobDefinition.LootDrop(ItemType.BONE, 1, 2, 100)), 5, 1);
    }

    public static MobDefinition spider(int level) {
        return hostile("Spider", "SPIDER", EntityType.SPIDER, level, 0, 1,
                List.of(MobType.ARTHROPOD), Material.SPIDER_EYE,
                List.of(new MobDefinition.LootDrop(ItemType.STRING, 1, 100),
                        new MobDefinition.LootDrop(ItemType.SPIDER_EYE, 1, 50)), 5, 1);
    }

    public static MobDefinition creeper() {
        return hostileWithOrbs("Creeper", "CREEPER", EntityType.CREEPER, 1, 0, 2, 2,
                List.of(MobType.CUBIC), Material.CREEPER_HEAD,
                List.of(new MobDefinition.LootDrop(ItemType.GUNPOWDER, 1, 100)), 5, 1);
    }

    public static MobDefinition enderman(int level) {
        return neutral("Enderman", "ENDERMAN", EntityType.ENDERMAN, level,
                List.of(MobType.ENDER), Material.ENDER_PEARL, List.of(), 5, 1);
    }

    public static MobDefinition witch(int level) {
        return hostileWithOrbs("Witch", "WITCH", EntityType.WITCH, level, 0, 1, 4,
                List.of(MobType.HUMANOID, MobType.ARCANE), Material.POTION,
                List.of(new MobDefinition.LootDrop(ItemType.GUNPOWDER, 1, 50),
                        new MobDefinition.LootDrop(ItemType.GLOWSTONE_DUST, 1, 50),
                        new MobDefinition.LootDrop(ItemType.GLASS_BOTTLE, 2, 20)), 5, 1);
    }

    public static MobDefinition cow() {
        return passive("Cow", "COW", EntityType.COW, Material.COW_SPAWN_EGG,
                List.of(new MobDefinition.LootDrop(ItemType.RAW_BEEF, 1, 100),
                        new MobDefinition.LootDrop(ItemType.LEATHER, 1, 100)), List.of(MobType.ANIMAL));
    }

    public static MobDefinition pig() {
        return passive("Pig", "PIG", EntityType.PIG, Material.PIG_SPAWN_EGG,
                List.of(new MobDefinition.LootDrop(ItemType.RAW_PORKCHOP, 1, 100)), List.of(MobType.ANIMAL));
    }

    public static MobDefinition chicken() {
        return passive("Chicken", "CHICKEN", EntityType.CHICKEN, Material.CHICKEN_SPAWN_EGG,
                List.of(new MobDefinition.LootDrop(ItemType.FEATHER, 1, 100),
                        new MobDefinition.LootDrop(ItemType.RAW_CHICKEN, 1, 100),
                        new MobDefinition.LootDrop(ItemType.EGG, 1, 30)), List.of(MobType.ANIMAL));
    }

    public static MobDefinition sheep() {
        return passive("Sheep", "SHEEP", EntityType.SHEEP, Material.SHEEP_SPAWN_EGG,
                List.of(new MobDefinition.LootDrop(ItemType.MUTTON, 1, 100),
                        new MobDefinition.LootDrop(ItemType.WHITE_WOOL, 1, 100)), List.of(MobType.ANIMAL));
    }

    public static MobDefinition horse() {
        return passive("Horse", "HORSE", EntityType.HORSE, Material.SADDLE, List.of(), List.of(MobType.ANIMAL));
    }

    public static MobDefinition bat() {
        return new MobDefinition("Bat", "BAT", EntityType.BAT, 3, 6, 0, 100,
                33, 100, 100, List.of(MobType.ANIMAL, MobType.AIRBORNE),
                List.of(new MobDefinition.LootDrop(ItemType.BAT_TALISMAN, 1, 1)),
                Material.BAT_SPAWN_EGG, 5, 1, RegionType.PRIVATE_ISLAND, false, false);
    }

    private static MobDefinition hostile(String name, String id, EntityType entityType, int level,
                                         long combatXp, int coins, List<MobType> types, Material material,
                                         List<MobDefinition.LootDrop> drops, int tier, int bracket) {
        return hostileWithOrbs(name, id, entityType, level, combatXp, coins, 1, types, material, drops, tier, bracket);
    }

    private static MobDefinition hostileWithOrbs(String name, String id, EntityType entityType, int level,
                                                 long combatXp, int coins, int xpOrbs, List<MobType> types,
                                                 Material material, List<MobDefinition.LootDrop> drops, int tier, int bracket) {
        return new MobDefinition(name, id, entityType, level, 1, 1, 100, combatXp, coins, xpOrbs,
                types, drops, material, tier, bracket, RegionType.PRIVATE_ISLAND, true, true);
    }

    private static MobDefinition neutral(String name, String id, EntityType entityType, int level,
                                         List<MobType> types, Material material,
                                         List<MobDefinition.LootDrop> drops, int tier, int bracket) {
        return new MobDefinition(name, id, entityType, level, 1, 1, 100, 0, 1, 1,
                types, drops, material, tier, bracket, RegionType.PRIVATE_ISLAND, true, false);
    }

    private static MobDefinition passive(String name, String id, EntityType entityType, Material material,
                                         List<MobDefinition.LootDrop> drops, List<MobType> types) {
        return new MobDefinition(name, id, entityType, 1, 50, 0, 100, 3, 0, 1,
                types, drops, material, 5, 1, RegionType.PRIVATE_ISLAND, false, false);
    }
}
