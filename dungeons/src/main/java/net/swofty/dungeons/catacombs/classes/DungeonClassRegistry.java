package net.swofty.dungeons.catacombs.classes;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public record DungeonClassRegistry(Map<DungeonClassType, DungeonClassDefinition> definitions) {
    public DungeonClassRegistry {
        definitions = Map.copyOf(definitions);
    }

    public DungeonClassDefinition definition(DungeonClassType type) {
        DungeonClassDefinition definition = definitions.get(type);
        if (definition == null) {
            throw new IllegalArgumentException("No dungeon class registered for " + type);
        }
        return definition;
    }

    public static DungeonClassRegistry defaults() {
        Map<DungeonClassType, DungeonClassDefinition> definitions = new EnumMap<>(DungeonClassType.class);
        definitions.put(DungeonClassType.HEALER, new DungeonClassDefinition(DungeonClassType.HEALER,
                "Healing and revive support",
                abilities(
                        passive("HEALING_AURA", "Healing Aura", "Periodically heals nearby teammates"),
                        ultimate("WISH", "Wish", 120, "Heals all teammates and grants absorption"),
                        orb("HEALING_CIRCLE", "Healing Circle", 2, "Creates a healing circle at the caster"),
                        ghost("REVIVE", "Revive", 60, "Revives a dead teammate"))));
        definitions.put(DungeonClassType.MAGE, new DungeonClassDefinition(DungeonClassType.MAGE,
                "Magic damage",
                abilities(
                        passive("MAGE_STAFF", "Mage Staff", "Left click beam attack while holding a dungeon item"),
                        ultimate("THUNDERSTORM", "Thunderstorm", 120, "Strikes nearby enemies with lightning"),
                        orb("GUIDED_SHEEP", "Guided Sheep", 30, "Launches an explosive sheep"),
                        ghost("WALL", "Wall", 60, "Creates a temporary wall"))));
        definitions.put(DungeonClassType.BERSERK, new DungeonClassDefinition(DungeonClassType.BERSERK,
                "Melee damage",
                abilities(
                        passive("BLOODLUST", "Bloodlust", "Kills increase outgoing melee damage briefly"),
                        ultimate("RAGNAROK", "Ragnarok", 120, "Summons allied zombies"),
                        orb("THROWING_AXE", "Throwing Axe", 10, "Throws an axe at a target"),
                        ghost("VENGEFUL_SPIRIT", "Vengeful Spirit", 30, "Damages enemies while dead"))));
        definitions.put(DungeonClassType.ARCHER, new DungeonClassDefinition(DungeonClassType.ARCHER,
                "Ranged damage",
                abilities(
                        passive("DOUBLE_SHOT", "Double Shot", "Arrows have a chance to fire twice"),
                        orb("EXPLOSIVE_SHOT", "Explosive Shot", 40, "Fires an explosive arrow"),
                        ultimate("RAPID_FIRE", "Rapid Fire", 120, "Rapidly fires arrows in the targeted direction"),
                        ghost("STUN_BOW", "Stun Bow", 15, "Stuns a target while dead"))));
        definitions.put(DungeonClassType.TANK, new DungeonClassDefinition(DungeonClassType.TANK,
                "Damage mitigation and enemy control",
                abilities(
                        passive("DIVERSION", "Diversion", "Redirects a portion of teammate damage to the Tank"),
                        orb("SEISMIC_WAVE", "Seismic Wave", 15, "Sends a damaging wave forward"),
                        ultimate("CASTLE_OF_STONE", "Castle of Stone", 120, "Greatly reduces incoming damage"),
                        ghost("ABSORPTION", "Absorption", 60, "Grants teammates absorption while dead"))));
        return new DungeonClassRegistry(definitions);
    }

    private static DungeonClassAbility passive(String id, String displayName, String description) {
        return new DungeonClassAbility(id, displayName, DungeonClassAbilityType.PASSIVE, 0, description);
    }

    private static DungeonClassAbility orb(String id, String displayName, int cooldownSeconds, String description) {
        return new DungeonClassAbility(id, displayName, DungeonClassAbilityType.ORB, cooldownSeconds, description);
    }

    private static DungeonClassAbility ultimate(String id, String displayName, int cooldownSeconds, String description) {
        return new DungeonClassAbility(id, displayName, DungeonClassAbilityType.ULTIMATE, cooldownSeconds, description);
    }

    private static DungeonClassAbility ghost(String id, String displayName, int cooldownSeconds, String description) {
        return new DungeonClassAbility(id, displayName, DungeonClassAbilityType.GHOST, cooldownSeconds, description);
    }

    private static List<DungeonClassAbility> abilities(DungeonClassAbility... abilities) {
        return List.of(abilities);
    }
}
