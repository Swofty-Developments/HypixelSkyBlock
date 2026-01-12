package net.swofty.type.generic.data;

import io.sentry.Sentry;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.datapoints.*;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.utility.MathUtility;
import org.bson.Document;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;
import tools.jackson.core.JacksonException;

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

        this.uuid = UUID.fromString(document.getString("_id"));
        for (Data data : Data.values()) {
            String key = data.getKey();
            if (!document.containsKey(key)) {
                this.datapoints.put(key, data.getDefaultDatapoint().deepClone().setUser(this).setData(data));
                continue;
            }
            String jsonValue = document.getString(key);
            try {
                Datapoint<?> dp = data.getDefaultDatapoint().getClass()
                        .getDeclaredConstructor(String.class).newInstance(key);
                dp.deserializeValue(jsonValue);
                this.datapoints.put(key, dp.setUser(this).setData(data));
            } catch (Exception e) {
                this.datapoints.put(key, data.getDefaultDatapoint().deepClone().setUser(this).setData(data));
                Logger.error(e, "Issue with datapoint {} for user {} - defaulting to default value", key, this.uuid);
                Sentry.captureException(e);
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
            } catch (JacksonException e) {
                Logger.error(e, "Failed to serialize datapoint {} for user {}", data.getKey(), this.uuid);
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
                if (produced != null) {
                    Datapoint<?> target = get(data);
                    target.setFrom(produced); // no onChange during save
                }
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
                Logger.error(e, "Issue with datapoint {} for user {} - requires fixing", data.getKey(), uuid);
            }
        }
        return h;
    }

    public static HypixelDataHandler getOfOfflinePlayer(UUID uuid) throws RuntimeException {
        if (userCache.containsKey(uuid))
            return (HypixelDataHandler) userCache.get(uuid);

        UserDatabase userDatabase = new UserDatabase(uuid.toString());
        Document doc = userDatabase.getHypixelData();
        return createFromDocument(doc);
    }

    @Blocking
    public static String getPotentialIGNFromUUID(UUID uuid) {
        if (userCache.containsKey(uuid))
            return ((HypixelDataHandler) userCache.get(uuid)).get(Data.IGN, DatapointString.class).getValue();
        Document doc = ProfilesDatabase.collection.find(new Document("_id", uuid.toString())).first();
        HypixelDataHandler handler = HypixelDataHandler.createFromDocument(doc);
        return handler.get(Data.IGN, DatapointString.class).getValue();
    }

    @Blocking
    public static @Nullable UUID getPotentialUUIDFromName(String name) throws RuntimeException {
        Document doc = UserDatabase.collection.find(
                new Document("ignLowercase", "\"" + name.toLowerCase() + "\"")
        ).first();

        if (doc == null)
            return null;
        return UUID.fromString(doc.getString("_id"));
    }

    /** Account-wide data (non-generic enum). */
    public enum Data {
        RANK("rank", DatapointRank.class, new DatapointRank("rank", Rank.DEFAULT),
                (player, datapoint) -> {
            player.sendPacket(MinecraftServer.getCommandManager().createDeclareCommandsPacket(player));

            Rank rank = (Rank) datapoint.getValue();

            // Delay this as player needs to be loaded
            MathUtility.delay(() -> {
                if (!player.isOnline()) return;
                if (HypixelConst.getTypeLoader().getType().isSkyBlock()) return;

                String teamName = StringUtility.limitStringLength(rank.getPriorityCharacter() + player.getUsername(), 15);
                Team team = new TeamBuilder("Z" + teamName, MinecraftServer.getTeamManager())
                        .prefix(Component.text(rank.getPrefix()))
                        .teamColor(rank.getTextColor())
                        .build();
                player.setTeam(team);
                player.getTeam().sendUpdatePacket();
            }, 5);
        }, (player, datapoint) -> {
            player.sendPacket(MinecraftServer.getCommandManager().createDeclareCommandsPacket(player));
            if (HypixelConst.getTypeLoader().getType().isSkyBlock()) return;

            Rank rank = (Rank) datapoint.getValue();
            String teamName = StringUtility.limitStringLength(rank.getPriorityCharacter() + player.getUsername(), 15);
            player.setTeam(new TeamBuilder("Z" + teamName, MinecraftServer.getTeamManager())
                    .prefix(Component.text(rank.getPrefix()))
                    .teamColor(rank.getTextColor())
                    .build());
            player.getTeam().sendUpdatePacket();
        }),

        IGN_LOWER("ignLowercase", DatapointString.class, new DatapointString("ignLowercase", "null"),
                null, (player, datapoint) -> ((DatapointString) datapoint).setValue(player.getUsername().toLowerCase())),

        IGN("ign", DatapointString.class, new DatapointString("ign", "null"),
                null, (player, datapoint) -> ((DatapointString) datapoint).setValue(player.getUsername())),

        CHAT_TYPE("chat_type", DatapointChatType.class,
                new DatapointChatType("chat_type", new DatapointChatType.ChatType(DatapointChatType.Chats.ALL))),

        TOGGLES("toggles", DatapointToggles.class, new DatapointToggles("toggles")),

        GAMEMODE("gamemode", DatapointGamemode.class, new DatapointGamemode("gamemode", GameMode.SURVIVAL),
                (player, datapoint) -> {
                    if (HypixelConst.getTypeLoader().getType().isSkyBlock()) {
                        player.setGameMode((GameMode) datapoint.getValue());
                    }
                },
                (player, datapoint) -> {
                    if (HypixelConst.getTypeLoader().getType().isSkyBlock()) {
                        // Reset to SURVIVAL if coming from non-SkyBlock server
                        HypixelPlayer hypixelPlayer = (HypixelPlayer) player;
                        if (hypixelPlayer.getOriginServer() == null || !hypixelPlayer.getOriginServer().isSkyBlock()) {
                            player.setGameMode(GameMode.SURVIVAL);
                        } else {
                            player.setGameMode((GameMode) datapoint.getValue());
                        }
                    }
                },
                (player) -> {
                    if (HypixelConst.getTypeLoader().getType().isSkyBlock()) {
                        return new DatapointGamemode("gamemode", player.getGameMode());
                    }
                    return null; // Don't update gamemode for non-SkyBlock servers
                }),

        SKIN_SIGNATURE("skin_signature",
                DatapointString.class, new DatapointString("skin_signature", "null"),
                (player, datapoint) -> {},
                (player, datapoint) -> ((DatapointString) datapoint).setValue(player.getSkin().signature())),

        SKIN_TEXTURE("skin_texture",
                DatapointString.class, new DatapointString("skin_texture", "null"),
                (player, datapoint) -> {},
                (player, datapoint) -> ((DatapointString) datapoint).setValue(player.getSkin().textures())),

        HYPIXEL_EXPERIENCE("hypixel_experience",
                DatapointHypixelExperience.class, new DatapointHypixelExperience("hypixel_experience", 0L)),

        ACHIEVEMENT_DATA("achievement_data",
                DatapointAchievementData.class, new DatapointAchievementData("achievement_data")),

        QUEST_DATA("quest_data",
                DatapointQuestData.class, new DatapointQuestData("quest_data")),

        PARKOUR_DATA("parkour_data", DatapointParkourData.class, new DatapointParkourData("parkour_data")),

        FRIEND_SORT("friend_sort", DatapointFriendSort.class,
                new DatapointFriendSort("friend_sort",
                        new DatapointFriendSort.FriendSortData(DatapointFriendSort.SortType.DEFAULT, false)))
        ;

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
