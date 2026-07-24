package net.swofty.type.generic.data.domain;

import net.swofty.commons.ServerType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ArrivalContext {
    private static final Map<UUID, ServerType> origins = new ConcurrentHashMap<>();

    private ArrivalContext() {}

    public static void put(UUID uuid, ServerType origin) {
        if (origin != null) origins.put(uuid, origin);
    }

    public static ServerType consume(UUID uuid) {
        return origins.remove(uuid);
    }

    public static ServerType peek(UUID uuid) {
        return origins.get(uuid);
    }
}
