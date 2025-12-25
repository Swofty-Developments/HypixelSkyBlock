package net.swofty.type.lobby.game;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.type.lobby.ServerInfoCache;

/**
 * Enum representing all game types available in the game menu.
 * Only games with a non-null lobbyType are implemented and playable.
 */
@Getter
public enum GameType {
    SKYBLOCK("SkyBlock", Material.GRASS_BLOCK, ServerType.SKYBLOCK_ISLAND,
            "§7Collect resources and become the",
            "§7richest player on the server!",
            "",
            "§7Build your island, farm resources,",
            "§7fight dragons, and more!"),

    BED_WARS("Bed Wars", Material.RED_BED, ServerType.BEDWARS_LOBBY,
            "§7Protect your bed and destroy",
            "§7the enemy beds. Upgrade yourself",
            "§7and your team by collecting Iron,",
            "§7Gold, Emerald and Diamond from",
            "§7generators to access powerful",
            "§7upgrades."),

    // Non-functional placeholders
    SKYWARS("SkyWars", Material.ENDER_EYE, null,
            "§cComing Soon!"),

    MURDER_MYSTERY("Murder Mystery", Material.BOW, null,
            "§cComing Soon!"),

    BUILD_BATTLE("Build Battle", Material.CRAFTING_TABLE, null,
            "§cComing Soon!"),

    DUELS("Duels", Material.IRON_SWORD, null,
            "§cComing Soon!"),

    ARCADE("Arcade Games", Material.SLIME_BALL, null,
            "§cComing Soon!");

    private final String displayName;
    private final Material material;
    private final ServerType lobbyType;
    private final String[] lore;

    GameType(String displayName, Material material, ServerType lobbyType, String... lore) {
        this.displayName = displayName;
        this.material = material;
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
}
