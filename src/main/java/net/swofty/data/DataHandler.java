package net.swofty.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.swofty.Utility;
import net.swofty.data.datapoints.*;
import net.swofty.user.SkyBlockInventory;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DataHandler {
    public static Map<UUID, DataHandler> userCache = new HashMap<>();
    private UUID uuid;
    private Map<String, Datapoint> datapoints = new HashMap<>();

    private Datapoint getDatapoint(String key) {
        return this.datapoints.get(key);
    }

    public static DataHandler getUser(UUID uuid) {
        if (!userCache.containsKey(uuid)) {
            return null;
        }
        return userCache.get(uuid);
    }

    public static DataHandler getUser(Player player) {
        return getUser(player.getUuid());
    }

    public UUID getUuid() {
        return uuid;
    }

    public static DataHandler fromDocument(Document document) throws JsonProcessingException {
        DataHandler dataHandler = new DataHandler();
        dataHandler.uuid = UUID.fromString(document.getString("_id"));
        Arrays.stream(Data.values()).forEach(data -> {
            if (!document.containsKey(data.getKey())) {
                dataHandler.datapoints.put(data.getKey(), data.getDefaultDatapoint().setUser(dataHandler).setData(data));
                return;
            }

            String jsonValue = document.getString(data.getKey());

            try {
                Datapoint<?> datapoint = data.getDefaultDatapoint().getClass().getDeclaredConstructor(String.class).newInstance(data.getKey());
                datapoint.deserializeValue(jsonValue);   // Deserialize the value
                dataHandler.datapoints.put(data.getKey(), datapoint.setUser(dataHandler).setData(data));
            } catch (Exception e) {
                // Issue with data, revert back to default
                dataHandler.datapoints.put(data.getKey(), data.getDefaultDatapoint().setUser(dataHandler).setData(data));
            }
        });
        return dataHandler;
    }

    public Document toDocument() {
        Document document = new Document();
        document.put("_id", this.uuid.toString());
        Arrays.stream(Data.values()).forEach(data -> {
            try {
                document.put(data.getKey(), getDatapoint(data.getKey()).getSerializedValue());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return document;
    }

    public <R extends Datapoint<T>, T> R get(Data datapoint, Class<R> type) {
        if (!this.datapoints.containsKey(datapoint.key))
            return type.cast(datapoint.defaultDatapoint);
        return type.cast(this.datapoints.get(datapoint.key));
    }

    public void runOnLoad() {
        Arrays.stream(Data.values()).forEach(data -> {
            if (data.onLoad != null) {
                data.onLoad.accept((SkyBlockPlayer) MinecraftServer.getConnectionManager().getPlayer(uuid), get(data, data.getType()));
            }
        });
    }

    @SneakyThrows
    public void runOnSave(SkyBlockPlayer player) {
        for (Data data : Data.values()) {
            if (data.onQuit != null) {
                get(data, data.getType()).setValue(data.onQuit.apply(player).getSerializedValue());
            }
        }
    }

    public static DataHandler initUserWithDefaultData(UUID uuid) {
        DataHandler dataHandler = new DataHandler();
        dataHandler.uuid = uuid;
        for(Data data : Data.values()) {
            dataHandler.datapoints.put(data.getKey(), data.getDefaultDatapoint()
                    .setUser(dataHandler)
                    .setData(data)
            );
        }
        return dataHandler;
    }

    public enum Data {
        RANK("rank", DatapointRank.class, new DatapointRank("rank", Rank.DEFAULT), (player, datapoint) -> {
            player.sendPacket(MinecraftServer.getCommandManager().createDeclareCommandsPacket(player));

            Rank rank = (Rank) datapoint.getValue();
            String teamName = Utility.limitStringLength(rank.ordinalToChar() + "_" + player.getUsername(), 16);
            Team team = new TeamBuilder(teamName, MinecraftServer.getTeamManager())
                    .prefix(Component.text(rank.getPrefix()))
                    .teamColor(rank.getTextColor())
                    .build();
            player.setTeam(team);
            player.getTeam().sendUpdatePacket();
        }),
        COINS("coins", DatapointDouble.class, new DatapointDouble("coins", 0.0), (player, datapoint) -> {}),
        INVENTORY("inventory", DatapointInventory.class, new DatapointInventory("inventory", new SkyBlockInventory()), (player, datapoint) -> {}),
        IGN_LOWER("ignLowercase", DatapointString.class, new DatapointString("ignLowercase", "null"), (player, datapoint) -> {}, (player, datapoint) -> {
            datapoint.setValue(player.getUsername().toLowerCase());
        }),
        BUILD_MODE("build_mode", DatapointBoolean.class, new DatapointBoolean("build_mode", false), (player, datapoint) -> {}, (player, datapoint) -> {
            player.setBypassBuild((Boolean) datapoint.getValue());
        }, (player) ->  {
            return new DatapointBoolean("build_mode", player.isBypassBuild());
        }),
        ;

        @Getter
        private final String key;
        @Getter
        private final Class<? extends Datapoint> type;
        @Getter
        private final Datapoint defaultDatapoint;
        public final BiConsumer<Player, Datapoint> onChange;
        public final BiConsumer<SkyBlockPlayer, Datapoint> onLoad;
        public final Function<SkyBlockPlayer, Datapoint> onQuit;

        Data(String key, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange, BiConsumer<SkyBlockPlayer, Datapoint> onLoad, Function<SkyBlockPlayer, Datapoint> onQuit) {
            this.key = key;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = onLoad;
            this.onQuit = onQuit;
        }

        Data(String key, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange, BiConsumer<SkyBlockPlayer, Datapoint> onLoad) {
            this.key = key;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = onLoad;
            this.onQuit = null;
        }

        Data(String key, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange) {
            this.key = key;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = null;
            this.onQuit = null;
        }

        public static Data fromKey(String key) {
            for (Data data : values()) {
                if(data.getKey().equals(key)) {
                    return data;
                }
            }
            return null;
        }
    }
}