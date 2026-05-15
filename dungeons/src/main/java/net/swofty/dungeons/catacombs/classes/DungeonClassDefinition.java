package net.swofty.dungeons.catacombs.classes;

import java.util.List;

public record DungeonClassDefinition(
        DungeonClassType type,
        String role,
        List<DungeonClassAbility> abilities
) {
    public DungeonClassDefinition {
        abilities = List.copyOf(abilities);
    }

    public List<DungeonClassAbility> abilities(DungeonClassAbilityType type) {
        return abilities.stream()
                .filter(ability -> ability.type() == type)
                .toList();
    }
}
