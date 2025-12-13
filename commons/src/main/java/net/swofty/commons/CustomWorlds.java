package net.swofty.commons;

public enum CustomWorlds {
	SKYBLOCK_ISLAND_TEMPLATE("hypixel_skyblock_island_template"),
	SKYBLOCK_HUB("hypixel_skyblock_hub"),
	SKYBLOCK_GOLD_MINE("hypixel_skyblock_gold_mine"),
	SKYBLOCK_DEEP_CAVERNS("hypixel_skyblock_deep_caverns"),
	SKYBLOCK_DUNGEON_HUB("hypixel_skyblock_dungeon_hub"),
	PROTOTYPE_LOBBY("hypixel_prototype_lobby"),
	BEDWARS_LOBBY("hypixel_bedwars_lobby"),
	;

	private final String folderName;

	CustomWorlds(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderName() {
		if (name().startsWith("SKYBLOCK_")) {
			return "./configuration/skyblock/islands/" + folderName;
		} else {
			return "./configuration/" + folderName;
		}
	}
}
