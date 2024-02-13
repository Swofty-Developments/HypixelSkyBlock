package net.swofty.dungeons;

public class DungeonsTest {
    public static void main(String[] args) {
        DungeonsData data = new DungeonsData(5, 5)
                .with(DungeonRooms.FAIRY, new DungeonsData.RoomData(1, 1))
                .with(DungeonRooms.PUZZLE, new DungeonsData.RoomData(1, 3))
                .with(DungeonRooms.MINI_BOSS, new DungeonsData.RoomData(1, 2));

        GeneratorService generatorService = DungeonsAPI.getGeneratorService(data);
        generatorService.generate().join();
    }
}
