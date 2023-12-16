package net.swofty;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.monitoring.BenchmarkManager;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.utils.MathUtils;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.calendar.SkyBlockCalendar;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.Resources;
import net.swofty.data.mongodb.AttributeDatabase;
import net.swofty.data.mongodb.RegionDatabase;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.entity.hologram.PlayerHolograms;
import net.swofty.entity.hologram.ServerHolograms;
import net.swofty.entity.npc.SkyBlockNPC;
import net.swofty.entity.villager.SkyBlockVillagerNPC;
import net.swofty.event.SkyBlockEvent;
import net.swofty.region.SkyBlockMiningConfiguration;
import net.swofty.region.SkyBlockRegion;
import net.swofty.server.SkyBlockServerAttributes;
import net.swofty.user.SkyBlockScoreboard;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.item.attribute.ItemAttribute;
import net.swofty.packet.SkyBlockPacketClientListener;
import net.swofty.user.categories.CustomGroups;
import net.swofty.user.statistics.PlayerStatistics;
import net.swofty.user.SkyBlockPlayer;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class SkyBlock {
    private static final AtomicReference<TickMonitor> LAST_TICK = new AtomicReference<>();
    private static final String crackedDomain = Resources.get("cracked_domain");
    public static ArrayList<UUID> offlineUUIDs = new ArrayList<>();
    @Getter
    @Setter
    private static SharedInstance instanceContainer;
    @Getter
    @Setter
    private static GlobalEventHandler globalEventHandler;

    public static void main(String[] args) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();

        /**
         * Initialize the server
         */
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        /**
         * Handle instances
         */
        InstanceContainer temporaryInstance = instanceManager.createInstanceContainer();
        temporaryInstance.setChunkLoader(new AnvilLoader("hypixel_hub"));

        instanceContainer = instanceManager.createSharedInstance(temporaryInstance);
        globalEventHandler = MinecraftServer.getGlobalEventHandler();

        /**
         * Register database
         */
        new UserDatabase("_placeHolder").connect(Resources.get("mongodb"));
        new RegionDatabase("_placeHolder").connect(Resources.get("mongodb"));
        AttributeDatabase.connect(Resources.get("mongodb"));

        /**
         * Register commands
         */
        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            // Large amount of Clients (such as Lunar) send a `/tip all` when joining
            // due to the scoreboard containing `hypixel.net`
            if (command.startsWith("tip ")) return;
            sender.sendMessage("§cUnknown command!");
        });
        loopThroughPackage("net.swofty.command.commands", SkyBlockCommand.class).forEach(command -> {
                MinecraftServer.getCommandManager().register(command.getCommand());
        });

        /**
         * Register NPCs
         */
        loopThroughPackage("net.swofty.entity.npc.npcs", SkyBlockNPC.class)
                .forEach(SkyBlockNPC::register);
        loopThroughPackage("net.swofty.entity.villager.villagers", SkyBlockVillagerNPC.class)
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
        loopThroughPackage("net.swofty.packet.packets.client", SkyBlockPacketClientListener.class)
                .forEach(SkyBlockPacketClientListener::cacheListener);
        loopThroughPackage("net.swofty.packet.packets.server", SkyBlockPacketClientListener.class)
                .forEach(SkyBlockPacketClientListener::cacheListener);
        SkyBlockPacketClientListener.register(globalEventHandler);
        SkyBlockPacketClientListener.register(globalEventHandler);

        /**
         * Load regions
         */
        SkyBlockRegion.cacheRegions();
        SkyBlockMiningConfiguration.startRepeater(MinecraftServer.getSchedulerManager());

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
         * Register holograms
         */
        ServerHolograms.spawnAll(instanceContainer);

        /**
         * Register items
         */
        ItemAttribute.registerItemAttributes();
        PlayerItemUpdater.updateLoop(MinecraftServer.getSchedulerManager());

        /**
         * Register events
         */
        loopThroughPackage("net.swofty.event.actions", SkyBlockEvent.class).forEach(SkyBlockEvent::cacheCommand);
        SkyBlockEvent.register(globalEventHandler);

        /**
         * Handle ConnectionManager
         */
        MinecraftServer.getConnectionManager().setUuidProvider((playerConnection, username) -> {
            String serverAddress = playerConnection.getServerAddress();
            if (serverAddress.contains(crackedDomain)) {
                // Player is attempting to connect through the Cracked domain
                offlineUUIDs.add(UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes()));
                return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes());
            } else {
                // Player is attempting to connect through the Premium domain
                return UUID.nameUUIDFromBytes(username.getBytes());
            }
        });
        MinecraftServer.getConnectionManager().setPlayerProvider(SkyBlockPlayer::new);

        /**
         * Start the server
         */
        minecraftServer.start("0.0.0.0", 25530);
        long endTime = System.currentTimeMillis();
        Logger.info("Started server in " + (endTime - startTime) + "ms");
    }

    public static List<SkyBlockPlayer> getLoadedPlayers() {
        List<SkyBlockPlayer> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .filter(player -> DataHandler.getUser(player) != null)
                .forEach(player -> players.add((SkyBlockPlayer) player));
        return players;
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        T instance = clazz.cast(subClass.getDeclaredConstructor().newInstance());
                        return instance;
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}