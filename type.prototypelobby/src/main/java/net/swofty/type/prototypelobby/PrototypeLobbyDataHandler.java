package net.swofty.type.prototypelobby;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.datapoints.DatapointInteger;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PrototypeLobbyDataHandler extends DataHandler {

    // Prototype Lobby specific cache
    public static final Map<UUID, PrototypeLobbyDataHandler> prototypeLobbyCache = new HashMap<>();

    protected PrototypeLobbyDataHandler() { super(); }
    public PrototypeLobbyDataHandler(UUID uuid) { super(uuid); }

    public static PrototypeLobbyDataHandler getUser(UUID uuid) {
        if (!prototypeLobbyCache.containsKey(uuid)) throw new RuntimeException("User " + uuid + " does not exist!");
        return prototypeLobbyCache.get(uuid);
    }

    public static @Nullable PrototypeLobbyDataHandler getUser(HypixelPlayer player) {
        try { return getUser(player.getUuid()); } catch (Exception e) { return null; }
    }

    @Override
    public PrototypeLobbyDataHandler fromDocument(Document document) {
        if (document == null) return initUserWithDefaultData(this.uuid);

        this.uuid = UUID.fromString(document.getString("_id"));
        for (Data data : Data.values()) {
            String key = data.getKey();
            if (!document.containsKey(key)) {
                this.datapoints.put(key, data.getDefaultDatapoint().setUser(this).setData(data));
                continue;
            }
            String jsonValue = document.getString(key);
            try {
                Datapoint<?> dp = data.getDefaultDatapoint().getClass()
                        .getDeclaredConstructor(String.class).newInstance(key);
                dp.deserializeValue(jsonValue);
                this.datapoints.put(key, dp.setUser(this).setData(data));
            } catch (Exception e) {
                this.datapoints.put(key, data.getDefaultDatapoint().setUser(this).setData(data));
                Logger.info("Issue with datapoint " + key + " for user " + this.uuid + " - defaulting");
                e.printStackTrace();
            }
        }
        return this;
    }

    public static PrototypeLobbyDataHandler createFromDocument(Document document) {
        PrototypeLobbyDataHandler h = new PrototypeLobbyDataHandler();
        return h.fromDocument(document);
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.put("_owner", this.uuid.toString());
        for (Data data : Data.values()) {
            try {
                document.put(data.getKey(), getDatapoint(data.getKey()).getSerializedValue());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return document;
    }

    /** Untyped convenience getter. */
    public Datapoint<?> get(Data datapoint) {
        Datapoint<?> dp = this.datapoints.get(datapoint.key);
        return dp != null ? dp : datapoint.defaultDatapoint;
    }

    /** Optionally typed getter (casts to the class you pass). */
    @SuppressWarnings("unchecked")
    public <R extends Datapoint<?>> R get(Data datapoint, Class<R> type) {
        Datapoint<?> dp = this.datapoints.get(datapoint.key);
        return (R) (dp != null ? type.cast(dp) : type.cast(datapoint.defaultDatapoint));
    }

    @Override
    public void runOnLoad(HypixelPlayer player) {
        for (Data data : Data.values()) {
            if (data.onLoad != null) {
                data.onLoad.accept(player, get(data));
            }
        }
    }

    @Override
    public void runOnSave(HypixelPlayer player) {
        for (Data data : Data.values()) {
            if (data.onQuit != null) {
                Datapoint<?> produced = data.onQuit.apply(player);
                Datapoint<?> target = get(data);
                target.setFrom(produced); // no onChange during save
            }
        }
    }

    public static PrototypeLobbyDataHandler initUserWithDefaultData(UUID uuid) {
        PrototypeLobbyDataHandler h = new PrototypeLobbyDataHandler();
        h.uuid = uuid;
        for (Data data : Data.values()) {
            try {
                h.datapoints.put(
                        data.getKey(),
                        data.getDefaultDatapoint().deepClone().setUser(h).setData(data)
                );
            } catch (Exception e) {
                Logger.error("Issue with datapoint " + data.getKey() + " for user " + uuid + " - fix");
                e.printStackTrace();
            }
        }
        return h;
    }

    /** Prototype lobby specific data enum. */
    public enum Data {
        HYPE("hype", DatapointInteger.class, new DatapointInteger("hype", 200));

        @Getter private final String key;
        @Getter private final Class<? extends Datapoint<?>> type;
        @Getter private final Datapoint<?> defaultDatapoint;
        public final BiConsumer<HypixelPlayer, Datapoint<?>> onChange;
        public final BiConsumer<HypixelPlayer, Datapoint<?>> onLoad;
        public final Function<HypixelPlayer, Datapoint<?>> onQuit;

        Data(String key,
             Class<? extends Datapoint<?>> type,
             Datapoint<?> defaultDatapoint,
             BiConsumer<HypixelPlayer, Datapoint<?>> onChange,
             BiConsumer<HypixelPlayer, Datapoint<?>> onLoad,
             Function<HypixelPlayer, Datapoint<?>> onQuit) {
            this.key = key; this.type = type; this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange; this.onLoad = onLoad; this.onQuit = onQuit;
        }
        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint,
             BiConsumer<HypixelPlayer, Datapoint<?>> onChange, BiConsumer<HypixelPlayer, Datapoint<?>> onLoad) {
            this(key, type, defaultDatapoint, onChange, onLoad, null);
        }
        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint,
             BiConsumer<HypixelPlayer, Datapoint<?>> onChange) {
            this(key, type, defaultDatapoint, onChange, null, null);
        }
        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint) {
            this(key, type, defaultDatapoint, null, null, null);
        }

        public static Data fromKey(String key) {
            for (Data d : values()) if (d.getKey().equals(key)) return d;
            return null;
        }
    }
}