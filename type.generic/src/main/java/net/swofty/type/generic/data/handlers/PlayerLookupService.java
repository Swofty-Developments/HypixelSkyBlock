package net.swofty.type.generic.data.handlers; //TODO all this should be moved to a ban package

import net.minestom.server.entity.Player;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.UUID;

/**
 * Centralized UUID/name lookup using what you already have.
 */
public final class PlayerLookupService {

    public UUID resolveUuid(String name) {
        // Online pass: your proxy scan is already in commands; keep that there.
        // Here we only do the Mongo/offline part via HypixelDataHandler.
        return HypixelDataHandler.getPotentialUUIDFromName(name);
    }

    public String resolveName(UUID uuid) {
        // Prefer online if present (fast), else Mongo
        try {
            return HypixelPlayer.getRawName(uuid);
        } catch (Exception ignored) {
        }
        return HypixelDataHandler.getPotentialIGNFromUUID(uuid);
    }

    public UUID uuidOf(Player p) {
        return p.getUuid();
    }
}
