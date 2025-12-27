package net.swofty.type.generic.achievement;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public enum AchievementCategory {
    BEDWARS("Bed Wars", "bedwars", Material.RED_BED),
    VAMPIREZ("VampireZ", "vampirez", Material.WITHER_SKELETON_SKULL),
    SKYWARS("SkyWars", "skywars", Material.ENDER_EYE),
    BLITZ_SG("Blitz SG", "blitzsg", Material.DIAMOND_SWORD),
    THE_WALLS("The Walls", "walls", Material.SAND),
    PAINTBALL("Paintball Warfare", "paintball", Material.SNOWBALL),
    ARCADE("Arcade Games", "arcade", Material.SLIME_BALL),
    QUAKECRAFT("Quakecraft", "quakecraft", Material.FIREWORK_ROCKET),
    MEGA_WALLS("Mega Walls", "megawalls", Material.SOUL_SAND),
    TNT_GAMES("The TNT Games", "tntgames", Material.TNT),
    ARENA_BRAWL("Arena Brawl", "arena", Material.BLAZE_POWDER),
    UHC_CHAMPIONS("UHC Champions", "uhc", Material.GOLDEN_APPLE),
    WARLORDS("Warlords", "warlords", Material.STONE_AXE),
    TURBO_KART_RACERS("Turbo Kart Racers", "gingerbread", Material.MINECART),
    SMASH_HEROES("Smash Heroes", "supersmash", Material.GOLDEN_APPLE),
    SPEED_UHC("Speed UHC", "speeduhc", Material.GOLDEN_CARROT),
    MURDER_MYSTERY("Murder Mystery", "murdermystery", Material.BOW),
    BUILD_BATTLE("Build Battle", "buildbattle", Material.CRAFTING_TABLE),
    DUELS("Duels", "duels", Material.FISHING_ROD),
    PIT("Pit", "pit", Material.DIRT),
    WOOL_GAMES("Wool Games", "woolgames", Material.WHITE_WOOL),
    COPS_AND_CRIMS("Cops and Crims", "copsandcrims", Material.IRON_BARS),
    GENERAL("General", "general", Material.NETHER_STAR),
    SKYBLOCK("SkyBlock", "skyblock", Material.PLAYER_HEAD);

    private final String displayName;
    private final String configKey;
    private final Material material;

    AchievementCategory(String displayName, String configKey, Material material) {
        this.displayName = displayName;
        this.configKey = configKey;
        this.material = material;
    }

    public static AchievementCategory fromConfigKey(String key) {
        for (AchievementCategory category : values()) {
            if (category.configKey.equalsIgnoreCase(key)) {
                return category;
            }
        }
        return null;
    }
}
