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
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.datapoints.*;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.PlayerShopData;
import net.swofty.types.generic.user.SkyBlockInventory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.MathUtility;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;
import org.tinylog.Logger;

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

    public static DataHandler getSelectedOfOfflinePlayer(UUID uuid) throws RuntimeException {
        if (userCache.containsKey(uuid))
            return userCache.get(uuid);

        PlayerProfiles playerProfiles = new UserDatabase(uuid).getProfiles();
        UUID selectedProfile = playerProfiles.getCurrentlySelected();
        if (selectedProfile == null)
            throw new RuntimeException("No profile selected for user " + uuid.toString());

        return fromDocument(ProfilesDatabase.fetchDocument(selectedProfile));
    }

    public static DataHandler getProfileOfOfflinePlayer(UUID uuid, UUID profileUUID) throws RuntimeException {
        if (userCache.containsKey(uuid))
            return userCache.get(uuid);

        PlayerProfiles playerProfiles = new UserDatabase(uuid).getProfiles();
        if (profileUUID == null)
            throw new RuntimeException("No profile selected for user " + uuid.toString());

        return fromDocument(ProfilesDatabase.fetchDocument(profileUUID));
    }

    public static DataHandler getUser(Player player) {
        return getUser(player.getUuid());
    }

    public static DataHandler fromDocument(Document document) {
        DataHandler dataHandler = new DataHandler();

        if (document == null) {
            // Return fresh data
            return initUserWithDefaultData(dataHandler.uuid);
        }

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
                Logger.info("Issue with datapoint " + data.getKey() + " for user " + dataHandler.uuid.toString() + " - Reverting to default value");

                e.printStackTrace();
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

    public void runOnLoad(SkyBlockPlayer player) {
        Arrays.stream(Data.values()).forEach(data -> {
            if (data.onLoad != null) {
                data.onLoad.accept(player, get(data, data.getType()));
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
            try {
                dataHandler.datapoints.put(
                        data.getKey(),
                        data.getDefaultDatapoint().deepClone().setUser(dataHandler).setData(data)
                );
            } catch (Exception e) {
                Logger.error("Issue with datapoint " + data.getKey() + " for user " + uuid.toString() + " - This must be fixed");
                e.printStackTrace();
            }
        }
        return dataHandler;
    }

    public enum Data {
        PROFILE_NAME("profile_name", false, true, false, DatapointString.class, new DatapointString("profile_name", "null"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            if (datapoint.getValue().equals("null")) {
                datapoint.setValue(PlayerProfiles.getRandomName());
            }
        }),
        RANK("rank", true, false, false, DatapointRank.class, new DatapointRank("rank", Rank.DEFAULT), (player, datapoint) -> {
            player.sendPacket(MinecraftServer.getCommandManager().createDeclareCommandsPacket(player));

            Rank rank = (Rank) datapoint.getValue();

            // Delay this as player needs to be loaded
            MathUtility.delay(() -> {
                SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
                if (!player.isOnline()) return;
                DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = skyBlockPlayer.getSkyBlockExperience();

                String teamName = StringUtility.limitStringLength(rank.getPriorityCharacter() + "_" + player.getUsername(), 16);
                Team team = new TeamBuilder("ZZZZZ" + teamName, MinecraftServer.getTeamManager())
                        .prefix(Component.text(
                                "§8[" + experience.getLevel().getColor() + experience.getLevel() + "§8] " +
                                        rank.getPrefix()
                        ))
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
        COINS("coins", false, false, false, DatapointDouble.class, new DatapointDouble("coins", 0.0)),
        INVENTORY("inventory", false, false, false, DatapointInventory.class, new DatapointInventory("inventory", new SkyBlockInventory()), (player, datapoint) -> {
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

                ItemStack loadedItem = PlayerItemUpdater.playerUpdate(player, itemStack.getItemStack(), true).build();
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
        SKILLS("skills", false, false, false, DatapointSkills.class, new DatapointSkills("skills")),
        IGN_LOWER("ignLowercase", true, false, false, DatapointString.class, new DatapointString("ignLowercase", "null"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            datapoint.setValue(player.getUsername().toLowerCase());
        }),
        IGN("ign", true, false, false, DatapointString.class, new DatapointString("ign", "null"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            datapoint.setValue(player.getUsername());
        }),
        BUILD_MODE("build_mode", true, false, false, DatapointBoolean.class, new DatapointBoolean("build_mode", false), (player, datapoint) -> {
        }, (player, datapoint) -> {
            player.setBypassBuild((Boolean) datapoint.getValue());
        }, (player) -> new DatapointBoolean("build_mode", player.isBypassBuild())),
        GAMEMODE("gamemode", true, false, false, DatapointGamemode.class, new DatapointGamemode("gamemode", GameMode.SURVIVAL), (player, datapoint) -> {
        }, (player, datapoint) -> {
            player.setGameMode((GameMode) datapoint.getValue());
        }, (player) -> new DatapointGamemode("gamemode", player.getGameMode())),
        EXPERIENCE("experience", false, false, false, DatapointFloat.class, new DatapointFloat("experience", 0f), (player, datapoint) -> {
        }, (player, datapoint) -> {
            player.setExp((Float) datapoint.getValue());
        }, (player) -> new DatapointFloat("experience", player.getExp())),
        BAZAAR_ORDERS("bazaar_orders", false, false, false, DatapointStringList.class, new DatapointStringList("bazaar_orders")),
        LEVELS("levels", false, false, false, DatapointInteger.class, new DatapointInteger("levels", 0), (player, datapoint) -> {
        }, (player, datapoint) -> {
            player.setLevel((Integer) datapoint.getValue());
        }, (player) -> new DatapointInteger("levels", player.getLevel())),
        MISSION_DATA("mission_data", false, false, false, DatapointMissionData.class, new DatapointMissionData("mission_data", new MissionData()), (player, datapoint) -> {
        }, (player, datapoint) -> {
            MissionData data = (MissionData) datapoint.getValue();
            data.setSkyBlockPlayer(player);
            datapoint.setValue(data);
        }),
        SHOPPING_DATA("shopping_data", false, false, true, DatapointShopData.class, new DatapointShopData("shopping_data", new PlayerShopData()), (player, datapoint) -> {}),
        TOGGLES("toggles", true, false, false, DatapointToggles.class, new DatapointToggles("toggles")),
        FAIRY_SOULS("player_fairy_souls", false, false, false, DatapointFairySouls.class, new DatapointFairySouls("player_fairy_souls")),
        CREATED("created", false, true, false, DatapointLong.class, new DatapointLong("created", 0L), (player, datapoint) -> {
        }, (player, datapoint) -> {
            if (datapoint.getValue().equals(0L)) {
                datapoint.setValue(System.currentTimeMillis());
            }
        }),
        LAST_EDITED_SKILL("last_edited_skill", false, false, false, DatapointSkillCategory.class, new DatapointSkillCategory("last_edited_skill", SkillCategories.FORAGING)),
        ISLAND_UUID("island_uuid", false, true, false, DatapointUUID.class, new DatapointUUID("island_uuid", null), (player, datapoint) -> {
        }, (player, datapoint) -> {
            datapoint.setValue(player.getSkyBlockIsland().getIslandID());
        }),
        IS_COOP("is_coop", false, true, false, DatapointBoolean.class, new DatapointBoolean("is_coop", false)),
        COLLECTION("collection", false, true, false, DatapointCollection.class, new DatapointCollection("collection")),
        MINION_DATA("minions", false, true, false, DatapointMinionData.class, new DatapointMinionData("minions")),
        STORAGE("storage", false, false, false, DatapointStorage.class, new DatapointStorage("storage")),
        BACKPACKS("backpacks", false, false, false, DatapointBackpacks.class, new DatapointBackpacks("backpacks")),
        VISITED_REGIONS("visited_regions", false, false, false, DatapointStringList.class, new DatapointStringList("visited_regions")),
        AUCTION_STATISTICS("auction_statistics", false, false, false, DatapointAuctionStatistics.class, new DatapointAuctionStatistics("auction_statistics")),
        AUCTION_ACTIVE_BIDS("auction_bids", false, false, false, DatapointUUIDList.class, new DatapointUUIDList("auction_active_bids")),
        AUCTION_INACTIVE_BIDS("auction_inactive_bids", false, false, false, DatapointUUIDList.class, new DatapointUUIDList("auction_inactive_bids")),
        AUCTION_ACTIVE_OWNED("auction_owned", false, false, false, DatapointUUIDList.class, new DatapointUUIDList("auction_active_owned")),
        AUCTION_INACTIVE_OWNED("auction_inactive_owned", false, false, false, DatapointUUIDList.class, new DatapointUUIDList("auction_inactive_owned")),
        AUCTION_ESCROW("auction_escrow", false, false, false, DatapointAuctionEscrow.class, new DatapointAuctionEscrow("auction_escrow")),
        BANK_DATA("bank_data", false, true, false, DatapointBankData.class, new DatapointBankData("bank_data"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            Thread.startVirtualThread(() -> {
                datapoint.setValue(datapoint.getValue());
            });
        }),
        PET_DATA("pet_data", false, false, false, DatapointPetData.class, new DatapointPetData("pet_data")),
        QUIVER("quiver", false, false, false, DatapointQuiver.class, new DatapointQuiver("quiver")),
        ACCESSORY_BAG("accessory_bag", false, false, false, DatapointAccessoryBag.class, new DatapointAccessoryBag("accessory_bag")),
        SKYBLOCK_EXPERIENCE("skyblock_experience", false, false, false, DatapointSkyBlockExperience.class, new DatapointSkyBlockExperience("skyblock_experience")),
        BITS("bits", false, false, false, DatapointInteger.class, new DatapointInteger("bits", 0)),
        GEMS("gems", false, false, false, DatapointInteger.class, new DatapointInteger("gems", 0)),
        PURCHASE_CONFIRMATION_BITS("purchase_confirmation_bits", false, false, false, DatapointBoolean.class, new DatapointBoolean("purchase_confirmation_bits", true)),
        MUSEUM_DATA("museum_data", false, false, false, DatapointMuseum.class, new DatapointMuseum("museum_data"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            DatapointMuseum.MuseumData data = (DatapointMuseum.MuseumData) datapoint.getValue();
            data.setCurrentlyViewing(Map.entry(player.getUuid(), player.getProfiles().getCurrentlySelected()));
        }),
        SKIN_SIGNATURE("skin_signature", false, false, false, DatapointString.class, new DatapointString("skin_signature", "null"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            datapoint.setValue(player.getSkin().signature());
        }),
        SKIN_TEXTURE("skin_texture", false, false, false, DatapointString.class, new DatapointString("skin_texture", "null"), (player, datapoint) -> {
        }, (player, datapoint) -> {
            datapoint.setValue(player.getSkin().textures());
        }),
        VISITED_ISLANDS("visited_islands", false, false, false, DatapointStringList.class, new DatapointStringList("visited_islands")),
        USED_SCROLLS("used_scrolls", false, false, false, DatapointStringList.class, new DatapointStringList("used_scrolls")),
        ;

        @Getter
        private final String key;
        @Getter
        private final Boolean isProfilePersistent;
        @Getter
        private final Boolean isCoopPersistent;
        @Getter
        private final Boolean repeatSetValue;
        @Getter
        private final Class<? extends Datapoint> type;
        @Getter
        private final Datapoint defaultDatapoint;
        public final BiConsumer<Player, Datapoint> onChange;
        public final BiConsumer<SkyBlockPlayer, Datapoint> onLoad;
        public final Function<SkyBlockPlayer, Datapoint> onQuit;

        Data(String key, Boolean isProfilePersistent, Boolean isCoopPersistent, Boolean repeatSetValue, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange, BiConsumer<SkyBlockPlayer, Datapoint> onLoad, Function<SkyBlockPlayer, Datapoint> onQuit) {
            this.key = key;
            this.isProfilePersistent = isProfilePersistent;
            this.isCoopPersistent = isCoopPersistent;
            this.repeatSetValue = repeatSetValue;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = onLoad;
            this.onQuit = onQuit;
        }

        Data(String key, Boolean isProfilePersistent, Boolean isCoopPersistent, Boolean repeatSetValue, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange, BiConsumer<SkyBlockPlayer, Datapoint> onLoad) {
            this.key = key;
            this.isProfilePersistent = isProfilePersistent;
            this.isCoopPersistent = isCoopPersistent;
            this.repeatSetValue = repeatSetValue;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = onLoad;
            this.onQuit = null;
        }

        Data(String key, Boolean isProfilePersistent, Boolean isCoopPersistent, Boolean repeatSetValue, Class<? extends Datapoint> type, Datapoint defaultDatapoint, BiConsumer<Player, Datapoint> onChange) {
            this.key = key;
            this.isProfilePersistent = isProfilePersistent;
            this.isCoopPersistent = isCoopPersistent;
            this.repeatSetValue = repeatSetValue;
            this.type = type;
            this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange;
            this.onLoad = null;
            this.onQuit = null;
        }

        Data(String key, Boolean isProfilePersistent, Boolean isCoopPersistent, Boolean repeatSetValue, Class<? extends Datapoint> type, Datapoint defaultDatapoint) {
            this.key = key;
            this.isProfilePersistent = isProfilePersistent;
            this.isCoopPersistent = isCoopPersistent;
            this.repeatSetValue = repeatSetValue;
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

    public static void startRepeatSetValueLoop() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                Arrays.stream(Data.values()).forEach(data -> {
                    if (data.repeatSetValue) {
                        Datapoint datapoint = player.getDataHandler().get(data, data.getType());
                        datapoint.setValue(datapoint.getValue());
                    }
                });
            });
            return TaskSchedule.nextTick();
        });
    }
}