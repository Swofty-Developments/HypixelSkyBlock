package net.swofty.dungeons.catacombs.item;

import net.swofty.dungeons.catacombs.CatacombsFloor;
import net.swofty.dungeons.catacombs.classes.DungeonClassAbilityType;
import net.swofty.dungeons.catacombs.classes.DungeonClassDefinition;

import java.util.List;

public record DungeonOrbProfile(
        String itemId,
        String displayName,
        boolean requiredForAbilities,
        List<CatacombsFloor> mortGrantFloors,
        List<DungeonHotbarSlot> livingSlots,
        List<DungeonHotbarSlot> ghostSlots
) {
    public DungeonOrbProfile {
        mortGrantFloors = List.copyOf(mortGrantFloors);
        livingSlots = List.copyOf(livingSlots);
        ghostSlots = List.copyOf(ghostSlots);
    }

    public static DungeonOrbProfile forClass(DungeonClassDefinition definition) {
        List<DungeonHotbarSlot> livingSlots = definition.abilities().stream()
                .filter(ability -> ability.type() == DungeonClassAbilityType.ORB
                        || ability.type() == DungeonClassAbilityType.ULTIMATE)
                .map(ability -> new DungeonHotbarSlot(
                        ability.type() == DungeonClassAbilityType.ULTIMATE ? 8 : 7,
                        ability.type() == DungeonClassAbilityType.ULTIMATE
                                ? DungeonHotbarAction.DROP_KEY_ULTIMATE
                                : DungeonHotbarAction.DROP_STACK_KEY_ABILITY,
                        ability))
                .toList();
        List<DungeonHotbarSlot> ghostSlots = definition.abilities(DungeonClassAbilityType.GHOST).stream()
                .map(ability -> new DungeonHotbarSlot(0, DungeonHotbarAction.GHOST_HOTBAR_SLOT, ability))
                .toList();
        return new DungeonOrbProfile(
                "DUNGEON_STONE",
                definition.type().displayName() + " Dungeon Orb",
                false,
                List.of(CatacombsFloor.ENTRANCE, CatacombsFloor.FLOOR_ONE, CatacombsFloor.FLOOR_TWO),
                livingSlots,
                ghostSlots);
    }
}
