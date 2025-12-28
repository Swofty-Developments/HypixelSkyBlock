package net.swofty.type.generic.achievement;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;

@Getter
public enum AchievementCategory {
    BEDWARS("Bed Wars", "bedwars", new GUIMaterial(Material.RED_BED)),
    VAMPIREZ("VampireZ", "vampirez", new GUIMaterial(Material.WITHER_SKELETON_SKULL)),
    SKYWARS("SkyWars", "skywars", new GUIMaterial(Material.ENDER_EYE)),
    BLITZ_SG("Blitz SG", "blitzsg", new GUIMaterial(Material.DIAMOND_SWORD)),
    THE_WALLS("The Walls", "walls", new GUIMaterial(Material.SAND)),
    PAINTBALL("Paintball Warfare", "paintball", new GUIMaterial(Material.SNOWBALL)),
    ARCADE("Arcade Games", "arcade", new GUIMaterial(Material.SLIME_BALL)),
    QUAKECRAFT("Quakecraft", "quakecraft", new GUIMaterial(Material.FIREWORK_ROCKET)),
    MEGA_WALLS("Mega Walls", "megawalls", new GUIMaterial(Material.SOUL_SAND)),
    TNT_GAMES("The TNT Games", "tntgames", new GUIMaterial(Material.TNT)),
    ARENA_BRAWL("Arena Brawl", "arena", new GUIMaterial(Material.BLAZE_POWDER)),
    UHC_CHAMPIONS("UHC Champions", "uhc", new GUIMaterial(Material.GOLDEN_APPLE)),
    WARLORDS("Warlords", "warlords", new GUIMaterial(Material.STONE_AXE)),
    TURBO_KART_RACERS("Turbo Kart Racers", "gingerbread", new GUIMaterial(Material.MINECART)),
    SMASH_HEROES("Smash Heroes", "supersmash", new GUIMaterial("d29a9f57267ed342a13e3ad3a240c4c5af5a1a36ab2de0d6c2a31af0e3cdde")),
    SPEED_UHC("Speed UHC", "speeduhc", new GUIMaterial(Material.GOLDEN_CARROT)),
    MURDER_MYSTERY("Murder Mystery", "murdermystery", new GUIMaterial(Material.BOW)),
    BUILD_BATTLE("Build Battle", "buildbattle", new GUIMaterial(Material.CRAFTING_TABLE)),
    DUELS("Duels", "duels", new GUIMaterial(Material.FISHING_ROD)),
    PIT("Pit", "pit", new GUIMaterial(Material.DIRT)),
    WOOL_GAMES("Wool Games", "woolgames", new GUIMaterial(Material.WHITE_WOOL)),
    COPS_AND_CRIMS("Cops and Crims", "copsandcrims", new GUIMaterial(Material.IRON_BARS)),
    GENERAL("General", "general", new GUIMaterial(Material.NETHER_STAR)),
    SKYBLOCK("SkyBlock", "skyblock", new GUIMaterial("d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8")),
    HOUSING("Housing", "housing", new GUIMaterial(Material.DARK_OAK_DOOR)),
    ;

    private final String displayName;
    private final String configKey;
    private final GUIMaterial material;

    AchievementCategory(String displayName, String configKey, GUIMaterial material) {
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
