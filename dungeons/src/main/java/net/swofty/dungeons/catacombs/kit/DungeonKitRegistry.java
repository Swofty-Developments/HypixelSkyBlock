package net.swofty.dungeons.catacombs.kit;

import net.swofty.dungeons.catacombs.classes.DungeonClassType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record DungeonKitRegistry(Map<String, DungeonClassKit> kits) {
    public DungeonKitRegistry {
        kits = Map.copyOf(kits);
    }

    public DungeonClassKit kit(String id) {
        DungeonClassKit kit = kits.get(id);
        if (kit == null) {
            throw new IllegalArgumentException("Unknown dungeon kit " + id);
        }
        return kit;
    }

    public List<DungeonClassKit> kits(DungeonClassType type) {
        return kits.values().stream()
                .filter(kit -> kit.type() == type)
                .toList();
    }

    public static DungeonKitRegistry defaults() {
        List<DungeonClassKit> kits = new ArrayList<>();
        kits.add(kit("HEALER_STARTER", "Healer Starter", DungeonClassType.HEALER,
                item("DUNGEON_STONE", 8),
                item("ORNATE_ZOMBIE_SWORD", 0),
                item("WAND_OF_MENDING", 1),
                item("REVIVE_STONE", 7)));
        kits.add(kit("MAGE_STARTER", "Mage Starter", DungeonClassType.MAGE,
                item("DUNGEON_STONE", 8),
                item("DREADLORD_SWORD", 0),
                item("CONJURING", 1),
                item("DECOY", 7)));
        kits.add(kit("BERSERK_STARTER", "Berserk Starter", DungeonClassType.BERSERK,
                item("DUNGEON_STONE", 8),
                item("ZOMBIE_KNIGHT_SWORD", 0),
                item("FEL_SWORD", 1),
                item("SPIRIT_LEAP", 7)));
        kits.add(kit("ARCHER_STARTER", "Archer Starter", DungeonClassType.ARCHER,
                item("DUNGEON_STONE", 8),
                item("MACHINE_GUN_BOW", 0),
                item("SUPER_UNDEAD_BOW", 1),
                item("DECOY", 7)));
        kits.add(kit("TANK_STARTER", "Tank Starter", DungeonClassType.TANK,
                item("DUNGEON_STONE", 8),
                item("EARTH_SHARD", 0),
                item("SUPERBOOM_TNT", 1),
                item("SPIRIT_LEAP", 7)));
        return new DungeonKitRegistry(kits.stream().collect(Collectors.toMap(DungeonClassKit::id, Function.identity())));
    }

    private static DungeonClassKit kit(String id, String displayName, DungeonClassType type, DungeonKitItem... items) {
        return new DungeonClassKit(id, displayName, type, List.of(items));
    }

    private static DungeonKitItem item(String itemId, int slot) {
        return new DungeonKitItem(itemId, slot, 1);
    }
}
