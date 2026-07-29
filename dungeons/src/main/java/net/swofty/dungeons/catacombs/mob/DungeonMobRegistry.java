package net.swofty.dungeons.catacombs.mob;

import net.swofty.dungeons.catacombs.CatacombsFloor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public record DungeonMobRegistry(Map<String, DungeonMobDefinition> mobs) {
    public DungeonMobRegistry {
        mobs = Map.copyOf(mobs);
    }

    public DungeonMobDefinition mob(String id) {
        DungeonMobDefinition mob = mobs.get(id);
        if (mob == null) {
            throw new IllegalArgumentException("Unknown dungeon mob " + id);
        }
        return mob;
    }

    public List<DungeonMobDefinition> spawnable(CatacombsFloor floor, DungeonMobRole role) {
        return mobs.values().stream()
                .filter(mob -> mob.role() == role)
                .filter(mob -> mob.canSpawn(floor))
                .toList();
    }

    public static DungeonMobRegistry defaults() {
        List<DungeonMobDefinition> mobs = new ArrayList<>();
        mobs.add(mob("BAT", "Bat", CatacombsFloor.ENTRANCE, DungeonMobRole.SECRET, 100, 0, 0));
        mobs.add(mob("CRYPT_UNDEAD", "Crypt Undead", CatacombsFloor.ENTRANCE, DungeonMobRole.NORMAL, 1200, 120, 0));
        mobs.add(mob("UNDEAD_SKELETON", "Undead Skeleton", CatacombsFloor.ENTRANCE, DungeonMobRole.NORMAL, 1000, 100, 0, DungeonMobAbility.BOW));
        mobs.add(mob("SCARED_SKELETON", "Scared Skeleton", CatacombsFloor.ENTRANCE, DungeonMobRole.NORMAL, 1100, 90, 0, DungeonMobAbility.BOW));
        mobs.add(mob("ZOMBIE_GRUNT", "Zombie Grunt", CatacombsFloor.FLOOR_ONE, DungeonMobRole.NORMAL, 2500, 220, 0));
        mobs.add(mob("SKELETON_GRUNT", "Skeleton Grunt", CatacombsFloor.FLOOR_ONE, DungeonMobRole.NORMAL, 2200, 200, 0, DungeonMobAbility.BOW));
        mobs.add(mob("SNIPER", "Sniper", CatacombsFloor.FLOOR_ONE, DungeonMobRole.STARRED, 3500, 350, 0, DungeonMobAbility.BOW));
        mobs.add(mob("CRYPT_DREADLORD", "Crypt Dreadlord", CatacombsFloor.FLOOR_ONE, DungeonMobRole.STARRED, 4500, 420, 0, DungeonMobAbility.MAGIC_PROJECTILE));
        mobs.add(mob("CRYPT_LURKER", "Crypt Lurker", CatacombsFloor.FLOOR_ONE, DungeonMobRole.STARRED, 5000, 380, 0, DungeonMobAbility.INVISIBILITY));
        mobs.add(mob("CELLAR_SPIDER", "Cellar Spider", CatacombsFloor.FLOOR_ONE, DungeonMobRole.NORMAL, 2500, 180, 0));
        mobs.add(mob("LONELY_SPIDER", "Lonely Spider", CatacombsFloor.FLOOR_ONE, DungeonMobRole.NORMAL, 3000, 210, 0));
        mobs.add(mob("LOST_ADVENTURER", "Lost Adventurer", CatacombsFloor.FLOOR_ONE, DungeonMobRole.MINIBOSS, 120000, 1600, 100, DungeonMobAbility.HEALING, DungeonMobAbility.MELEE));
        mobs.add(mob("ANGRY_ARCHAEOLOGIST", "Angry Archaeologist", CatacombsFloor.FLOOR_ONE, DungeonMobRole.MINIBOSS, 100000, 1500, 75, DungeonMobAbility.MELEE));
        mobs.add(mob("SKELETON_SOLDIER", "Skeleton Soldier", CatacombsFloor.FLOOR_ONE, DungeonMobRole.STARRED, 7000, 520, 0, DungeonMobAbility.BOW));
        mobs.add(mob("ZOMBIE_SOLDIER", "Zombie Soldier", CatacombsFloor.FLOOR_TWO, DungeonMobRole.STARRED, 9000, 580, 0));
        mobs.add(mob("SKELETON_MASTER", "Skeleton Master", CatacombsFloor.FLOOR_THREE, DungeonMobRole.STARRED, 12000, 650, 0, DungeonMobAbility.BOW, DungeonMobAbility.TRUE_DAMAGE));
        mobs.add(mob("PUZZLE_BLAZE", "Puzzle Blaze", CatacombsFloor.FLOOR_THREE, DungeonMobRole.PUZZLE, 1500, 0, 0));
        mobs.add(mob("SHADOW_ASSASSIN", "Shadow Assassin", CatacombsFloor.FLOOR_THREE, DungeonMobRole.MINIBOSS, 250000, 2800, 200, DungeonMobAbility.TELEPORT, DungeonMobAbility.INVISIBILITY));
        mobs.add(mob("FROZEN_ADVENTURER", "Frozen Adventurer", CatacombsFloor.FLOOR_FOUR, DungeonMobRole.MINIBOSS, 400000, 3200, 250, DungeonMobAbility.MAGIC_PROJECTILE, DungeonMobAbility.SLOW));
        mobs.add(mob("KING_MIDAS", "King Midas", CatacombsFloor.FLOOR_FIVE, DungeonMobRole.MINIBOSS, 500000, 3500, 300, DungeonMobAbility.MELEE));
        mobs.add(mob("SKELETOR", "Skeletor", CatacombsFloor.FLOOR_FIVE, DungeonMobRole.STARRED, 30000, 900, 120, DungeonMobAbility.BOW));
        mobs.add(mob("SUPER_ARCHER", "Super Archer", CatacombsFloor.FLOOR_FIVE, DungeonMobRole.STARRED, 40000, 1100, 150, DungeonMobAbility.BOW));
        mobs.add(mob("SUPER_TANK_ZOMBIE", "Super Tank Zombie", CatacombsFloor.FLOOR_FIVE, DungeonMobRole.STARRED, 60000, 900, 600, DungeonMobAbility.SHIELD));
        mobs.add(mob("FEL", "Fel", CatacombsFloor.FLOOR_FIVE, DungeonMobRole.NORMAL, 35000, 1300, 100, DungeonMobAbility.TELEPORT));
        mobs.add(mob("DEATHMITE", "Deathmite", CatacombsFloor.FLOOR_FIVE, DungeonMobRole.NORMAL, 10000000, 100000, 0, DungeonMobAbility.TRUE_DAMAGE));
        mobs.add(mob("WATCHER_UNDEAD", "Watcher Undead", CatacombsFloor.ENTRANCE, DungeonMobRole.BLOOD, 5000, 500, 0, DungeonMobAbility.SUMMON_UNDEAD));
        mobs.add(mob("BONZO", "Bonzo", CatacombsFloor.FLOOR_ONE, DungeonMobRole.BOSS, 350000, 1600, 0, DungeonMobAbility.EXPLOSION, DungeonMobAbility.REVIVE));
        mobs.add(mob("SCARF", "Scarf", CatacombsFloor.FLOOR_TWO, DungeonMobRole.BOSS, 1000000, 2500, 0, DungeonMobAbility.SUMMON_UNDEAD));
        mobs.add(mob("PROFESSOR", "The Professor", CatacombsFloor.FLOOR_THREE, DungeonMobRole.BOSS, 2500000, 3000, 0, DungeonMobAbility.SUMMON_UNDEAD));
        mobs.add(mob("THORN", "Thorn", CatacombsFloor.FLOOR_FOUR, DungeonMobRole.BOSS, 3000000, 3500, 0, DungeonMobAbility.SPIRIT_BOW_VULNERABILITY));
        mobs.add(mob("LIVID", "Livid", CatacombsFloor.FLOOR_FIVE, DungeonMobRole.BOSS, 7000000, 5000, 0, DungeonMobAbility.SPLIT_CLONES));
        mobs.add(mob("SADAN", "Sadan", CatacombsFloor.FLOOR_SIX, DungeonMobRole.BOSS, 40000000, 12000, 0, DungeonMobAbility.SUMMON_UNDEAD));
        mobs.add(mob("MAXOR", "Maxor", CatacombsFloor.FLOOR_SEVEN, DungeonMobRole.BOSS, 75000000, 15000, 0, DungeonMobAbility.WITHER_SKULL));
        mobs.add(mob("STORM", "Storm", CatacombsFloor.FLOOR_SEVEN, DungeonMobRole.BOSS, 75000000, 15000, 0, DungeonMobAbility.WITHER_SKULL));
        mobs.add(mob("GOLDOR", "Goldor", CatacombsFloor.FLOOR_SEVEN, DungeonMobRole.BOSS, 75000000, 15000, 0, DungeonMobAbility.TERMINAL_OBJECTIVE));
        mobs.add(mob("NECRON", "Necron", CatacombsFloor.FLOOR_SEVEN, DungeonMobRole.BOSS, 100000000, 20000, 0, DungeonMobAbility.WITHER_SKULL, DungeonMobAbility.CHARGE));
        mobs.add(mob("WITHER_KING", "Wither King", CatacombsFloor.FLOOR_SEVEN, DungeonMobRole.BOSS, 1000000000L, 50000, 0, DungeonMobAbility.WITHER_SKULL));
        return new DungeonMobRegistry(mobs.stream().collect(Collectors.toMap(DungeonMobDefinition::id, Function.identity())));
    }

    private static DungeonMobDefinition mob(String id, String displayName, CatacombsFloor minimumFloor,
                                            DungeonMobRole role, long health, long damage, int defense,
                                            DungeonMobAbility... abilities) {
        return new DungeonMobDefinition(id, displayName, minimumFloor, role, health, damage, defense, Set.of(abilities));
    }
}
