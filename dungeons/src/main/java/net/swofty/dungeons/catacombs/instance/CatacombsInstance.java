package net.swofty.dungeons.catacombs.instance;

import net.swofty.dungeons.SkyBlockDungeon;
import net.swofty.dungeons.catacombs.CatacombsFloorDefinition;
import net.swofty.dungeons.catacombs.boss.state.BossFightController;
import net.swofty.dungeons.catacombs.kit.DungeonClassKit;
import net.swofty.dungeons.catacombs.run.CatacombsRunState;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public record CatacombsInstance(
        UUID id,
        CatacombsFloorDefinition floor,
        SkyBlockDungeon dungeon,
        CatacombsRunState runState,
        BossFightController bossFight,
        Map<UUID, DungeonClassKit> kits,
        Map<Integer, DungeonRoomEncounter> encounters,
        Path renderedMap
) {
    public CatacombsInstance {
        kits = Map.copyOf(kits);
        encounters = Map.copyOf(encounters);
    }
}
