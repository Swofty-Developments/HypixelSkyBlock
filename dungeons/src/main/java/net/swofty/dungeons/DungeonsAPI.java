package net.swofty.dungeons;

public class DungeonsAPI {
    /**
     * Get a new instance of the {@link DungeonsData} class.
     * @param data the data to use for the new instance.
     * @return a new instance of the {@link DungeonsData} class.
     */
    public static GeneratorService getGeneratorService(DungeonsData data) {
        // Validate DungeonsData has been properly filled
        for (DungeonRoomType room : DungeonRoomType.values()) {
            if (room.isRequiresData() && data.getDataForOrNull(room) == null) {
                throw new IllegalArgumentException("DungeonsData must contain data for all data-required rooms.");
            }
        }

        return new GeneratorService(data);
    }
}
