package net.swofty.type.generic.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.swofty.type.generic.data.datapoints.*;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HypixelDataHandler extends DataHandler {

    protected HypixelDataHandler() { super(); }
    public HypixelDataHandler(UUID uuid) { super(uuid); }

    public static HypixelDataHandler getUser(UUID uuid) {
        if (!userCache.containsKey(uuid)) throw new RuntimeException("User " + uuid + " does not exist!");
        return (HypixelDataHandler) userCache.get(uuid);
    }

    public static @Nullable HypixelDataHandler getUser(Player player) {
        try { return getUser(player.getUuid()); } catch (Exception e) { return null; }
    }

    @Override
    public HypixelDataHandler fromDocument(Document document) {
        if (document == null) return initUserWithDefaultData(this.uuid);

        this.uuid = UUID.fromString(document.getString("_owner"));
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

    public static HypixelDataHandler createFromDocument(Document document) {
        HypixelDataHandler h = new HypixelDataHandler();
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

    public static HypixelDataHandler initUserWithDefaultData(UUID uuid) {
        HypixelDataHandler h = new HypixelDataHandler();
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

    /** Account-wide data (non-generic enum). */
    public enum Data {
        RANK("rank", DatapointRank.class, new DatapointRank("rank", Rank.DEFAULT),
                (player, datapoint) -> player.sendPacket(MinecraftServer.getCommandManager().createDeclareCommandsPacket(player))),

        IGN_LOWER("ignLowercase", DatapointString.class, new DatapointString("ignLowercase", "null"),
                null, (player, datapoint) -> ((DatapointString) datapoint).setValue(player.getUsername().toLowerCase())),

        IGN("ign", DatapointString.class, new DatapointString("ign", "null"),
                null, (player, datapoint) -> ((DatapointString) datapoint).setValue(player.getUsername())),

        CHAT_TYPE("chat_type", DatapointChatType.class,
                new DatapointChatType("chat_type", new DatapointChatType.ChatType(DatapointChatType.Chats.ALL))),

        GAMEMODE("gamemode", DatapointGamemode.class, new DatapointGamemode("gamemode", GameMode.SURVIVAL),
                (player, datapoint) -> player.setGameMode((GameMode) datapoint.getValue()),
                (player, datapoint) -> player.setGameMode((GameMode) datapoint.getValue()),
                (player) -> new DatapointGamemode("gamemode", player.getGameMode())),

        HOTBAR_SLOT("hotbar_slot", DatapointByte.class, new DatapointByte("hotbar_slot", (byte) 1),
                (player, datapoint) -> player.setHeldItemSlot((Byte) datapoint.getValue()),
                (player, datapoint) -> player.setHeldItemSlot((Byte) datapoint.getValue()),
                (player) -> new DatapointByte("hotbar_slot", player.getHeldSlot()));

        @Getter private final String key;
        @Getter private final Class<? extends Datapoint<?>> type;
        @Getter private final Datapoint<?> defaultDatapoint;
        public final BiConsumer<Player, Datapoint<?>> onChange;
        public final BiConsumer<Player, Datapoint<?>> onLoad;
        public final Function<Player, Datapoint<?>> onQuit;

        Data(String key,
             Class<? extends Datapoint<?>> type,
             Datapoint<?> defaultDatapoint,
             BiConsumer<Player, Datapoint<?>> onChange,
             BiConsumer<Player, Datapoint<?>> onLoad,
             Function<Player, Datapoint<?>> onQuit) {
            this.key = key; this.type = type; this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange; this.onLoad = onLoad; this.onQuit = onQuit;
        }
        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint,
             BiConsumer<Player, Datapoint<?>> onChange, BiConsumer<Player, Datapoint<?>> onLoad) {
            this(key, type, defaultDatapoint, onChange, onLoad, null);
        }
        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint,
             BiConsumer<Player, Datapoint<?>> onChange) {
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
