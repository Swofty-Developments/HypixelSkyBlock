package net.swofty.dungeons.catacombs.mob;

import net.swofty.dungeons.catacombs.CatacombsFloor;

import java.util.Set;

public record DungeonMobDefinition(
        String id,
        String displayName,
        CatacombsFloor minimumFloor,
        DungeonMobRole role,
        long health,
        long damage,
        int defense,
        Set<DungeonMobAbility> abilities
) {
    public DungeonMobDefinition {
        abilities = Set.copyOf(abilities);
    }

    public boolean canSpawn(CatacombsFloor floor) {
        return floor.ordinal() >= minimumFloor.ordinal();
    }

    public DungeonMobDefinition scale(double healthMultiplier, double damageMultiplier, double defenseMultiplier) {
        return new DungeonMobDefinition(
                id,
                displayName,
                minimumFloor,
                role,
                Math.round(health * healthMultiplier),
                Math.round(damage * damageMultiplier),
                (int) Math.round(defense * defenseMultiplier),
                abilities);
    }
}
