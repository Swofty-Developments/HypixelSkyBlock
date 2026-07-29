package net.swofty.dungeons.catacombs;

import net.swofty.dungeons.catacombs.boss.CatacombsBosses;
import net.swofty.dungeons.catacombs.puzzle.CatacombsPuzzle;
import net.swofty.dungeons.catacombs.run.DungeonRunRules;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record CatacombsRegistry(Map<CatacombsMode, Map<CatacombsFloor, CatacombsFloorDefinition>> floors) {
    public CatacombsRegistry {
        floors = Map.copyOf(floors);
    }

    public CatacombsFloorDefinition floor(CatacombsFloor floor, CatacombsMode mode) {
        Map<CatacombsFloor, CatacombsFloorDefinition> byFloor = floors.get(mode);
        if (byFloor == null || !byFloor.containsKey(floor)) {
            throw new IllegalArgumentException("No Catacombs definition for " + mode + " " + floor.displayName());
        }
        return byFloor.get(floor);
    }

    public static CatacombsRegistry defaults() {
        Map<CatacombsMode, Map<CatacombsFloor, CatacombsFloorDefinition>> floors = new EnumMap<>(CatacombsMode.class);
        floors.put(CatacombsMode.NORMAL, normalFloors());
        floors.put(CatacombsMode.MASTER, masterFloors());
        return new CatacombsRegistry(floors);
    }

    private static Map<CatacombsFloor, CatacombsFloorDefinition> normalFloors() {
        Map<CatacombsFloor, CatacombsFloorDefinition> floors = new EnumMap<>(CatacombsFloor.class);
        floors.put(CatacombsFloor.ENTRANCE, definition(CatacombsFloor.ENTRANCE, CatacombsMode.NORMAL,
                CatacombsBosses.watcher(), 1, 0, 0, 600, true));
        floors.put(CatacombsFloor.FLOOR_ONE, definition(CatacombsFloor.FLOOR_ONE, CatacombsMode.NORMAL,
                CatacombsBosses.bonzo(), 2, 1, 0, 600, true));
        floors.put(CatacombsFloor.FLOOR_TWO, definition(CatacombsFloor.FLOOR_TWO, CatacombsMode.NORMAL,
                CatacombsBosses.scarf(), 2, 1, 0, 600, true));
        floors.put(CatacombsFloor.FLOOR_THREE, definition(CatacombsFloor.FLOOR_THREE, CatacombsMode.NORMAL,
                CatacombsBosses.professor(), 2, 1, 1, 600, false));
        floors.put(CatacombsFloor.FLOOR_FOUR, definition(CatacombsFloor.FLOOR_FOUR, CatacombsMode.NORMAL,
                CatacombsBosses.thorn(), 2, 1, 1, 720, false));
        floors.put(CatacombsFloor.FLOOR_FIVE, definition(CatacombsFloor.FLOOR_FIVE, CatacombsMode.NORMAL,
                CatacombsBosses.livid(), 3, 2, 1, 720, false));
        floors.put(CatacombsFloor.FLOOR_SIX, definition(CatacombsFloor.FLOOR_SIX, CatacombsMode.NORMAL,
                CatacombsBosses.sadan(), 3, 2, 1, 840, false));
        floors.put(CatacombsFloor.FLOOR_SEVEN, definition(CatacombsFloor.FLOOR_SEVEN, CatacombsMode.NORMAL,
                CatacombsBosses.witherLords(false), 3, 2, 1, 840, false));
        return floors;
    }

    private static Map<CatacombsFloor, CatacombsFloorDefinition> masterFloors() {
        Map<CatacombsFloor, CatacombsFloorDefinition> floors = new EnumMap<>(CatacombsFloor.class);
        floors.put(CatacombsFloor.FLOOR_ONE, master(CatacombsFloor.FLOOR_ONE, CatacombsBosses.bonzo(), 2, 1, 0, 540));
        floors.put(CatacombsFloor.FLOOR_TWO, master(CatacombsFloor.FLOOR_TWO, CatacombsBosses.scarf(), 2, 1, 0, 540));
        floors.put(CatacombsFloor.FLOOR_THREE, master(CatacombsFloor.FLOOR_THREE, CatacombsBosses.professor(), 2, 1, 1, 540));
        floors.put(CatacombsFloor.FLOOR_FOUR, master(CatacombsFloor.FLOOR_FOUR, CatacombsBosses.thorn(), 2, 1, 1, 660));
        floors.put(CatacombsFloor.FLOOR_FIVE, master(CatacombsFloor.FLOOR_FIVE, CatacombsBosses.livid(), 3, 2, 1, 660));
        floors.put(CatacombsFloor.FLOOR_SIX, master(CatacombsFloor.FLOOR_SIX, CatacombsBosses.sadan(), 3, 2, 1, 780));
        floors.put(CatacombsFloor.FLOOR_SEVEN, master(CatacombsFloor.FLOOR_SEVEN, CatacombsBosses.witherLords(true), 3, 2, 1, 780));
        return floors;
    }

    private static CatacombsFloorDefinition master(CatacombsFloor floor,
                                                   net.swofty.dungeons.catacombs.boss.CatacombsBossEncounter boss,
                                                   int puzzleRooms, int minibossRooms, int trapRooms,
                                                   int speedScoreSeconds) {
        return definition(floor, CatacombsMode.MASTER, boss, puzzleRooms, minibossRooms, trapRooms,
                speedScoreSeconds, false);
    }

    private static CatacombsFloorDefinition definition(CatacombsFloor floor, CatacombsMode mode,
                                                       net.swofty.dungeons.catacombs.boss.CatacombsBossEncounter boss,
                                                       int puzzleRooms, int minibossRooms, int trapRooms,
                                                       int speedScoreSeconds, boolean automaticGhostRevive) {
        return new CatacombsFloorDefinition(
                floor,
                mode,
                boss,
                new DungeonRunRules(speedScoreSeconds, 4200, 2, 5, 17, automaticGhostRevive, trapRooms > 0),
                puzzleRooms,
                minibossRooms,
                trapRooms,
                puzzlesFor(floor));
    }

    private static Set<CatacombsPuzzle> puzzlesFor(CatacombsFloor floor) {
        return Arrays.stream(CatacombsPuzzle.values())
                .filter(puzzle -> puzzle.canGenerate(floor))
                .collect(Collectors.toUnmodifiableSet());
    }
}
