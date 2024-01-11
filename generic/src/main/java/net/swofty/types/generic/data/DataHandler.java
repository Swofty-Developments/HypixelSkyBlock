package net.swofty.types.generic.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamBuilder;
import net.swofty.types.generic.data.datapoints.*;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.user.PlayerShopData;
import net.swofty.types.generic.user.SkyBlockInventory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.UserProfiles;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.MathUtility;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DataHandler {
    public static Map<UUID, DataHandler> userCache = new HashMap<>();
    @Getter
    private UUID uuid;
    private final Map<String, Datapoint> datapoints = new HashMap<>();

    public Datapoint getDatapoint(String key) {
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

    public static DataHandler fromDocument(Document document) {
        DataHandler dataHandler = new DataHandler();
        dataHandler.uuid = UUID.fromString(document.getString("_owner"));
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

    public Document toDocument(UUID profileUuid) {
        Document document = new Document();
        document.put("_owner", this.uuid.toString());
        document.put("_id", profileUuid.toString());
        Arrays.stream(Data.values()).forEach(data -> {
            try {
                document.put(data.getKey(), getDatapoint(data.getKey()).getSerializedValue());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return document;
    }

    public Map<String, Object> getPersistentValues() {
        Map<String, Object> values = new HashMap<>();
        Arrays.stream(Data.values()).forEach(data -> {
            if (data.isProfilePersistent) {
                values.put(data.getKey(), getDatapoint(data.getKey()).getValue());
            }
        });
        return values;
    }

    public Map<String, Object> getCoopValues() {
        Map<String, Object> values = new HashMap<>();
        Arrays.stream(Data.values()).forEach(data -> {
            if (data.isCoopPersistent) {
                values.put(data.getKey(), getDatapoint(data.getKey()).getValue());
            }
        });
        return values;
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
                get(data, data.getType()).setValue(data.onQuit.apply(player).getValue());
            }
        }
    }

    public static DataHandler initUserWithDefaultData(UUID uuid) {
        DataHandler dataHandler = new DataHandler();
        dataHandler.uuid = uuid;
        for (Data data : Data.values()) {
            dataHandler.datapoints.put(
                    data.getKey(),
                    data.getDefaultDatapoint().deepClone().setUser(dataHandler).setData(data)
            );
        }
        return dataHandler;
    }

    public enum Data {
        PROFILE_NAME("profile_name", false, true, DatapointString.class, new DatapointString("profile_name", "null"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            if (datapoint.getValue().equals("null")) {
                datapoint.setValue(UserProfiles.getRandomName());
            }
        }),
        RANK("rank", true, false, DatapointRank.class, new DatapointRank("rank", Rank.DEFAULT), (player, datapoint) -> {
            player.sendPacket(MinecraftServer.getCommandManager().createDeclareCommandsPacket(player));

            Rank rank = (Rank) datapoint.getValue();

            // Delay this as player needs to be loaded
            MathUtility.delay(() -> {
                String teamName = StringUtility.limitStringLength(rank.getPriorityCharacter() + "_" + player.getUsername(), 16);
                Team team = new TeamBuilder(teamName, MinecraftServer.getTeamManager())
                        .prefix(Component.text(rank.getPrefix()))
                        .teamColor(rank.getTextColor())
                        .build();
                player.setTeam(team);
                player.getTeam().sendUpdatePacket();
            }, 5);
        }, ((player, datapoint) -> {
            Team team = new TeamBuilder(player.getUsername(), MinecraftServer.getTeamManager())
                    .prefix(Component.text(((Rank) datapoint.getValue()).getPrefix()))
                    .teamColor(((Rank) datapoint.getValue()).getTextColor())
                    .build();
            player.setTeam(team);
            player.getTeam().sendUpdatePacket();
        })),
        COINS("coins", false, false, DatapointDouble.class, new DatapointDouble("coins", 0.0)),
        INVENTORY("inventory", false, false, DatapointInventory.class, new DatapointInventory("inventory", new SkyBlockInventory()), (player, datapoint) -> {
        }, (player, datapoint) -> {
            SkyBlockInventory skyBlockInventory = (SkyBlockInventory) datapoint.getValue();

            player.setHelmet(skyBlockInventory.getHelmet().getItemStack());
            player.setChestplate(skyBlockInventory.getChestplate().getItemStack());
            player.setLeggings(skyBlockInventory.getLeggings().getItemStack());
            player.setBoots(skyBlockInventory.getBoots().getItemStack());

            skyBlockInventory.getItems().forEach((integer, itemStack) -> {
                PlayerItemOrigin origin = PlayerItemOrigin.INVENTORY_SLOT;
                origin.setData(integer);

                player.getInventory().setItemStack(integer, itemStack.getItemStack());

                ItemStack loadedItem = PlayerItemUpdater.playerUpdate(player, origin, itemStack.getItemStack()).build();
                origin.setStack(player, loadedItem);
            });

            player.getInventory().setItemStack(8,
                    new NonPlayerItemUpdater(new SkyBlockItem(ItemType.SKYBLOCK_MENU).getItemStack())
                            .getUpdatedItem().build());
        }, (player) -> {
            SkyBlockInventory skyBlockInventory = new SkyBlockInventory();

            ItemStack helmet = player.getHelmet();
            if (SkyBlockItem.isSkyBlockItem(helmet)) {
                skyBlockInventory.setHelmet(new SkyBlockItem(helmet));
            }
            ItemStack chestplate = player.getChestplate();
            if (SkyBlockItem.isSkyBlockItem(chestplate)) {
                skyBlockInventory.setChestplate(new SkyBlockItem(chestplate));
            }
            ItemStack leggings = player.getLeggings();
            if (SkyBlockItem.isSkyBlockItem(leggings)) {
                skyBlockInventory.setLeggings(new SkyBlockItem(leggings));
            }
            ItemStack boots = player.getBoots();
            if (SkyBlockItem.isSkyBlockItem(boots)) {
                skyBlockInventory.setBoots(new SkyBlockItem(boots));
            }

            for (int i = 0; i <= 36; i++) {
                ItemStack stack = player.getInventory().getItemStack(i);
                if (SkyBlockItem.isSkyBlockItem(stack)) {
                    skyBlockInventory.getItems().put(i, new SkyBlockItem(stack));
                }
            }
            return new DatapointInventory("inventory", skyBlockInventory);
        }),
        IGN_LOWER("ignLowercase", true, false, DatapointString.class, new DatapointString("ignLowercase", "null"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            datapoint.setValue(player.getUsername().toLowerCase());
        }),
        IGN("ign", true, false, DatapointString.class, new DatapointString("ign", "null"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            datapoint.setValue(player.getUsername());
        }),
        BUILD_MODE("build_mode", true, false, DatapointBoolean.class, new DatapointBoolean("build_mode", false), (player, datapoint) -> {
        }, (player, datapoint) -> {
            player.setBypassBuild((Boolean) datapoint.getValue());
        }, (player) -> new DatapointBoolean("build_mode", player.isBypassBuild())),
        GAMEMODE("gamemode", true, false, DatapointGamemode.class, new DatapointGamemode("gamemode", GameMode.SURVIVAL), (player, datapoint) -> {
        }, (player, datapoint) -> {
            player.setGameMode((GameMode) datapoint.getValue());
        }, (player) -> new DatapointGamemode("gamemode", player.getGameMode())),
        EXPERIENCE("experience", false, false, DatapointFloat.class, new DatapointFloat("experience", 0f), (player, datapoint) -> {
        }, (player, datapoint) -> {
            player.setExp((Float) datapoint.getValue());
        }, (player) -> new DatapointFloat("experience", player.getExp())),
        LEVELS("levels", false, false, DatapointInteger.class, new DatapointInteger("levels", 0), (player, datapoint) -> {
        }, (player, datapoint) -> {
            player.setLevel((Integer) datapoint.getValue());
        }, (player) -> new DatapointInteger("levels", player.getLevel())),
        MISSION_DATA("mission_data", false, false, DatapointMissionData.class, new DatapointMissionData("mission_data", new MissionData()), (player, datapoint) -> {
        }, (player, datapoint) -> {
            MissionData data = (MissionData) datapoint.getValue();
            data.setSkyBlockPlayer(player);
            datapoint.setValue(data);
        }),
        SHOPPING_DATA("shopping_data", false, false, DatapointShopData.class, new DatapointShopData("shopping_data", new PlayerShopData()), (player, datapoint) -> {
        }, (player, datapoint) -> {
            PlayerShopData data = (PlayerShopData) datapoint.getValue();
            datapoint.setValue(data);
        }),
        DISABLE_DROP_MESSAGE("disable_drop_message", true, false, DatapointBoolean.class, new DatapointBoolean("disable_drop_message", false), (player, datapoint) -> {}),
        FAIRY_SOULS("fairy_souls", false, false, DatapointIntegerList.class, new DatapointIntegerList("fairy_souls"), (player, datapoint) -> {}),
        CREATED("created", false, true, DatapointLong.class, new DatapointLong("created", 0L), (player, datapoint) -> {
        }, (player, datapoint) -> {
            if (datapoint.getValue().equals(0L)) {
                datapoint.setValue(System.currentTimeMillis());
            }
        }),
        ISLAND_UUID("island_uuid", false, true, DatapointUUID.class, new DatapointUUID("island_uuid", null), (player, datapoint) -> {
        }, (player, datapoint) -> {
            datapoint.setValue(player.getSkyBlockIsland().getIslandID());
        }),
        IS_COOP("is_coop", false, true, DatapointBoolean.class, new DatapointBoolean("is_coop", false), (player, datapoint) -> {}),
        COLLECTION("collection", false, true, DatapointCollection.class, new DatapointCollection("collection"), (player, datapoint) -> {}, (player, datapoint) -> {}),
        MINION_DATA("minions", false, true, DatapointMinionData.class, new DatapointMinionData("minions"), (player, datapoint) -> {}, (player, datapoint) -> {}),
        ;

        @Getter
        private final String key;
        @Getter
        private final Boolean isProfilePersistent;
        @Getter
        private final Boolean isCoopPersistent;
        @Getter
        private final Class<? extends Datapoint> type;
        @Getter
        private final Datapoint defaultDatapoint;
        public final BiConsumer<Player, Datapoint> onChange;
        public final BiConsumer<SkyBlockPlayer, Datapoint> onLoad;
        public final Function<SkyBlockPlayer, Datapoint> onQuit;

        Data(String key, Boolean isProfilePersistent, Boolean isCoopPersistent, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange, BiConsumer<SkyBlockPlayer, Datapoint> onLoad, Function<SkyBlockPlayer, Datapoint> onQuit) {
            this.key = key;
            this.isProfilePersistent = isProfilePersistent;
            this.isCoopPersistent = isCoopPersistent;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = onLoad;
            this.onQuit = onQuit;
        }

        Data(String key, Boolean isProfilePersistent, Boolean isCoopPersistent, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange, BiConsumer<SkyBlockPlayer, Datapoint> onLoad) {
            this.key = key;
            this.isProfilePersistent = isProfilePersistent;
            this.isCoopPersistent = isCoopPersistent;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = onLoad;
            this.onQuit = null;
        }

        Data(String key, Boolean isProfilePersistent, Boolean isCoopPersistent, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange) {
            this.key = key;
            this.isProfilePersistent = isProfilePersistent;
            this.isCoopPersistent = isCoopPersistent;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = null;
            this.onQuit = null;
        }

        Data(String key, Boolean isProfilePersistent, Boolean isCoopPersistent, Class<? extends Datapoint> type, Datapoint defaultDatapoint) {
            this.key = key;
            this.isProfilePersistent = isProfilePersistent;
            this.isCoopPersistent = isCoopPersistent;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = null;
            this.onLoad = null;
            this.onQuit = null;
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