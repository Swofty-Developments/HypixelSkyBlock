package net.swofty.type.murdermysterygame.maphandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapHandlerRegistry {
    private static final Map<String, MapHandler> handlers = new HashMap<>();

    public static void register(MapHandler handler) {
        handlers.put(handler.getMapId().toLowerCase(), handler);
    }

    public static Optional<MapHandler> getHandler(String mapId) {
        if (mapId == null) return Optional.empty();
        return Optional.ofNullable(handlers.get(mapId.toLowerCase()));
    }

    public static void clear() {
        handlers.clear();
    }
}
