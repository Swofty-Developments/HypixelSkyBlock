package net.swofty.anticheat.api;

import lombok.Getter;
import net.swofty.anticheat.api.modifier.CustomModifierRegistry;
import net.swofty.anticheat.api.player.BypassManager;
import net.swofty.anticheat.api.player.PlayerDataRegistry;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.prediction.modifier.FrictionModifier;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

import java.util.UUID;

/**
 * Main API entry point for interacting with the Swofty Anticheat.
 *
 * This API allows you to:
 * - Register custom movement modifiers
 * - Store and retrieve custom player data
 * - Manage check bypasses
 * - Hook into detection events
 */
public class AnticheatAPI {

    @Getter
    private static final CustomModifierRegistry modifierRegistry = new CustomModifierRegistry();

    @Getter
    private static final PlayerDataRegistry playerDataRegistry = new PlayerDataRegistry();

    @Getter
    private static final BypassManager bypassManager = new BypassManager();

    /**
     * Register a custom velocity modifier that will be applied during movement prediction.
     *
     * @param modifier The custom velocity modifier
     */
    public static void registerVelocityModifier(VelocityModifier modifier) {
        modifierRegistry.registerVelocityModifier(modifier);
    }

    /**
     * Register a custom friction modifier that will be applied during movement prediction.
     *
     * @param modifier The custom friction modifier
     */
    public static void registerFrictionModifier(FrictionModifier modifier) {
        modifierRegistry.registerFrictionModifier(modifier);
    }

    /**
     * Unregister a custom velocity modifier.
     *
     * @param modifierClass The class of the modifier to unregister
     */
    public static void unregisterVelocityModifier(Class<? extends VelocityModifier> modifierClass) {
        modifierRegistry.unregisterVelocityModifier(modifierClass);
    }

    /**
     * Unregister a custom friction modifier.
     *
     * @param modifierClass The class of the modifier to unregister
     */
    public static void unregisterFrictionModifier(Class<? extends FrictionModifier> modifierClass) {
        modifierRegistry.unregisterFrictionModifier(modifierClass);
    }

    /**
     * Store custom data associated with a player.
     * This data persists as long as the player is online.
     *
     * @param uuid The player's UUID
     * @param key The data key
     * @param value The data value
     */
    public static void setPlayerData(UUID uuid, String key, Object value) {
        playerDataRegistry.setData(uuid, key, value);
    }

    /**
     * Retrieve custom data associated with a player.
     *
     * @param uuid The player's UUID
     * @param key The data key
     * @return The data value, or null if not found
     */
    public static Object getPlayerData(UUID uuid, String key) {
        return playerDataRegistry.getData(uuid, key);
    }

    /**
     * Remove custom data associated with a player.
     *
     * @param uuid The player's UUID
     * @param key The data key
     */
    public static void removePlayerData(UUID uuid, String key) {
        playerDataRegistry.removeData(uuid, key);
    }

    /**
     * Clear all custom data for a player.
     *
     * @param uuid The player's UUID
     */
    public static void clearPlayerData(UUID uuid) {
        playerDataRegistry.clearData(uuid);
    }

    /**
     * Set a temporary bypass for a specific check for a player.
     * The bypass will automatically expire after the specified duration.
     *
     * @param uuid The player's UUID
     * @param flagType The flag type to bypass
     * @param durationMs Duration in milliseconds
     */
    public static void setBypass(UUID uuid, FlagType flagType, long durationMs) {
        bypassManager.setBypass(uuid, flagType, durationMs);
    }

    /**
     * Set a permanent bypass for a specific check for a player.
     *
     * @param uuid The player's UUID
     * @param flagType The flag type to bypass
     */
    public static void setBypass(UUID uuid, FlagType flagType) {
        bypassManager.setBypass(uuid, flagType);
    }

    /**
     * Remove a bypass for a specific check for a player.
     *
     * @param uuid The player's UUID
     * @param flagType The flag type to remove bypass for
     */
    public static void removeBypass(UUID uuid, FlagType flagType) {
        bypassManager.removeBypass(uuid, flagType);
    }

    /**
     * Check if a player has a bypass for a specific check.
     *
     * @param uuid The player's UUID
     * @param flagType The flag type to check
     * @return true if the player has an active bypass
     */
    public static boolean hasBypass(UUID uuid, FlagType flagType) {
        return bypassManager.hasBypass(uuid, flagType);
    }

    /**
     * Clear all bypasses for a player.
     *
     * @param uuid The player's UUID
     */
    public static void clearBypasses(UUID uuid) {
        bypassManager.clearBypasses(uuid);
    }
}
