package net.swofty.type.skyblockgeneric.region;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.commons.Songs;
import net.swofty.type.skyblockgeneric.region.biome.*;
import net.swofty.type.skyblockgeneric.region.mining.configurations.*;
import net.swofty.type.skyblockgeneric.region.mining.configurations.deepmines.*;
import net.swofty.type.skyblockgeneric.region.mining.configurations.thepark.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum RegionType {
	PRIVATE_ISLAND("Your Island", "§a"),

	VILLAGE("Village", WheatAndFlowersConfiguration.class),
	BANK("Bank", "§6"),
	LIBRARY("Library"),
	AUCTION_HOUSE("Auction House", "§6"),
	FLOWER_HOUSE("Flower House"),
	BAZAAR_ALLEY("Bazaar Alley", "§e"),
	COMMUNITY_CENTER("Community Center"),
	BUILDERS_HOUSE("Builder's House"),
	MOUNTAIN("Mountain"),
	WILDERNESS("Wilderness", "§2", Songs.WILDERNESS),
	RUINS("Ruins"),
	COLOSSEUM("Colosseum"),
	GRAVEYARD("Graveyard", "§c"),
	COAL_MINE("Coal Mine", MineCoalConfiguration.class),
	HIGH_LEVEL("High Level", "§4"),
	ARCHERY_RANGE("Archery Range", "§9"),
	BLACKSMITH("Blacksmith"),
	FARM("Farm", MineWheatConfiguration.class),
	DARK_AUCTION("Dark Auction", "§5"),
	FOREST("Forest", MineLogsConfiguration.class),

	BIRCH_PARK("Birch Park", "§a", BirchParkConfiguration.class, BirchParkBiome.class),
	HOWLING_CAVE("Howling Cave", null, BirchParkBiome.class),
	SPRUCE_WOODS("Spruce Woods", "§a", SpruceWoodsConfiguration.class, SpruceWoodsBiome.class),
	VIKING_LONGHOUSE("Viking Longhouse", "§b", SpruceWoodsConfiguration.class, SpruceWoodsBiome.class),
	DARK_THICKET("Dark Thicket", "§a", DarkOakConfiguration.class, DarkThicketBiome.class),
	TRIALS_OF_FIRE("Trials of Fire", "§c", null, DarkThicketBiome.class),
	SAVANNA_WOODLAND("Savanna Woodland", "§a", SavannaWoodlandConfiguration.class),
	MELODY_PLATEAU("Melody's Plateau", "§d", SavannaWoodlandConfiguration.class),
	JUNGLE_ISLAND("Jungle Island", "§a", JungleIslandConfiguration.class),

	JERRYS_WORKSHOP("Jerry's Workshop", "§c"),
	JERRY_POND("Jerry Pond", "§b"),
	SUNKEN_JERRY_POND("Sunken Jerry Pond", "§b"),
	TERRYS_SHACK("Terry's Shack", "§b"),
	MOUNT_JERRY("Mount Jerry", "§c"),
	HOT_SPRINGS("Hot Springs", "§4"),
	GLACIAL_CAVE("Glacial Cave", "§3", GlacialCaveConfiguration.class),
	GARYS_SHACK("Gary's Shack", "§b"),
	SHERRYS_SHOWROOM("Sherry's Showroom", "§e"),
	EINARYS_EMPORIUM("Einary's Emporium", "§6"),

	THE_BARN("The Barn", "§b", BarnConfiguration.class),
	MUSHROOM_DESERT("Mushroom Desert"),
	DESERT_SETTLEMENT("Desert Settlement", "§e"),
	OASIS("Oasis"),
	SHEPHERD_KEEP("Shepherd's Keep"),
	TRAPPERS_DEN("Trapper's Den"),
	JAKE_HOUSE("Jake's House"),
	MUSHROOM_GORGE("Mushroom Gorge"),
	OVERGROWN_MUSHROOM_CAVE("Overgrown Mushroom Cave", "§2"),
	GLOWING_MUSHROOM_CAVE("Glowing Mushroom Cave", "§3"),

	SPIDERS_DEN("Spider's Den", "§4"),
	SPIDERS_DEN_HIVE("Spider's Den", "§4"),
	BLAZING_FORTRESS("Blazing Fortress", "§4"),
	THE_END("The End", "§d"),
	THE_END_NEST("The End", "§d"),
	ARCHAEOLOGICAL_SITE("Archaeological Site", "§a"),
	BURNING_BRIDGE("Burning Bridge", "§4"),
	VOID_SEPULTURE("Void Sepulture", "§d"),
	DRAGONS_NEST("Dragon's Nest", "§5"),

	GOLD_MINE("Gold Mine", "§6", GoldMineConfiguration.class),
	DEEP_CAVERNS("Deep Caverns", "§b"),
	GUNPOWDER_MINES("Gunpowder Mines", GunpowderMinesConfiguration.class),
	LAPIS_QUARRY("Lapis Quarry", LapisQuarryConfiguration.class),
	PIGMENS_DEN("Pigmen's Den", PigmensDenConfiguration.class),
	SLIMEHILL("Slimehill", SlimehillConfiguration.class),
	DIAMOND_RESERVE("Diamond Reserve", DiamondReserveConfiguration.class),
	OBSIDIAN_SANCTUARY("Obsidian Sanctuary", ObsidianSanctuaryConfiguration.class),

	DWARVEN_VILLAGE("Dwarven Village", DwarvenMinesConfiguration.class),
	DWARVEN_MINES("Dwarven Mines", "§2", DwarvenMinesConfiguration.class),
	GOBLIN_BURROWS("Goblin Burrows", DwarvenMinesConfiguration.class),
	THE_MIST("The Mist", "§8", DwarvenMinesConfiguration.class),
	GREAT_ICE_WALL("Great Ice Wall", DwarvenMinesConfiguration.class),
	GATES_TO_THE_MINES("Gates to the Mines", DwarvenMinesConfiguration.class),
	RAMPARTS_QUARRY("Rampart's Quarry", DwarvenMinesConfiguration.class),
	FORGE_BASIN("Forge Basin", DwarvenMinesConfiguration.class),
	THE_FORGE("The Forge", DwarvenMinesConfiguration.class),
	CLIFFSIDE_VEINS("Cliffside Veins", DwarvenMinesConfiguration.class),
	ROYAL_MINES("Royal Mines", DwarvenMinesConfiguration.class),
	UPPER_MINES("Upper Mines", DwarvenMinesConfiguration.class),
	LAVA_SPRINGS("Lava Springs", DwarvenMinesConfiguration.class),
	DIVANS_GATEWAY("Divan's Gateway", DwarvenMinesConfiguration.class),
	FAR_REACH("Far Reserve", DwarvenMinesConfiguration.class),
	PALACE_BRIDGE("Palace Bridge", DwarvenMinesConfiguration.class),
	ROYAL_PALACE("Royal Palace", DwarvenMinesConfiguration.class),
	ARISTOCRAT_PASSAGE("Aristocrat's Passage", DwarvenMinesConfiguration.class),
	FAR_RESERVE("Far Reserve", DwarvenMinesConfiguration.class);

	private final String name;
	private final String color;
	private final SkyBlockRegenConfiguration miningHandler;
	private final SkyBlockBiomeConfiguration biomeHandler;
	private final List<Songs> songs;

	@SneakyThrows
	RegionType(String name, String color, Class<? extends SkyBlockRegenConfiguration> miningHandler, Class<? extends SkyBlockBiomeConfiguration> biomeHandler, Songs... songs) {
		this.name = name;
		this.color = color;

		if (miningHandler != null)
			this.miningHandler = miningHandler.getDeclaredConstructor().newInstance();
		else
			this.miningHandler = null;

		if (biomeHandler != null)
			this.biomeHandler = biomeHandler.getDeclaredConstructor().newInstance();
		else
			this.biomeHandler = null;

		this.songs = new ArrayList<>();
	}

	RegionType(String name, String color, Class<? extends SkyBlockRegenConfiguration> miningHandler) {
		this(name, color, miningHandler, null, new Songs[0]);
	}

	RegionType(String name, Class<? extends SkyBlockRegenConfiguration> miningHandler) {
		this(name, "§b", miningHandler);
	}


	RegionType(String name, Class<? extends SkyBlockRegenConfiguration> miningHandler, Class<? extends SkyBlockBiomeConfiguration> biomeHandler) {
		this(name, "§b", miningHandler, biomeHandler);
	}

	RegionType(String name, String color) {
		this(name, color, new Songs[0]);
	}

	RegionType(String name, String color, Songs... songs) {
		this.name = name;
		this.color = color;
		this.miningHandler = null;
		this.songs = new ArrayList<>(List.of(songs));
		this.biomeHandler = null;
	}

	RegionType(String name) {
		this(name, "§b", new Songs[0]);
	}

	public static RegionType getByID(int id) {
		return RegionType.values()[id];
	}

	@SneakyThrows
	public SkyBlockRegenConfiguration getMiningHandler() {
		return miningHandler;
	}

	@SneakyThrows
	public SkyBlockBiomeConfiguration getBiomeHandler() {
		return biomeHandler;
	}

	@Override
	public String toString() {
		return color + name;
	}
}