package net.swofty.type.lobby.game;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.lobby.ServerInfoCache;

@Getter
public enum GameType {
    DISASTERS("Disasters §c§lRELEASED TO ARCADE!", Material.LAVA_BUCKET, GameType.Category.CASUAL_GAMES,
        null, // arcade lobby
        "§7From a zombie apocalypse to meteor",
        "§7showers, work alone or with friends",
        "§7to survive!"),

    WOOL_GAMES("Wool Games", Material.WHITE_WOOL, Category.COMPETITIVE,
        null, // wool games lobby
        "§7A series of team-based PvP games",
        "§7with wool as the theme!"),

    SKYBLOCK("SkyBlock §2§l0.24.3 §b§lABIPHONE CONTACTS & JERRY'S WORKSHOP",
        ItemStackCreator.getStackHead("686718d85e25b006f2c8f160f619b23c8fd6ae75ddf1c06308ec0f539d931703"),
        Category.PERSISTENT_GAME,
        ServerType.SKYBLOCK_ISLAND,
        "§7SkyBlock has finally arrived on",
        "§7Hypixel! Play with friends (or solo!),",
        "§7build your private islands and",
        "§7collect all the items!"),

    BED_WARS("Bed Wars",
        Material.RED_BED,
        Category.TEAM_SURVIVAL,
        ServerType.BEDWARS_LOBBY,
        "§7Protect your bed along with your",
        "§7teammates and destroy enemy beds",
        "§7to win!"),

    SKYWARS("SkyWars §d§lOLD EMBLEMS + QOL CHANGES",
        Material.ENDER_EYE,
        Category.SURVIVAL,
        ServerType.SKYWARS_LOBBY,
        "§7Hypixel's take on the SkyWars",
        "§7gamemode. Featuring the angel of",
        "§7Death, Soul Well, and §cINSANE MODE§7!",
        "§7Play on your own or in teams."
    ),

    MURDER_MYSTERY("Murder Mystery",
        Material.BOW,
        Category.TEAM_SURVIVAL,
        ServerType.MURDER_MYSTERY_LOBBY,
        "§71 Murderer. 1 Detective. And a whole",
        "§7lot of Innocents. Can you survive",
        "§7this tense social game of betrayal",
        "§7and murder?"),

    HOUSING("Housing",
        Material.DARK_OAK_DOOR,
        Category.HOUSING,
        null,
        "§7Customize and build on your own",
        "§7personal plot, hang out with your",
        "§7friends, visit other people's houses,",
        "§7and more!"),

    THE_TNT_GAMES("The TNT Games",
        Material.TNT,
        Category.CASUAL_GAMES,
        null,
        "§7Fun minigames with TNT involved!"),

    BUILD_BATTLE("Build Battle",
        Material.CRAFTING_TABLE,
        Category.CASUAL_GAMES,
        null,
        "§7Create a build based on a theme in",
        "§7just 5 minutes! Vote on competing",
        "§7builds with ratings ranging from",
        "§7\"Super-Poop\" to \"Legendary\". Get",
        "§7the most votes out of 16 players to",
        "§7win!"),

    DUELS("Duels", Material.FISHING_ROD, Category.COMPETITIVE,
        null,
        "§7Quick paced 1v1, 2v2, 4v4!",
        "§f∙ UHC Duels",
        "§f∙ SkyWars Duels",
        "§f∙ The Bridge",
        "§f∙ Sumo Duels",
        "§f∙ OP Duels",
        "§f∙ Classic Duels",
        "§f∙ NoDebuff Duels",
        "§f∙ Blitz Duels",
        "§f∙ Combo Duels",
        "§f∙ Bow Duels",
        "§f∙ Spleef Duels",
        "§f∙ Mega Walls Duels",
        "§f∙ Boxing Duels",
        "§f∙ Parkour Duels",
        "§f∙ Bed Wars Duels",
        "§f∙ Quakecraft Duels"),

    PROTOTYPE("Prototype §c§lDISASTERS RELEASED TO ARCADE!",
        Material.ANVIL,
        Category.CASUAL_GAMES,
        ServerType.PROTOTYPE_LOBBY,
        "§7PTL is a place for testing fun and",
        "§7creative new minigames and systems",
        "§7on Hypixel.",
        " ",
        "§cEverything in this lobby is currently",
        "§cin development, and may be removed",
        "§cat any time."),
    ;

    private final String displayName;
    private final ItemStack.Builder item;
    private final Category category;
    private final ServerType lobbyType;
    private final String[] lore;

    GameType(String displayName, ItemStack.Builder item, Category category, ServerType lobbyType, String... lore) {
        this.displayName = displayName;
        this.item = item;
        this.category = category;
        this.lobbyType = lobbyType;
        this.lore = lore;
    }

    GameType(String displayName, Material item, Category category, ServerType lobbyType, String... lore) {
        this.displayName = displayName;
        this.item = ItemStackCreator.getStack("", item, 1);
        this.category = category;
        this.lobbyType = lobbyType;
        this.lore = lore;
    }

    /**
     * Check if this game type is implemented and playable.
     */
    public boolean isImplemented() {
        return lobbyType != null;
    }

    /**
     * Get the total player count for this game type.
     * For SkyBlock, counts all SkyBlock server types.
     * For other games, counts players in the lobby type.
     */
    public int getPlayerCount() {
        if (!isImplemented()) return 0;

        if (this == SKYBLOCK) {
            return ServerInfoCache.getTotalSkyBlockPlayers();
        }

        return ServerInfoCache.getTotalPlayersForType(lobbyType);
    }

    public enum Category {
        PROTOTYPE_GAME,
        PERSISTENT_GAME,
        SURVIVAL,
        TEAM_SURVIVAL,
        COMPETITIVE,
        CASUAL_GAMES,
        HOUSING
    }
}
