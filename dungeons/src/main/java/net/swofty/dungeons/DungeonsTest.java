package net.swofty.dungeons;

public class DungeonsTest {
    public static void main(String[] args) {
        DungeonsData data = new DungeonsData(5, 5)
                .with(DungeonRoomType.FAIRY, new DungeonsData.RoomData(1, 1))
                .with(DungeonRoomType.PUZZLE, new DungeonsData.RoomData(1, 3))
                .with(DungeonRoomType.MINI_BOSS, new DungeonsData.RoomData(1, 2));

        GeneratorService generatorService = DungeonsAPI.getGeneratorService(data);
        SkyBlockDungeon dungeon = generatorService.generate().join();

        System.out.println("Generated dungeon: \n" + dungeon);
        System.out.println(System.currentTimeMillis() - generatorService.getGenerationStartTime());
    }
}
