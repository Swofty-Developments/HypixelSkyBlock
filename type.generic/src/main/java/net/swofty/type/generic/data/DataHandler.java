package net.swofty.type.generic.data;

import lombok.Getter;
import lombok.NonNull;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

@Getter
public abstract class DataHandler {
    public static Map<UUID, DataHandler> userCache = new ConcurrentHashMap<>();

    protected UUID uuid;
    protected final Map<String, Datapoint<?>> datapoints = new ConcurrentHashMap<>();

    protected DataHandler() {}
    protected DataHandler(UUID uuid) { this.uuid = uuid; }

    public Datapoint<?> getDatapoint(String key) { return this.datapoints.get(key); }

    public static @NonNull DataHandler getUser(UUID uuid) {
        DataHandler handler = userCache.get(uuid);
        if (handler == null) throw new RuntimeException("User " + uuid + " does not exist!");
        return handler;
    }

    public static DataHandler getUser(HypixelPlayer player) { return getUser(player.getUuid()); }

    /**
     * Non-throwing lookup. Prefer this in code paths that may run before
     * {@code ActionPlayerDataLoad} has populated the cache, e.g. early
     * disconnect handling or events flagged {@code requireDataLoaded = false}.
     */
    public static Optional<DataHandler> findUser(UUID uuid) {
        return Optional.ofNullable(userCache.get(uuid));
    }

    public static Optional<DataHandler> findUser(HypixelPlayer player) {
        return findUser(player.getUuid());
    }

    /**
     * Brief polling wait for the cache entry to appear. Addresses the race
     * where a downstream listener fires between {@code AsyncPlayerConfiguration}
     * and the moment {@code ActionPlayerDataLoad} writes into {@code userCache}.
     * Returns empty if the data is still missing after the timeout.
     */
    public static Optional<DataHandler> awaitUser(UUID uuid, long timeoutMillis) {
        DataHandler handler = userCache.get(uuid);
        if (handler != null) return Optional.of(handler);

        final long deadline = System.nanoTime() + timeoutMillis * 1_000_000L;
        long sleepNanos = 1_000_000L; // 1ms, grows up to ~16ms
        while (System.nanoTime() < deadline) {
            LockSupport.parkNanos(sleepNanos);
            if (Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
                return Optional.empty();
            }
            handler = userCache.get(uuid);
            if (handler != null) return Optional.of(handler);
            sleepNanos = Math.min(sleepNanos * 2, 16_000_000L);
        }
        return Optional.empty();
    }

    public abstract DataHandler fromDocument(Document document);
    public abstract Document toDocument();
    public abstract void runOnLoad(HypixelPlayer player);
    public abstract void runOnSave(HypixelPlayer player);

    public static DataHandler initUserWithDefaultData(UUID uuid) {
        throw new UnsupportedOperationException("Must be implemented by subclass");
    }

    public static @Nullable UUID getPotentialUUIDFromName(String name) throws RuntimeException {
        throw new UnsupportedOperationException("Implement if name lookup is supported");
    }
}
