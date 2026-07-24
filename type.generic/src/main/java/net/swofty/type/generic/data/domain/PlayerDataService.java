package net.swofty.type.generic.data.domain;

import net.swofty.commons.ServerType;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.LockSupport;

public final class PlayerDataService {
    private static final List<PlayerDataDomain<?>> registered = new CopyOnWriteArrayList<>();
    private static final Map<String, Map<UUID, DataHandler>> caches = new ConcurrentHashMap<>();

    private PlayerDataService() {}

    public static void register(PlayerDataDomain<?> domain) {
        registered.add(domain);
        registered.sort(Comparator.comparingInt(PlayerDataDomain::order));
        caches.computeIfAbsent(domain.key().id(), k -> new ConcurrentHashMap<>());
    }

    private static Map<UUID, DataHandler> cache(DomainKey<?> key) {
        return caches.computeIfAbsent(key.id(), k -> new ConcurrentHashMap<>());
    }

    public static void store(DomainKey<?> key, UUID uuid, DataHandler handler) {
        cache(key).put(uuid, handler);
    }

    public static void evict(DomainKey<?> key, UUID uuid) {
        cache(key).remove(uuid);
    }

    public static <H extends DataHandler> H get(DomainKey<H> key, UUID uuid) {
        DataHandler handler = cache(key).get(uuid);
        if (handler == null) throw new RuntimeException("User " + uuid + " does not exist!");
        return key.type().cast(handler);
    }

    public static <H extends DataHandler> Optional<H> find(DomainKey<H> key, UUID uuid) {
        return Optional.ofNullable(cache(key).get(uuid)).map(key.type()::cast);
    }

    public static boolean isLoaded(DomainKey<?> key, UUID uuid) {
        return cache(key).containsKey(uuid);
    }

    public static <H extends DataHandler> H await(DomainKey<H> key, UUID uuid, long timeoutMillis) {
        Map<UUID, DataHandler> cache = cache(key);
        DataHandler handler = cache.get(uuid);
        if (handler != null) return key.type().cast(handler);

        long deadline = System.nanoTime() + timeoutMillis * 1_000_000L;
        long sleepNanos = 1_000_000L;
        while (System.nanoTime() < deadline) {
            LockSupport.parkNanos(sleepNanos);
            if (Thread.currentThread().isInterrupted()) {
                Thread.currentThread().interrupt();
                return null;
            }
            handler = cache.get(uuid);
            if (handler != null) return key.type().cast(handler);
            sleepNanos = Math.min(sleepNanos * 2, 16_000_000L);
        }
        return null;
    }

    static List<PlayerDataDomain<?>> active(ServerType type) {
        List<PlayerDataDomain<?>> out = new ArrayList<>();
        for (PlayerDataDomain<?> domain : registered) {
            if (domain.appliesTo(type)) out.add(domain);
        }
        return out;
    }

    public static void loadAll(ServerType type, UUID uuid) {
        for (PlayerDataDomain<?> domain : active(type)) domain.load(uuid);
    }

    public static void attachAll(ServerType type, HypixelPlayer player) {
        for (PlayerDataDomain<?> domain : active(type)) domain.attach(player);
    }

    public static void applyAll(ServerType type, HypixelPlayer player) {
        for (PlayerDataDomain<?> domain : active(type)) domain.applyToPlayer(player);
    }

    public static void saveAndUnloadAll(ServerType type, HypixelPlayer player) {
        for (PlayerDataDomain<?> domain : active(type)) {
            domain.save(player);
            domain.unload(player.getUuid());
        }
    }

    public static void prepare(ServerType type, UUID uuid, ServerType originType) {
        ArrivalContext.put(uuid, originType);
        for (PlayerDataDomain<?> domain : active(type)) domain.load(uuid);
    }
}
