package net.swofty.velocity.bedwars;

import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BedWarsPreferenceStore {
    private static final Map<UUID, JSONObject> store = new ConcurrentHashMap<>();

    public static void put(UUID uuid, JSONObject preference) {
        store.put(uuid, preference);
    }

    public static JSONObject take(UUID uuid) {
        return store.remove(uuid);
    }
}
