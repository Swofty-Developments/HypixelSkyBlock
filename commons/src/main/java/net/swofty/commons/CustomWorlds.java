package net.swofty.commons;

public enum CustomWorlds {
	SKYBLOCK_ISLAND_TEMPLATE("hypixel_skyblock_island_template"),
	SKYBLOCK_HUB("hypixel_skyblock_hub"),
	SKYBLOCK_SPIDERS_DEN("hypixel_skyblock_spiders_den"),
	SKYBLOCK_THE_END("hypixel_skyblock_the_end"),
	SKYBLOCK_CRIMSON_ISLE("hypixel_skyblock_crimson_isle"),
	SKYBLOCK_GOLD_MINE("hypixel_skyblock_gold_mine"),
	SKYBLOCK_DEEP_CAVERNS("hypixel_skyblock_deep_caverns"),
	SKYBLOCK_DWARVEN_MINES("hypixel_skyblock_dwarven_mines"),
	SKYBLOCK_THE_PARK("hypixel_skyblock_the_park"),
	SKYBLOCK_GALATEA("hypixel_skyblock_galatea"),
	SKYBLOCK_BACKWATER_BAYOU("hypixel_skyblock_backwater_bayou"),
	SKYBLOCK_JERRYS_WORKSHOP("hypixel_skyblock_jerrys_workshop"),
	SKYBLOCK_DUNGEON_HUB("hypixel_skyblock_dungeon_hub"),
	PROTOTYPE_LOBBY("hypixel_prototype_lobby"),
	BEDWARS_LOBBY("hypixel_bedwars_lobby"),
	MURDER_MYSTERY_LOBBY("hypixel_murder_mystery_lobby"),
	SKYWARS_LOBBY("hypixel_skywars_lobby"),
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
