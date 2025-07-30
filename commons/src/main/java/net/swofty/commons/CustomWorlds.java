package net.swofty.commons;

public enum CustomWorlds {
    ISLANDS_TEMPLATE("hypixel_island_template"),
    HUB("hypixel_hub"),
    DUNGEON_HUB("hypixel_dungeon_hub"),
    ;

    private final String folderName;

    CustomWorlds(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return "./configuration/" + folderName;
    }
}
