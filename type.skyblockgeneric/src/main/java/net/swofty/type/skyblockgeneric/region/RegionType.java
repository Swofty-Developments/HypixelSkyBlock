package net.swofty.type.skyblockgeneric.region;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.commons.Songs;
import net.swofty.type.skyblockgeneric.region.mining.configurations.*;

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

    BIRCH_PARK("Birch Park", "§a"),
    HOWLING_CAVE("Howling Cave"),
    SPRUCE_WOODS("Spruce Woods", "§a"),
    DARK_THICKET("Dark Thicket", "§a"),
    SAVANNA_WOODLAND("Savanna Woodland", "§a"),
    MELODY_PLATEAU("Melody's Plateau", "§5"),
    JUNGLE_ISLAND("Jungle Island", "§a"),

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
    DEEP_CAVERN("Deep Caverns", "§b"),
    GUNPOWDER_MINES("Gunpowder Mines"),
    LAPIS_QUARRY("Lapis Quarry"),
    PIGMENS_DEN("Pigmen's Den"),
    SLIMEHILL("Slimehill"),
    DIAMOND_RESERVE("Diamond Reserve"),
    OBSIDIAN_SANCTUARY("Obsidian Sanctuary"),

    DWARVEN_VILLAGE("Dwarven Village"),
    DWARVEN_MINES("Dwarven Mines", "§2"),
    GOBLIN_BURROWS("Goblin Burrows"),
    THE_MIST("The Mist", "§8"),
    GREAT_ICE_WALL("Great Ice Wall"),
    GATES_TO_THE_MINES("Gates to the Mines"),
    RAMPARTS_QUARRY("Rampart's Quarry"),
    FORGE_BASIN("Forge Basin"),
    THE_FORGE("The Forge"),
    CLIFFSIDE_VEINS("Cliffside Veins"),
    ROYAL_MINES("Royal Mines"),
    DIVANS_GATEWAY("Divan's Gateway"),
    FAR_RESERVE("Far Reserve");

    private final String name;
    private final String color;
    private final SkyBlockMiningConfiguration miningHandler;
    private final List<Songs> songs;

    @SneakyThrows
    RegionType(String name, String color, Class<? extends SkyBlockMiningConfiguration> miningHandler, Songs... songs) {
        this.name = name;
        this.color = color;

        if (miningHandler != null)
            this.miningHandler = miningHandler.newInstance();
        else
            this.miningHandler = null;
        this.songs = new ArrayList<>();
    }

    RegionType(String name, String color, Class<? extends SkyBlockMiningConfiguration> miningHandler) {
        this(name, color, miningHandler, new Songs[0]);
    }

    RegionType(String name, Class<? extends SkyBlockMiningConfiguration> miningHandler) {
        this(name, "§b", miningHandler);
    }

    RegionType(String name, String color) {
        this(name, color, new Songs[0]);
    }

    RegionType(String name, String color, Songs... songs) {
        this.name = name;
        this.color = color;
        this.miningHandler = null;
        this.songs = new ArrayList<>(List.of(songs));
    }

    RegionType(String name) {
        this(name, "§b", new Songs[0]);
    }

    @SneakyThrows
    public SkyBlockMiningConfiguration getMiningHandler() {
        return miningHandler;
    }

    @Override
    public String toString() {
        return color + name;
    }

    public static RegionType getByID(int id) {
        return RegionType.values()[id];
    }
}