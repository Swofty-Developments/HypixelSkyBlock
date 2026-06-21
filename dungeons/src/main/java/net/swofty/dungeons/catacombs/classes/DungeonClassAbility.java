package net.swofty.dungeons.catacombs.classes;

public record DungeonClassAbility(
        String id,
        String displayName,
        DungeonClassAbilityType type,
        int cooldownSeconds,
        String description
) {}
