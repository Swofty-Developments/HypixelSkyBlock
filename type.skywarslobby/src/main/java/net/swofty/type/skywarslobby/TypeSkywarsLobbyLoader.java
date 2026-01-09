package net.swofty.type.skywarslobby;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.lobby.LobbyTypeLoader;
import net.swofty.type.lobby.events.*;
import net.swofty.type.lobby.item.LobbyItem;
import net.swofty.type.lobby.item.LobbyItemHandler;
import net.swofty.type.lobby.item.impl.HidePlayers;
import net.swofty.type.lobby.item.impl.LobbySelector;
import net.swofty.type.lobby.item.impl.PlayCompass;
import net.swofty.type.lobby.item.impl.ProfileItem;
import net.swofty.type.lobby.launchpad.LaunchPad;
import net.swofty.type.lobby.parkour.LobbyParkourManager;
import net.swofty.type.lobby.parkour.Parkour;
import net.swofty.type.skywarslobby.hologram.LeaderboardHologramManager;
import net.swofty.type.skywarslobby.hologram.SoulWellHologramManager;
import net.swofty.type.skywarslobby.parkour.SkywarsLobbyParkour;
import net.swofty.type.skywarslobby.item.SkywarsMenuItem;
import net.swofty.type.skywarslobby.kit.SkywarsKitRegistry;
import net.swofty.type.skywarslobby.level.SkywarsLevelRegistry;
import net.swofty.type.skywarslobby.perk.SkywarsPerkRegistry;
import net.swofty.type.skywarslobby.soulwell.SoulWellParticleManager;
import net.swofty.type.skywarslobby.soulwell.SoulWellUpgradeRegistry;
import net.swofty.type.skywarslobby.util.SkywarsLobbyMap;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeSkywarsLobbyLoader implements LobbyTypeLoader {
    @Getter
    private final LobbyItemHandler itemHandler = new LobbyItemHandler();
    @Getter
    private static LeaderboardHologramManager leaderboardManager;
    private LobbyParkourManager parkourManager;
    private final Pos spawnPont = new Pos(-3.5, 66, 0.5, -90, 0);

    @Override
    public ServerType getType() {
        return ServerType.SKYWARS_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        // Initialize registries
        SkywarsKitRegistry.initialize();
        SkywarsPerkRegistry.initialize();
        SkywarsLevelRegistry.initialize();
        SoulWellUpgradeRegistry.initialize();
        Logger.info("Initialized SkyWars kit, perk, level, and soul well upgrade registries");

        SkywarsLobbyScoreboard.start();

        // Place map item frames in the lobby
        SkywarsLobbyMap skywarsLobbyMap = new SkywarsLobbyMap();
        skywarsLobbyMap.placeItemFrames(HypixelConst.getInstanceContainer());

        // Initialize parkour manager
        parkourManager = new LobbyParkourManager(getParkour());

        // Initialize leaderboard holograms
        LeaderboardHologramManager.initialize(HypixelConst.getInstanceContainer());

        // Initialize Soul Well hologram
        SoulWellHologramManager.initialize(HypixelConst.getInstanceContainer());

        // Initialize Soul Well particles
        SoulWellParticleManager.initialize(HypixelConst.getInstanceContainer());

        // Register all hotbar items
        getHotbarItems().values().forEach(itemHandler::add);

        // Register commands
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.skywarslobby.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });

        // Schedule hologram updates every 2 seconds
        MinecraftServer.getSchedulerManager().buildTask(PlayerHolograms::updateExternalHolograms)
                .delay(TaskSchedule.seconds(5))
                .repeat(TaskSchedule.seconds(2))
                .schedule();

        LobbyTypeLoader.registerLobbyCommands();
    }

    @Override
    public List<LaunchPad> getLaunchPads() {
        return List.of();
    }

    @Override
    public Map<Integer, LobbyItem> getHotbarItems() {
        return Map.of(
                0, new PlayCompass(),
                1, new ProfileItem(),
                2, new SkywarsMenuItem(),
                7, new HidePlayers(),
                8, new LobbySelector()
        );
    }

    @Override
    public Parkour getParkour() {
        return new SkywarsLobbyParkour();
    }

    @Override
    public LobbyParkourManager getParkourManager() {
        return parkourManager;
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.ORCHESTRATOR);
    }

    @Override
    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return List.of();
            }
        };
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> spawnPont,
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        List<HypixelEventClass> events = new ArrayList<>(HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.skywarslobby.events",
                HypixelEventClass.class
        ).toList());
        // Add lobby base events
        events.add(new LobbyItemEvents());
        events.add(new LobbyLaunchPadEvents());
        events.add(new LobbyPlayerJoinEvents());
        events.add(new LobbyBlockBreak());
        events.add(new LobbyPlayerMove(spawnPont));
        return events;
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.skywarslobby.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.skywarslobby.npcs",
                HypixelNPC.class
        ).toList();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.skywarslobby.redis.service",
                ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.SKYWARS_LOBBY;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(SkywarsDataHandler.class);
    }
}
