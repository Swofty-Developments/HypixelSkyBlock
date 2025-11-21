package net.swofty.anticheat.api.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataRegistry {
    private final Map<UUID, Map<String, Object>> playerData = new ConcurrentHashMap<>();

    public void setData(UUID uuid, String key, Object value) {
        playerData.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>()).put(key, value);
    }

    public Object getData(UUID uuid, String key) {
        Map<String, Object> data = playerData.get(uuid);
        return data != null ? data.get(key) : null;
    }

    public <T> T getData(UUID uuid, String key, Class<T> type) {
        Object data = getData(uuid, key);
        if (type.isInstance(data)) {
            return type.cast(data);
        }
        return null;
    }

    public void removeData(UUID uuid, String key) {
        Map<String, Object> data = playerData.get(uuid);
        if (data != null) {
            data.remove(key);
        }
    }

    public void clearData(UUID uuid) {
        playerData.remove(uuid);
    }

    public Map<String, Object> getAllData(UUID uuid) {
        Map<String, Object> data = playerData.get(uuid);
        return data != null ? new HashMap<>(data) : new HashMap<>();
    }

    public boolean hasData(UUID uuid, String key) {
        Map<String, Object> data = playerData.get(uuid);
        return data != null && data.containsKey(key);
    }
}
