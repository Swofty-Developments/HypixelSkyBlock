package net.swofty.type.generic.collectibles;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class CollectibleUnlockResolverRegistry {

    private static final Map<String, CollectibleUnlockResolver> RESOLVERS = new ConcurrentHashMap<>();

    private CollectibleUnlockResolverRegistry() {
    }

    public static void register(String key, CollectibleUnlockResolver resolver) {
        if (key == null || key.isBlank() || resolver == null) {
            return;
        }
        RESOLVERS.put(key.trim().toLowerCase(), resolver);
    }

    public static Optional<CollectibleUnlockResolver> get(String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(RESOLVERS.get(key.trim().toLowerCase()));
    }

    public static void clear() {
        RESOLVERS.clear();
    }
}
