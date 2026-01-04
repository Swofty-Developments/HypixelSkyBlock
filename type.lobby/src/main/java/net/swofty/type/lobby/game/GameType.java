package net.swofty.type.lobby.game;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.lobby.ServerInfoCache;

@Getter
public enum GameType {
    SKYBLOCK("SkyBlock §2§l0.24 §a§lTHE GREENHOUSE",
            ItemStackCreator.getStackHead("686718d85e25b006f2c8f160f619b23c8fd6ae75ddf1c06308ec0f539d931703"),
            Category.PERSISTENT_GAME,
            ServerType.SKYBLOCK_ISLAND,
            "§7SkyBlock has finally arrived on",
            "§7Hypixel! Play with friends (or solo!),",
            "§7build your private islands and",
            "§7collect all the items!"),

    BED_WARS("Bed Wars §6§lv1.10 - DREAM MODE & QOL",
            Material.RED_BED,
            Category.TEAM_SURVIVAL,
            ServerType.BEDWARS_LOBBY,
            "§7Protect your bed along with your",
            "§7teammates and destroy enemy beds",
            "§7to win!"),

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

    MURDER_MYSTERY("Murder Mystery",
            Material.BOW,
            Category.TEAM_SURVIVAL,
            ServerType.MURDER_MYSTERY_LOBBY,
            "§71 Murderer. 1 Detective. And a whole",
            "§7lot of Innocents. Can you survive",
            "§7this tense social game of betrayal",
            "§7and murder?"),

    SKYWARS("SkyWars §d§lOLD EMBLEMS + QOL CHANGES",
            Material.ENDER_EYE,
            Category.SURVIVAL,
            ServerType.SKYWARS_LOBBY,
            "§7Hypixel's take on the SkyWars",
            "§7gamemode. Featuring the angel of",
            "§7Death, Soul Well, and §cINSANE MODE§7!",
            "§7Play on your own or in teams."
            ),
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
    }
}
