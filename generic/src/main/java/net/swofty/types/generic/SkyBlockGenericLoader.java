package net.swofty.types.generic;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.Configuration;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.Songs;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.block.attribute.BlockAttribute;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.mongodb.*;
import net.swofty.types.generic.entity.ServerCrystalImpl;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.entity.mob.MobRegistry;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.item.set.impl.MuseumableSet;
import net.swofty.types.generic.item.set.impl.SetRepeatable;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.levels.CustomLevelAward;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.levels.unlocks.CustomLevelUnlock;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.MissionRepeater;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.noteblock.SkyBlockSongsHandler;
import net.swofty.types.generic.packet.SkyBlockPacketClientListener;
import net.swofty.types.generic.packet.SkyBlockPacketServerListener;
import net.swofty.types.generic.redis.RedisAuthenticate;
import net.swofty.types.generic.redis.RedisOriginServer;
import net.swofty.types.generic.region.SkyBlockMiningConfiguration;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.server.attribute.SkyBlockServerAttributes;
import net.swofty.types.generic.server.eventcaller.CustomEventCaller;
import net.swofty.types.generic.user.SkyBlockIsland;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.SkyBlockScoreboard;
import net.swofty.types.generic.user.categories.CustomGroups;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.user.fairysouls.FairySoulZone;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import net.swofty.types.generic.utility.LaunchPads;
import net.swofty.types.generic.utility.MathUtility;
import net.swofty.types.generic.utility.StringUtility;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public record SkyBlockGenericLoader(SkyBlockTypeLoader typeLoader) {
    private static final AtomicReference<TickMonitor> LAST_TICK = new AtomicReference<>();

    @Getter
    private static MinecraftServer server;

    @SneakyThrows
    public void initialize(MinecraftServer server) {
        SkyBlockGenericLoader.server = server;
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        SkyBlockConst.setTypeLoader(typeLoader);

        /**
         * Handle instances
         */
        CustomWorlds mainInstance = typeLoader.getMainInstance();
        if (mainInstance != null) {
            InstanceContainer temporaryInstance = instanceManager.createInstanceContainer();
            temporaryInstance.setChunkLoader(new AnvilLoader(typeLoader.getMainInstance().getFolderName()));

            SkyBlockConst.setInstanceContainer(instanceManager.createSharedInstance(temporaryInstance));
        }
        SkyBlockConst.setEmptyInstance(instanceManager.createSharedInstance(instanceManager.createInstanceContainer()));
        SkyBlockConst.getEmptyInstance().setBlock(0, 99, 0, Block.BEDROCK);
        SkyBlockConst.setEventHandler(MinecraftServer.getGlobalEventHandler());

        /**
         * Setup launchpads
         */
        if (mainInstance != null) {
            LaunchPads.register(MinecraftServer.getSchedulerManager());
        }

        /**
         * Register database
         */
        ConnectionString cs = new ConnectionString(Configuration.get("mongodb"));
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        MongoClient mongoClient = MongoClients.create(settings);

        AuthenticationDatabase.connect(mongoClient);
        ProfilesDatabase.connect(mongoClient);
        RegionDatabase.connect(mongoClient);
        IslandDatabase.connect(mongoClient);
        FairySoulDatabase.connect(mongoClient);
        AttributeDatabase.connect(mongoClient);
        UserDatabase.connect(mongoClient);
        CoopDatabase.connect(mongoClient);
        CrystalDatabase.connect(mongoClient);

        /**
         * Register commands
         */
        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            // Large amount of Clients (such as Lunar) send a `/tip all` when joining
            // due to the scoreboard containing `hypixel.net`
            if (command.startsWith("tip ")) return;
            sender.sendMessage("§fUnknown command. Type \"/help\" for help. ('" + command + "')");
        });
        loopThroughPackage("net.swofty.types.generic.command.commands", SkyBlockCommand.class).forEach(command -> {
            MinecraftServer.getCommandManager().register(command.getCommand());
        });

        /**
         * Register NPCs
         */
        if (mainInstance != null) {
            loopThroughPackage("net.swofty.types.generic.entity.npc.npcs", SkyBlockNPC.class)
                    .forEach(SkyBlockNPC::register);
            loopThroughPackage("net.swofty.types.generic.entity.npc.npcs", NPCDialogue.class)
                    .forEach(SkyBlockNPC::register);
            loopThroughPackage("net.swofty.types.generic.entity.villager.villagers", SkyBlockVillagerNPC.class)
                    .forEach(SkyBlockVillagerNPC::register);
            loopThroughPackage("net.swofty.types.generic.entity.villager.villagers", NPCVillagerDialogue.class)
                    .forEach(SkyBlockVillagerNPC::register);

            typeLoader.getNPCs().forEach(SkyBlockNPC::register);
            typeLoader.getVillagerNPCs().forEach(SkyBlockVillagerNPC::register);
            typeLoader.getAnimalNPCs().forEach(SkyBlockAnimalNPC::register);
        }

        /**
         * Register entities
         */
        typeLoader.getMobs().forEach(mob -> {
            MobRegistry.registerExtraMob(mob.getEntityType(), mob.getClazz());
        });
        MathUtility.delay(() -> SkyBlockMob.runRegionPopulators(MinecraftServer.getSchedulerManager()), 50);

        /**
         * Handle server attributes
         */
        SkyBlockCalendar.tick(MinecraftServer.getSchedulerManager());
        SkyBlockServerAttributes.loadAttributes(AttributeDatabase.getDocument("attributes"));
        SkyBlockServerAttributes.saveAttributeLoop();

        /**
         * Start data loop
         */
        DataHandler.startRepeatSetValueLoop();

        /**
         * Attempt to start the song service
         */
        boolean hasAllSongs = Arrays.stream(Songs.values()).allMatch(song -> song.getPath().toFile().exists());
        if (hasAllSongs) {
            Logger.info("All songs have been found, starting song service for this instance.");
            SkyBlockSongsHandler.isEnabled = true;
        } else {
            Logger.error("Not all songs have been found, song service will not start for this instance.");
            SkyBlockSongsHandler.isEnabled = false;
        }

        /**
         * Start Tablist loop
         */
        typeLoader.getTablistManager().runScheduler(MinecraftServer.getSchedulerManager());

        /**
         * Register packet events
         */
        loopThroughPackage("net.swofty.types.generic.packet.packets.client", SkyBlockPacketClientListener.class)
                .forEach(SkyBlockPacketClientListener::cacheListener);
        loopThroughPackage("net.swofty.types.generic.packet.packets.server", SkyBlockPacketServerListener.class)
                .forEach(SkyBlockPacketServerListener::cacheListener);
        SkyBlockPacketClientListener.register(SkyBlockConst.getEventHandler());
        SkyBlockPacketServerListener.register(SkyBlockConst.getEventHandler());

        /**
         * Load regions
         */
        SkyBlockRegion.cacheRegions();
        SkyBlockMiningConfiguration.startRepeater(MinecraftServer.getSchedulerManager());
        MinecraftServer.getDimensionTypeManager().addDimension(
                DimensionType.builder(NamespaceID.from("skyblock:island"))
                        .ambientLight(2)
                        .build());
        SkyBlockIsland.runVacantLoop(MinecraftServer.getSchedulerManager());

        /**
         * Load fairy souls
         */
        FairySoul.cacheFairySouls();

        /**
         * Create audiences
         */
        CustomGroups.registerAudiences();
        PlayerStatistics.run();

        /**
         * Start repeaters
         */
        SkyBlockScoreboard.start();
        PlayerHolograms.updateAll(MinecraftServer.getSchedulerManager());

        /**
         * Start generic tablist
         */
        MinecraftServer.getGlobalEventHandler().addListener(ServerTickMonitorEvent.class, event ->
                LAST_TICK.set(event.getTickMonitor()));
        BenchmarkManager benchmarkManager = MinecraftServer.getBenchmarkManager();
        benchmarkManager.enable(Duration.ofDays(3));
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            Collection<SkyBlockPlayer> players = getLoadedPlayers();
            if (players.isEmpty())
                return;

            long ramUsage = benchmarkManager.getUsedMemory();
            ramUsage /= (long) 1e6; // bytes to MB
            TickMonitor tickMonitor = LAST_TICK.get();
            double TPS = 1000 / tickMonitor.getTickTime();

            if (TPS < 20) {
                SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                    player.getLogHandler().debug("§cServer TPS is below 20! TPS: " + TPS);
                });
                Logger.error("Server TPS is below 20! TPS: " + TPS);
            }

            final Component header = Component.text("§bYou are playing on §e§lMC.HYPIXEL.NET")
                    .append(Component.newline())
                    .append(Component.text("§7RAM USAGE: §8" + ramUsage + " MB"))
                    .append(Component.newline())
                    .append(Component.text("§7TPS: §8" + TPS))
                    .append(Component.newline());
            final Component footer = Component.newline()
                    .append(Component.text("§a§lActive Effects"))
                    .append(Component.newline())
                    .append(Component.text("§7No effects active. Drink potions or splash them on the"))
                    .append(Component.newline())
                    .append(Component.text("§7ground to buff yourself!"))
                    .append(Component.newline())
                    .append(Component.newline())
                    .append(Component.text("§d§lCookie Buff"))
                    .append(Component.newline())
                    .append(Component.text("§7Not active! Obtain booster cookies from the community"))
                    .append(Component.newline())
                    .append(Component.text("§7shop in the hub."))
                    .append(Component.newline())
                    .append(Component.newline())
                    .append(Component.text("§aRanks, Boosters & MORE! §c§lSTORE.HYPIXEL.NET"));
            Audiences.players().sendPlayerListHeaderAndFooter(header, footer);
        }).repeat(10, TimeUnit.SERVER_TICK).schedule();

        /**
         * Register holograms and fairy souls
         */
        if (mainInstance != null) {
            ServerHolograms.spawnAll(SkyBlockConst.getInstanceContainer());
            FairySoul.spawnEntities(SkyBlockConst.getInstanceContainer(), FairySoulZone.HUB);
        }

        /**
         * Spawn server crystals
         */
        if (SkyBlockConst.getInstanceContainer() != null) {
            Thread.startVirtualThread(() -> {
                CrystalDatabase.getAllCrystals().forEach(crystal -> {
                    if (crystal.serverType != SkyBlockConst.getTypeLoader().getType()) return;

                    ItemType type = crystal.itemType;
                    try {
                        ServerOrb asCrystal = (ServerOrb) type.clazz.newInstance();
                        ServerCrystalImpl crystalImpl = new ServerCrystalImpl(
                                asCrystal.getOrbSpawnMaterial(),
                                crystal.url,
                                asCrystal.getBlocksToPlaceOn()
                        );

                        crystalImpl.setInstance(SkyBlockConst.getInstanceContainer(),
                                new Pos(crystal.position.x(), crystal.position.y(), crystal.position.z()));
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        }

        /**
         * Register items
         */
        ItemAttribute.registerItemAttributes();
        PlayerItemUpdater.updateLoop(MinecraftServer.getSchedulerManager());

        /**
         * Register blocks
         */
        BlockAttribute.registerBlockAttributes();

        /**
         * Register SkyBlock levels
         */
        SkyBlockLevelCause.initializeCauses();

        /**
         * Register events
         */
        loopThroughPackage("net.swofty.types.generic.enchantment.impl", SkyBlockEventClass.class).forEach(SkyBlockEventHandler::registerEventMethods);
        loopThroughPackage("net.swofty.types.generic.event.custom", SkyBlockEventClass.class).forEach(SkyBlockEventHandler::registerEventMethods);
        loopThroughPackage("net.swofty.types.generic.event.actions", SkyBlockEventClass.class).forEach(SkyBlockEventHandler::registerEventMethods);
        loopThroughPackage("net.swofty.types.generic.item.items", SkyBlockEventClass.class).forEach(SkyBlockEventHandler::registerEventMethods);
        loopThroughPackage("net.swofty.types.generic.mission.missions", SkyBlockEventClass.class).forEach(SkyBlockEventHandler::registerEventMethods);
        typeLoader.getTraditionalEvents().forEach(SkyBlockEventHandler::registerEventMethods);
        typeLoader.getCustomEvents().forEach(SkyBlockEventHandler::registerEventMethods);

        // Register missions
        loopThroughPackage("net.swofty.types.generic.mission.missions", SkyBlockMission.class)
                .forEach((event) -> {
                    MissionData.registerMission(event.getClass());
                });
        loopThroughPackage("net.swofty.types.generic.mission.missions", MissionRepeater.class)
                .forEach((event) -> {
                    event.getTask(MinecraftServer.getSchedulerManager());
                });
        loopThroughPackage("net.swofty.types.generic.item.set.sets", SetRepeatable.class)
                .forEach((event) -> {
                    event.getTask(MinecraftServer.getSchedulerManager());
                });
        loopThroughPackage("net.swofty.types.generic.enchantment.impl", SkyBlockValueEvent.class)
                .forEach(SkyBlockValueEvent::cacheEvent);
        loopThroughPackage("net.swofty.types.generic.item.set.sets", SkyBlockValueEvent.class)
                .forEach(SkyBlockValueEvent::cacheEvent);
        loopThroughPackage("net.swofty.types.generic.item.items", SkyBlockValueEvent.class)
                .forEach(SkyBlockValueEvent::cacheEvent);
        SkyBlockValueEvent.register();
        CustomEventCaller.start();
        SkyBlockEventHandler.register(SkyBlockConst.getEventHandler());

        /**
         * Cache custom collections
         */
        Thread.startVirtualThread(() -> {
            // Collection Unlocks
            CollectionCategories.getCategories().forEach(category -> {
                Arrays.stream(category.getCollections()).forEach(collection -> {
                    List<CollectionCategory.ItemCollectionReward> rewards = List.of(collection.rewards());
                    rewards.parallelStream().forEach(reward -> {
                        Arrays.stream(reward.unlocks()).forEach(unlock -> {
                            if (unlock instanceof CollectionCategory.UnlockCustomAward award) {
                                CustomCollectionAward.AWARD_CACHE.put(award.getAward(),
                                        Map.entry(collection.type(), reward.requirement()));
                            }
                        });
                    });
                });
            });

            // Level Unlocks
            Arrays.stream(SkyBlockLevelRequirement.values()).forEach(requirement -> {
                requirement.getUnlocks().forEach(unlock -> {
                    if (unlock instanceof CustomLevelUnlock award) {
                        CustomLevelAward.addToCache(requirement.asInt(), award.getAward());
                    }
                });
            });
        });

        /**
         * Load item recipes
         */
        loopThroughPackage("net.swofty.types.generic.item.items", DefaultCraftable.class)
                .forEach(recipe -> {
                    try {
                        recipe.getRecipes().forEach(SkyBlockRecipe::init);
                    } catch (Exception e) {}
                });
        CollectionCategories.getCategories().forEach(category -> {
            Arrays.stream(category.getCollections()).forEach(collection -> {
                List<SkyBlockRecipe<?>> recipes = new ArrayList<>();
                Arrays.stream(collection.rewards()).forEach(reward -> {
                    Arrays.stream(reward.unlocks()).forEach(unlock -> {
                        if (unlock instanceof CollectionCategory.UnlockRecipe recipe) {
                            List<SkyBlockRecipe<?>> recipeInstances = recipe.getRecipes();

                            recipeInstances.forEach(recipeInstance -> {
                                recipeInstance.setCanCraft((player) -> {
                                    int amount = player.getCollection().get(collection.type());
                                    return new SkyBlockRecipe.CraftingResult(
                                            amount >= reward.requirement(),
                                            new String[]{"§7You must have §c" + collection.type().getDisplayName(null)
                                                    + " Collection "
                                                    + StringUtility.getAsRomanNumeral(collection.getPlacementOf(reward))}
                                    );
                                });
                                recipes.add(recipeInstance);
                            });
                        }
                    });
                });
                recipes.forEach(SkyBlockRecipe::init);
            });
        });

        /**
         * Register Museum items
         */
        Arrays.stream(ItemType.values()).forEach(itemType -> {
            try {
                if (itemType.clazz == null) return;
                if (itemType.clazz.newInstance() instanceof Museumable museumable) {
                    if (!(itemType.clazz.newInstance() instanceof TrackedUniqueItem))
                        Logger.error("Item " + itemType + " is not a tracked item, but is being registered as a museum item.");

                    MuseumableItemCategory.addItem(museumable.getMuseumCategory(), itemType);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        Arrays.stream(ArmorSetRegistry.values()).forEach(armorSet -> {
            try {
                if (armorSet.getClazz().newInstance() instanceof MuseumableSet) {
                    MuseumableItemCategory.addItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getHelmet());
                    MuseumableItemCategory.addItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getChestplate());
                    MuseumableItemCategory.addItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getLeggings());
                    MuseumableItemCategory.addItem(MuseumableItemCategory.ARMOR_SETS, armorSet.getBoots());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        /**
         * Handle ConnectionManager
         */
        MinecraftServer.getConnectionManager().setPlayerProvider((uuid, username, playerConnection) -> {
            SkyBlockPlayer player = new SkyBlockPlayer(uuid, username, playerConnection);

            Thread.ofVirtual().start(() -> {
                new ProxyPlayer(uuid).getVersion().thenAccept(player::setVersion);
            });

            if (RedisOriginServer.origin.containsKey(uuid)) {
                player.setOriginServer(RedisOriginServer.origin.get(uuid));
            }
            if (RedisAuthenticate.toAuthenticate.contains(uuid)) {
                player.setHasAuthenticated(false);
            }

            Logger.info("Received new player: " + username + " (" + uuid + ")");

            return player;
        });
    }

    public static List<SkyBlockPlayer> getLoadedPlayers() {
        List<SkyBlockPlayer> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .filter(player -> DataHandler.getUser(player) != null)
                .filter(player -> player.getInstance() != null)
                .forEach(player -> players.add((SkyBlockPlayer) player));
        return players;
    }

    public static SkyBlockPlayer getFromUUID(UUID uuid) {
        return getLoadedPlayers().stream().filter(player -> player.getUuid().toString().equalsIgnoreCase(uuid.toString())).findFirst().orElse(null);
    }

    public static SkyBlockPlayer getPlayerFromProfileUUID(UUID uuid) {
        return getLoadedPlayers().stream().filter(player -> player.getProfiles().getCurrentlySelected().equals(uuid)).findFirst().orElse(null);
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}
