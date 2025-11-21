package net.swofty.anticheat.api.player;

import net.swofty.anticheat.flag.FlagType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BypassManager {
    private final Map<UUID, Map<FlagType, BypassEntry>> bypasses = new ConcurrentHashMap<>();

    public void setBypass(UUID uuid, FlagType flagType, long durationMs) {
        long expiryTime = System.currentTimeMillis() + durationMs;
        bypasses.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .put(flagType, new BypassEntry(expiryTime));
    }

    public void setBypass(UUID uuid, FlagType flagType) {
        bypasses.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .put(flagType, new BypassEntry(-1));
    }

    public void removeBypass(UUID uuid, FlagType flagType) {
        Map<FlagType, BypassEntry> playerBypasses = bypasses.get(uuid);
        if (playerBypasses != null) {
            playerBypasses.remove(flagType);
        }
    }

    public boolean hasBypass(UUID uuid, FlagType flagType) {
        Map<FlagType, BypassEntry> playerBypasses = bypasses.get(uuid);
        if (playerBypasses == null) return false;

        BypassEntry entry = playerBypasses.get(flagType);
        if (entry == null) return false;

        if (entry.expiryTime == -1) return true;

        if (System.currentTimeMillis() < entry.expiryTime) {
            return true;
        } else {
            playerBypasses.remove(flagType);
            return false;
        }
    }

    public void clearBypasses(UUID uuid) {
        bypasses.remove(uuid);
    }

    public void cleanupExpired() {
        long currentTime = System.currentTimeMillis();
        bypasses.values().forEach(playerBypasses ->
            playerBypasses.entrySet().removeIf(entry ->
                entry.getValue().expiryTime != -1 && currentTime >= entry.getValue().expiryTime
            )
        );
        bypasses.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    private static class BypassEntry {
        final long expiryTime;

        BypassEntry(long expiryTime) {
            this.expiryTime = expiryTime;
        }
    }
}
