package net.swofty.type.ravengardgeneric.data;

import lombok.Getter;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.data.datapoints.DatapointRavengardInteger;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;
import tools.jackson.core.JacksonException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RavengardDataHandler extends DataHandler {
    public static final Map<UUID, RavengardDataHandler> ravengardCache = new HashMap<>();

    protected RavengardDataHandler() {
        super();
    }

    protected RavengardDataHandler(UUID uuid) {
        super(uuid);
    }

    public static RavengardDataHandler getUser(UUID uuid) {
        if (!ravengardCache.containsKey(uuid)) {
            throw new RuntimeException("User " + uuid + " does not exist!");
        }
        return ravengardCache.get(uuid);
    }

    public static @Nullable RavengardDataHandler getUser(HypixelPlayer player) {
        try {
            return getUser(player.getUuid());
        } catch (Exception e) {
            return null;
        }
    }

    public static RavengardDataHandler createFromDocument(UUID playerUuid, Document document) {
        RavengardDataHandler handler = new RavengardDataHandler(playerUuid);
        return handler.fromDocument(document);
    }

    @Override
    public RavengardDataHandler fromDocument(Document document) {
        if (document == null) {
            return initUserWithDefaultData(this.uuid);
        }

        if (document.containsKey("_id")) {
            this.uuid = UUID.fromString(document.getString("_id"));
        }

        for (Data data : Data.values()) {
            String key = data.getKey();
            if (!document.containsKey(key)) {
                this.datapoints.put(key, data.getDefaultDatapoint().deepClone().setUser(this).setData(data));
                continue;
            }

            String jsonValue = document.getString(key);
            try {
                Datapoint<?> datapoint = data.getDefaultDatapoint().deepClone();
                datapoint.deserializeValue(jsonValue);
                this.datapoints.put(key, datapoint.setUser(this).setData(data));
            } catch (Exception e) {
                this.datapoints.put(key, data.getDefaultDatapoint().deepClone().setUser(this).setData(data));
                Logger.warn(e, "Issue with Ravengard datapoint {} for user {} - defaulting", key, this.uuid);
            }
        }

        return this;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.put("_owner", this.uuid.toString());

        for (Data data : Data.values()) {
            try {
                document.put(data.getKey(), getDatapoint(data.getKey()).getSerializedValue());
            } catch (JacksonException e) {
                Logger.error(e, "Failed to serialize Ravengard datapoint {} for user {}", data.getKey(), this.uuid);
            }
        }

        return document;
    }

    public Datapoint<?> get(Data datapoint) {
        Datapoint<?> datapointValue = this.datapoints.get(datapoint.key);
        return datapointValue != null ? datapointValue : datapoint.defaultDatapoint;
    }

    @SuppressWarnings("unchecked")
    public <R extends Datapoint<?>> R get(Data datapoint, Class<R> type) {
        Datapoint<?> datapointValue = this.datapoints.get(datapoint.key);
        return (R) (datapointValue != null ? type.cast(datapointValue) : type.cast(datapoint.defaultDatapoint));
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
                target.setFrom(produced);
            }
        }
    }

    public static RavengardDataHandler initUserWithDefaultData(UUID uuid) {
        RavengardDataHandler handler = new RavengardDataHandler();
        handler.uuid = uuid;

        for (Data data : Data.values()) {
            try {
                handler.datapoints.put(
                        data.getKey(),
                        data.getDefaultDatapoint().deepClone().setUser(handler).setData(data)
                );
            } catch (Exception e) {
                Logger.error(e, "Issue with Ravengard datapoint {} for user {} - requires fixing", data.getKey(), uuid);
            }
        }

        return handler;
    }

    public static boolean hasDataInDocument(Document document) {
        if (document == null) {
            return false;
        }
        for (Data data : Data.values()) {
            if (document.containsKey(data.getKey())) {
                return true;
            }
        }
        return false;
    }

    public static RavengardDataHandler getOfOfflinePlayer(UUID uuid) {
        if (ravengardCache.containsKey(uuid)) {
            return ravengardCache.get(uuid);
        }

        UserDatabase userDatabase = new UserDatabase(uuid.toString());
        Document document = userDatabase.getHypixelData();
        return createFromDocument(uuid, document);
    }

    public enum Data {
        // Schema marker so the handler can bootstrap and migrate cleanly later.
        DATA_VERSION(
                "ravengard_data_version",
                DatapointRavengardInteger.class,
                new DatapointRavengardInteger("ravengard_data_version", 1)
        );

        @Getter
        private final String key;
        @Getter
        private final Class<? extends Datapoint<?>> type;
        @Getter
        private final Datapoint<?> defaultDatapoint;
        public final BiConsumer<HypixelPlayer, Datapoint<?>> onChange;
        public final BiConsumer<HypixelPlayer, Datapoint<?>> onLoad;
        public final Function<HypixelPlayer, Datapoint<?>> onQuit;

        Data(
                String key,
                Class<? extends Datapoint<?>> type,
                Datapoint<?> defaultDatapoint,
                BiConsumer<HypixelPlayer, Datapoint<?>> onChange,
                BiConsumer<HypixelPlayer, Datapoint<?>> onLoad,
                Function<HypixelPlayer, Datapoint<?>> onQuit
        ) {
            this.key = key;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = onLoad;
            this.onQuit = onQuit;
        }

        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint) {
            this(key, type, defaultDatapoint, null, null, null);
        }

        public static Data fromKey(String key) {
            for (Data data : values()) {
                if (data.getKey().equals(key)) {
                    return data;
                }
            }
            return null;
        }
    }
}
