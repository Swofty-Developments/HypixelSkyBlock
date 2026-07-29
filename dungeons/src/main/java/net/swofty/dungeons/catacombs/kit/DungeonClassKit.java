package net.swofty.dungeons.catacombs.kit;

import net.swofty.dungeons.catacombs.classes.DungeonClassType;

import java.util.List;

public record DungeonClassKit(
        String id,
        String displayName,
        DungeonClassType type,
        List<DungeonKitItem> items
) {
    public DungeonClassKit {
        items = List.copyOf(items);
    }
}
