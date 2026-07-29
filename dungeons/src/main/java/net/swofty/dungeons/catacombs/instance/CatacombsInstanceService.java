package net.swofty.dungeons.catacombs.instance;

import net.swofty.dungeons.DungeonRoomType;
import net.swofty.dungeons.SkyBlockDungeon;
import net.swofty.dungeons.catacombs.CatacombsAPI;
import net.swofty.dungeons.catacombs.CatacombsFloorDefinition;
import net.swofty.dungeons.catacombs.boss.state.BossFightController;
import net.swofty.dungeons.catacombs.kit.DungeonClassKit;
import net.swofty.dungeons.catacombs.mob.DungeonMobDefinition;
import net.swofty.dungeons.catacombs.mob.DungeonMobRole;
import net.swofty.dungeons.catacombs.run.CatacombsRunConfig;
import net.swofty.dungeons.catacombs.run.CatacombsRunState;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CatacombsInstanceService {
    public CatacombsInstance create(CatacombsFloorDefinition floor,
                                    Map<UUID, DungeonClassKit> kits,
                                    Path mapOutput) throws IOException {
        SkyBlockDungeon dungeon = CatacombsAPI.generator(floor).generate().join();
        Map<Integer, DungeonRoomEncounter> encounters = encounters(floor, dungeon);
        CatacombsRunState runState = CatacombsRunState.start(new CatacombsRunConfig(
                floor,
                kits.entrySet().stream().collect(HashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue().type()),
                        HashMap::putAll),
                dungeon.getRooms().size(),
                estimateSecrets(floor, dungeon)));
        Path renderedMap = null;
        if (mapOutput != null) {
            renderedMap = CatacombsAPI.renderMap(dungeon, mapOutput).path();
        }
        return new CatacombsInstance(UUID.randomUUID(), floor, dungeon, runState,
                new BossFightController(floor.boss()), kits, encounters, renderedMap);
    }

    private Map<Integer, DungeonRoomEncounter> encounters(CatacombsFloorDefinition floor, SkyBlockDungeon dungeon) {
        Map<Integer, DungeonRoomEncounter> encounters = new HashMap<>();
        int roomId = 0;
        for (SkyBlockDungeon.DungeonRoom room : dungeon.getRooms().values()) {
            List<DungeonMobDefinition> mobs = switch (room.getRoomType()) {
                case MINI_BOSS -> CatacombsAPI.mobs(floor.floor(), DungeonMobRole.MINIBOSS).stream().limit(1).toList();
                case PUZZLE -> CatacombsAPI.mobs(floor.floor(), DungeonMobRole.PUZZLE).stream().limit(1).toList();
                case EXIT, BLOOD -> CatacombsAPI.mobs(floor.floor(), DungeonMobRole.BLOOD).stream().limit(5).toList();
                case BASE -> CatacombsAPI.mobs(floor.floor(), DungeonMobRole.STARRED).stream().limit(3).toList();
                default -> List.of();
            };
            if (!mobs.isEmpty()) {
                encounters.put(roomId, new DungeonRoomEncounter(roomId, mobs));
            }
            if (room.getRoomType() == DungeonRoomType.EXIT) {
                room.setRoomType(DungeonRoomType.BLOOD);
            }
            roomId++;
        }
        return encounters;
    }

    private int estimateSecrets(CatacombsFloorDefinition floor, SkyBlockDungeon dungeon) {
        int baseRooms = (int) dungeon.getRooms().values().stream()
                .filter(room -> room.getRoomType() == DungeonRoomType.BASE)
                .count();
        return Math.max(5, baseRooms * 4 + floor.puzzleRooms());
    }
}
