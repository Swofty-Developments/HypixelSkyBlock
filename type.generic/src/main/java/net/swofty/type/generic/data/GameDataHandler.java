package net.swofty.type.generic.data;

import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

/**
 * Interface for game-specific data handlers (BedWars, SkyWars, etc.)
 * These are account-wide handlers that can be selectively loaded by servers.
 *
 * To add a new game data handler:
 * 1. Create a class extending DataHandler and implementing GameDataHandler
 * 2. Register it in HypixelGenericLoader via GameDataHandlerRegistry.register()
 * 3. Add to TypeLoader's getAdditionalDataHandlers() for servers that need it
 */
public interface GameDataHandler {

    /**
     * Unique identifier for this handler type (used in logging and registry)
     */
    String getHandlerId();

    /**
     * The cache for this handler type
     */
    Map<UUID, ? extends DataHandler> getCache();

    /**
     * Create handler from document or return null if document doesn't contain handler data
     */
    DataHandler createFromDocument(UUID playerUuid, Document document);

    /**
     * Initialize with default data for a new player
     */
    DataHandler initWithDefaults(UUID playerUuid);

    /**
     * Get the handler for a player (from cache)
     */
    DataHandler getHandler(UUID playerUuid);

    /**
     * Put handler in cache
     */
    void cacheHandler(UUID playerUuid, DataHandler handler);

    /**
     * Remove handler from cache
     */
    void removeFromCache(UUID playerUuid);

    /**
     * Check if document contains data for this handler
     */
    boolean hasDataInDocument(Document document);

    /**
     * Run onLoad callbacks for this handler
     */
    default void runOnLoad(UUID playerUuid, HypixelPlayer player) {
        DataHandler handler = getHandler(playerUuid);
        if (handler != null) {
            handler.runOnLoad(player);
        }
    }

    /**
     * Run onSave callbacks for this handler
     */
    default void runOnSave(UUID playerUuid, HypixelPlayer player) {
        DataHandler handler = getHandler(playerUuid);
        if (handler != null) {
            handler.runOnSave(player);
        }
    }
}
