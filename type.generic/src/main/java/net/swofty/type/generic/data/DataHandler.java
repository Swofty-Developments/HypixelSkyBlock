package net.swofty.type.generic.data;

import lombok.Getter;
import lombok.NonNull;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DataHandler {
    public static Map<UUID, DataHandler> userCache = new HashMap<>();

    @Getter protected UUID uuid;
    protected final Map<String, Datapoint<?>> datapoints = new HashMap<>();

    protected DataHandler() {}
    protected DataHandler(UUID uuid) { this.uuid = uuid; }

    public Datapoint<?> getDatapoint(String key) { return this.datapoints.get(key); }
    public Map<String, Datapoint<?>> getDatapoints() { return this.datapoints; }

    public static @NonNull DataHandler getUser(UUID uuid) {
        if (!userCache.containsKey(uuid)) throw new RuntimeException("User " + uuid + " does not exist!");
        return userCache.get(uuid);
    }

    public static DataHandler getUser(HypixelPlayer player) { return getUser(player.getUuid()); }

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
