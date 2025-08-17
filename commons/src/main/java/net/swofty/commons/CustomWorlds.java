package net.swofty.commons;

public enum CustomWorlds {
    ISLANDS_TEMPLATE("hypixel_island_template"),
    SKYBLOCK_HUB("hypixel_skyblock_hub"),
    DUNGEON_HUB("hypixel_dungeon_hub"),
    PROTOTYPE_LOBBY("hypixel_prototype_lobby"),
    BEDWARS_LOBBY("hypixel_bedwars_lobby"),
    ;

    private final String folderName;

    CustomWorlds(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return "./configuration/" + folderName;
    }
}
