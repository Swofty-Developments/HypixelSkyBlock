package net.swofty.commons.skyblock;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.Configuration;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.skyblock.calendar.SkyBlockCalendar;
import net.swofty.commons.skyblock.data.mongodb.*;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.packet.SkyBlockPacketClientListener;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockIsland;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.fairysouls.FairySoul;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.entity.DroppedItemEntityImpl;
import net.swofty.commons.skyblock.entity.hologram.PlayerHolograms;
import net.swofty.commons.skyblock.entity.hologram.ServerHolograms;
import net.swofty.commons.skyblock.entity.npc.NPCDialogue;
import net.swofty.commons.skyblock.entity.npc.SkyBlockNPC;
import net.swofty.commons.skyblock.entity.villager.NPCVillagerDialogue;
import net.swofty.commons.skyblock.entity.villager.SkyBlockVillagerNPC;
import net.swofty.commons.skyblock.event.value.SkyBlockValueEvent;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.item.impl.Craftable;
import net.swofty.commons.skyblock.item.set.impl.SetRepeatable;
import net.swofty.commons.skyblock.item.updater.PlayerItemUpdater;
import net.swofty.commons.skyblock.mission.MissionData;
import net.swofty.commons.skyblock.mission.MissionRepeater;
import net.swofty.commons.skyblock.mission.SkyBlockMission;
import net.swofty.commons.skyblock.region.SkyBlockMiningConfiguration;
import net.swofty.commons.skyblock.region.SkyBlockRegion;
import net.swofty.commons.skyblock.server.attribute.SkyBlockServerAttributes;
import net.swofty.commons.skyblock.server.eventcaller.CustomEventCaller;
import net.swofty.commons.skyblock.user.SkyBlockScoreboard;
import net.swofty.commons.skyblock.user.categories.CustomGroups;
import net.swofty.commons.skyblock.user.statistics.PlayerStatistics;
import net.swofty.proxyapi.ProxyAPI;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class SkyBlock {
    private static final AtomicReference<TickMonitor> LAST_TICK = new AtomicReference<>();
    @Getter
    @Setter
    private static SharedInstance instanceContainer;
    @Getter
    @Setter
    private static UUID serverUUID;
    @Getter
    @Setter
    private static GlobalEventHandler globalEventHandler;

    public static void main(String[] args) throws JsonProcessingException {
        if (args.length == 0 || !ServerType.isServerType(args[0])) {
            Logger.error("Please specify a server type.");
            Arrays.stream(ServerType.values()).forEach(serverType -> Logger.error(serverType.name()));
            System.exit(0);
            return;
        }
        ServerType serverType = ServerType.valueOf(args[0].toUpperCase());
        long startTime = System.currentTimeMillis();

        /**
         * Initialize the server
         */
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        serverUUID = UUID.randomUUID();

        /**
         * Handle instances
         */
        InstanceContainer temporaryInstance = instanceManager.createInstanceContainer();
        temporaryInstance.setChunkLoader(new AnvilLoader(CustomWorlds.HUB.getFolderName()));

        instanceContainer = instanceManager.createSharedInstance(temporaryInstance);
        globalEventHandler = MinecraftServer.getGlobalEventHandler();

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

        /**
         * Register commands
         */
        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            // Large amount of Clients (such as Lunar) send a `/tip all` when joining
            // due to the scoreboard containing `hypixel.net`
            if (command.startsWith("tip ")) return;
            sender.sendMessage("§fUnknown command. Type \"/help\" for help. ('" + command + "')");
        });
        loopThroughPackage("net.swofty.commons.skyblock.command.commands", SkyBlockCommand.class).forEach(command -> {
            MinecraftServer.getCommandManager().register(command.getCommand());
        });

        /**
         * Register NPCs
         */
        loopThroughPackage("net.swofty.commons.skyblock.entity.npc.npcs", SkyBlockNPC.class)
                .forEach(SkyBlockNPC::register);
        loopThroughPackage("net.swofty.commons.skyblock.entity.npc.npcs", NPCDialogue.class)
                .forEach(SkyBlockNPC::register);
        loopThroughPackage("net.swofty.commons.skyblock.entity.villager.villagers", SkyBlockVillagerNPC.class)
                .forEach(SkyBlockVillagerNPC::register);
        loopThroughPackage("net.swofty.commons.skyblock.entity.villager.villagers", NPCVillagerDialogue.class)
                .forEach(SkyBlockVillagerNPC::register);

        /**
         * Handle server attributes
         */
        SkyBlockCalendar.tick(MinecraftServer.getSchedulerManager());
        SkyBlockServerAttributes.loadAttributes(AttributeDatabase.getDocument("attributes"));
        SkyBlockServerAttributes.saveAttributeLoop();

        /**
         * Register packet events
         */
        loopThroughPackage("net.swofty.commons.skyblock.packet.packets.client", SkyBlockPacketClientListener.class)
                .forEach(SkyBlockPacketClientListener::cacheListener);
        loopThroughPackage("net.swofty.commons.skyblock.packet.packets.server", SkyBlockPacketClientListener.class)
                .forEach(SkyBlockPacketClientListener::cacheListener);
        SkyBlockPacketClientListener.register(globalEventHandler);
        SkyBlockPacketClientListener.register(globalEventHandler);

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
         * Debugging
         */
        MinecraftServer.getGlobalEventHandler().addListener(ServerTickMonitorEvent.class, event ->
                LAST_TICK.set(event.getTickMonitor()));

        BenchmarkManager benchmarkManager = MinecraftServer.getBenchmarkManager();
        benchmarkManager.enable(Duration.ofDays(3));
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();
            if (players.isEmpty())
                return;

            long ramUsage = benchmarkManager.getUsedMemory();
            ramUsage /= 1e6; // bytes to MB

            TickMonitor tickMonitor = LAST_TICK.get();
            final Component header = Component.text("RAM USAGE: " + ramUsage + " MB")
                    .append(Component.newline())
                    .append(Component.text("TICK TIME: " + MathUtils.round(tickMonitor.getTickTime(), 2) + "ms"))
                    .append(Component.newline())
                    .append(Component.text("ACQ TIME: " + MathUtils.round(tickMonitor.getAcquisitionTime(), 2) + "ms"));
            final Component footer = benchmarkManager.getCpuMonitoringMessage();
            Audiences.players().sendPlayerListHeaderAndFooter(header, footer);
        }).repeat(10, TimeUnit.SERVER_TICK).schedule();

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
         * Register holograms and fairy souls
         */
        ServerHolograms.spawnAll(instanceContainer);
        FairySoul.spawnEntities(instanceContainer);

        /**
         * Register items
         */
        ItemAttribute.registerItemAttributes();
        PlayerItemUpdater.updateLoop(MinecraftServer.getSchedulerManager());
        DroppedItemEntityImpl.spinLoop();

        /**
         * Register events
         */
        loopThroughPackage("net.swofty.commons.skyblock.event.custom", SkyBlockEvent.class).forEach(SkyBlockEvent::cacheEvent);
        loopThroughPackage("net.swofty.commons.skyblock.event.actions", SkyBlockEvent.class).forEach(SkyBlockEvent::cacheEvent);

        // Register missions
        loopThroughPackage("net.swofty.commons.skyblock.mission.missions", SkyBlockMission.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                        MissionData.registerMission(event.getClass());
                    } catch (Exception e) {
                    }
                });
        loopThroughPackage("net.swofty.commons.skyblock.mission.missions", MissionRepeater.class)
                .forEach((event) -> {
                    try {
                        event.getTask(MinecraftServer.getSchedulerManager());
                    } catch (Exception e) {
                    }
                });
        loopThroughPackage("net.swofty.commons.skyblock.item.set.sets", SetRepeatable.class)
                .forEach((event) -> {
                    try {
                        event.getTask(MinecraftServer.getSchedulerManager());
                    } catch (Exception e) {
                    }
                });
        CustomEventCaller.start();
        SkyBlockEvent.register(globalEventHandler);

        loopThroughPackage("net.swofty.commons.skyblock.enchantment.impl", SkyBlockValueEvent.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                    } catch (Exception e) {
                    }
                });
        loopThroughPackage("net.swofty.commons.skyblock.item.set.sets", SkyBlockValueEvent.class)
                .forEach((event) -> {
                    try {
                        event.cacheEvent();
                    } catch (Exception e) {
                    }
                });
        SkyBlockValueEvent.register();

        /**
         * Load item recipes
         */
        loopThroughPackage("net.swofty.commons.skyblock.item.items", Craftable.class)
                .forEach(recipe -> {
                    try {
                        recipe.getRecipe().init();
                    } catch (Exception e) {
                    }
                });

        /**
         * Handle ConnectionManager
         */
        MojangAuth.init();
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyBlockPlayer::new);

        /**
         * Start the server
         */
        int port = Configuration.getOrDefault("port", 25530);
        minecraftServer.start("0.0.0.0", port);
        long endTime = System.currentTimeMillis();
        MinecraftServer.setBrandName("SkyBlock");
        Logger.info("Started server on port " + port + " in " + (endTime - startTime) + "ms");
        Logger.info("Server Type: " + serverType.name());

        /**
         * Initialize Proxy support
         */
        Logger.info("Initializing proxy support...");
        new ProxyAPI(serverType, Configuration.get("redis-uri"), serverUUID);
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

    public static SkyBlockPlayer getFromUUID(UUID uuid) {
        return getLoadedPlayers().stream().filter(player -> player.getUuid().toString().equalsIgnoreCase(uuid.toString())).findFirst().orElse(null);
    }

    public static SkyBlockPlayer getPlayerFromProfileUUID(UUID uuid) {
        return getLoadedPlayers().stream().filter(player -> player.getProfiles().getCurrentlySelected().equals(uuid)).findFirst().orElse(null);
    }
}