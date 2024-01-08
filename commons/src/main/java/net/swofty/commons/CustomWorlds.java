package net.swofty.commons;

import lombok.Getter;

public enum CustomWorlds {
    ISLANDS_TEMPLATE("hypixel_island_template"),
    HUB("hypixel_hub"),
    ;

    private final String folderName;

    CustomWorlds(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return "./configuration/" + folderName;
    }
}
