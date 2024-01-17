package net.swofty.types.generic.region;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.types.generic.region.mining.configurations.MineCoalConfiguration;
import net.swofty.types.generic.region.mining.configurations.MineWheatConfiguration;

@Getter
public enum RegionType {
    PRIVATE_ISLAND("Your Island", "§a"),
    VILLAGE("Village", MineWheatConfiguration.class),
    MOUNTAIN("Mountain"),
    FOREST("Forest"),
    FARM("Farm", MineWheatConfiguration.class),
    RUINS("Ruins"),
    COLOSSEUM("Colosseum"),
    GRAVEYARD("Graveyard", "§c"),
    COAL_MINE("Coal Mine", MineCoalConfiguration.class),
    COAL_MINE_CAVES("Coal Mine"),
    WILDERNESS("Wilderness", "§2"),
    HIGH_LEVEL("High Level", "§4"),
    BAZAAR_ALLEY("Bazaar Alley", "§e"),
    AUCTION_HOUSE("Auction House", "§6"),
    ARCHERY_RANGE("Archery Range", "§9"),
    BANK("Bank", "§6"),
    BLACKSMITH("Blacksmith"),
    LIBRARY("Library"),
    THE_BARN("The Barn", "§b"),
    MUSHROOM_DESERT("Mushroom Desert"),
    GOLD_MINE("Gold Mine", "§6"),
    DEEP_CAVERN("Deep Caverns", "§b"),
    GUNPOWDER_MINES("Gunpowder Mines"),
    LAPIS_QUARRY("Lapis Quarry"),
    PIGMENS_DEN("Pigmen's Den"),
    SLIMEHILL("Slimehill"),
    BIRCH_PARK("Birch Park", "§a"),
    SPRUCE_WOODS("Spruce Woods", "§a"),
    DARK_THICKET("Dark Thicket", "§a"),
    SAVANNA_WOODLAND("Savanna Woodland", "§a"),
    JUNGLE_ISLAND("Jungle Island", "§a"),
    HOWLING_CAVE("Howling Cave"),
    DIAMOND_RESERVE("Diamond Reserve"),
    OBSIDIAN_SANCTUARY("Obsidian Sanctuary"),
    SPIDERS_DEN("Spider's Den", "§4"),
    COMMUNITY_CENTER("Community Center"),
    SPIDERS_DEN_HIVE("Spider's Den", "§4"),
    BLAZING_FORTRESS("Blazing Fortress", "§4"),
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
    FAR_RESERVE("Far Reserve"),
    THE_END("The End", "§d"),
    THE_END_NEST("The End", "§d"),
    DESERT_SETTLEMENT("Desert Settlement", "§e"),
    OASIS("Oasis"),
    ARCHAEOLOGICAL_SITE("Archaeological Site", "§a"),
    MUSHROOM_GORGE("Mushroom Gorge"),
    OVERGROWN_MUSHROOM_CAVE("Overgrown Mushroom Cave", "§2"),
    GLOWING_MUSHROOM_CAVE("Glowing Mushroom Cave", "§3"),
    BURNING_BRIDGE("Burning Bridge", "§4"),
    VOID_SEPULTURE("Void Sepulture", "§d"),
    DRAGONS_NEST("Dragon's Nest", "§5");

    private final String name;
    private final String color;
    private final SkyBlockMiningConfiguration miningHandler;

    @SneakyThrows
    RegionType(String name, String color, Class<? extends SkyBlockMiningConfiguration> miningHandler) {
        this.name = name;
        this.color = color;

        if (miningHandler != null)
            this.miningHandler = miningHandler.newInstance();
        else
            this.miningHandler = null;
    }

    RegionType(String name, Class<? extends SkyBlockMiningConfiguration> miningHandler) {
        this(name, "§b", miningHandler);
    }

    RegionType(String name, String color) {
        this(name, color, null);
    }

    RegionType(String name) {
        this(name, "§b", null);
    }

    @SneakyThrows
    public SkyBlockMiningConfiguration getMiningHandler() {
        return miningHandler;
    }

    public static RegionType getByID(int id) {
        return RegionType.values()[id];
    }
}