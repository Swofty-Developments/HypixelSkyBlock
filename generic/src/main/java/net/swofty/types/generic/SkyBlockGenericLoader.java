package net.swofty.types.generic;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.Configuration;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServiceType;
import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.bazaar.BazaarItemSet;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.mongodb.*;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.entity.ServerOrbImpl;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.entity.mob.MobRegistry;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.ServerOrb;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.set.impl.SetRepeatable;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.MissionRepeater;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.packet.SkyBlockPacketClientListener;
import net.swofty.types.generic.packet.SkyBlockPacketServerListener;
import net.swofty.types.generic.protocol.bazaar.ProtocolInitializeBazaarCheck;
import net.swofty.types.generic.region.SkyBlockMiningConfiguration;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.server.attribute.SkyBlockServerAttributes;
import net.swofty.types.generic.server.eventcaller.CustomEventCaller;
import net.swofty.types.generic.user.SkyBlockIsland;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.SkyBlockScoreboard;
import net.swofty.types.generic.user.categories.CustomGroups;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import net.swofty.types.generic.utility.MathUtility;
import org.json.JSONObject;
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
        SkyBlockConst.setEventHandler(MinecraftServer.getGlobalEventHandler());

        /**
         * Register database
         */
        new ProfilesDatabase("_placeHolder").connect(Configuration.get("mongodb"));
        new RegionDatabase("_placeHolder").connect(Configuration.get("mongodb"));
        new IslandDatabase("_placeHolder").connect(Configuration.get("mongodb"));
        FairySoulDatabase.connect(Configuration.get("mongodb"));
        AttributeDatabase.connect(Configuration.get("mongodb"));
        UserDatabase.connect(Configuration.get("mongodb"));
        CoopDatabase.connect(Configuration.get("mongodb"));
        OrbDatabase.connect(Configuration.get("mongodb"));

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

            final Component header = Component.text("§bYou are playing on §e§lMC.HYPIXEL.NET")
                    .append(Component.newline())
                    .append(Component.text("§7RAM USAGE: §8" + ramUsage + " MB"))
                    .append(Component.newline())
                    .append(Component.text("§7TPS: §8" + (1000 / tickMonitor.getTickTime())))
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
            FairySoul.spawnEntities(SkyBlockConst.getInstanceContainer());
        }

        /**
         * Spawn server orbs
         */
        if (SkyBlockConst.getInstanceContainer() != null) {
            Thread.startVirtualThread(() -> {
                OrbDatabase.getAllOrbs().forEach(orb -> {
                    ItemType type = orb.itemType;
                    try {
                        ServerOrb asOrb = (ServerOrb) type.clazz.newInstance();
                        ServerOrbImpl orbImpl = new ServerOrbImpl(asOrb.getOrbSpawnMaterial(), orb.url);
                        orbImpl.setInstance(SkyBlockConst.getInstanceContainer(),
                                new Pos(orb.position.x(), orb.position.y(), orb.position.z()));
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
         * Register events
         */
        loopThroughPackage("net.swofty.types.generic.event.custom", SkyBlockEvent.class).forEach(SkyBlockEvent::cacheEvent);
        loopThroughPackage("net.swofty.types.generic.event.actions", SkyBlockEvent.class).forEach(SkyBlockEvent::cacheEvent);
        typeLoader.getTraditionalEvents().forEach(SkyBlockEvent::cacheEvent);
        typeLoader.getCustomEvents().forEach(SkyBlockEvent::cacheEvent);

        // Register missions
        loopThroughPackage("net.swofty.types.generic.mission.missions", SkyBlockMission.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                        MissionData.registerMission(event.getClass());
                    } catch (Exception e) {
                    }
                });
        loopThroughPackage("net.swofty.types.generic.mission.missions", MissionRepeater.class)
                .forEach((event) -> {
                    try {
                        event.getTask(MinecraftServer.getSchedulerManager());
                    } catch (Exception e) {
                    }
                });
        loopThroughPackage("net.swofty.types.generic.item.set.sets", SetRepeatable.class)
                .forEach((event) -> {
                    try {
                        event.getTask(MinecraftServer.getSchedulerManager());
                    } catch (Exception e) {
                    }
                });
        CustomEventCaller.start();
        SkyBlockEvent.register(SkyBlockConst.getEventHandler());

        loopThroughPackage("net.swofty.types.generic.enchantment.impl", SkyBlockValueEvent.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                    } catch (Exception e) {}
                });
        loopThroughPackage("net.swofty.types.generic.item.set.sets", SkyBlockValueEvent.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                    } catch (Exception e) {}
                });
        loopThroughPackage("net.swofty.types.generic.item.items", SkyBlockValueEvent.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                    } catch (Exception e) {}
                });
        SkyBlockValueEvent.register();

        /**
         * Load item recipes
         */
        loopThroughPackage("net.swofty.types.generic.item.items", Craftable.class)
                .forEach(recipe -> {
                    try {
                        recipe.getRecipes().forEach(SkyBlockRecipe::init);
                    } catch (Exception e) {}
                });

        /**
         * Handle ConnectionManager
         */
        MinecraftServer.getConnectionManager().setPlayerProvider((uuid, username, playerConnection) -> {
            SkyBlockPlayer player = new SkyBlockPlayer(uuid, username, playerConnection);

            Thread thread = Thread.ofVirtual().start(() -> {
                new ProxyPlayer(uuid).getVersion().thenAccept(player::setVersion);
            });

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
