package net.swofty.dungeons.catacombs.item;

import net.swofty.dungeons.catacombs.classes.DungeonClassAbility;

public record DungeonHotbarSlot(
        int slot,
        DungeonHotbarAction action,
        DungeonClassAbility ability
) {}
