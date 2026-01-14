package net.swofty.type.bedwarslobby;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.bedwarslobby.hologram.LeaderboardHologramManager;
import net.swofty.type.bedwarslobby.item.impl.BedWarsMenu;
import net.swofty.type.bedwarslobby.item.impl.Collectibles;
import net.swofty.type.bedwarslobby.launchpad.BedWarsLaunchPads;
import net.swofty.type.bedwarslobby.parkour.BedWarsLobbyParkour;
import net.swofty.type.bedwarslobby.util.BedWarsLobbyMap;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.leaderboard.BedWarsLeaderboardAggregator;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.bedwarslobby.tab.BedWarsPlayersOnlineModule;
import net.swofty.type.lobby.LobbyTypeLoader;
import net.swofty.type.lobby.events.*;
import net.swofty.type.lobby.item.LobbyItem;
import net.swofty.type.lobby.item.LobbyItemHandler;
import net.swofty.type.lobby.item.impl.HidePlayers;
import net.swofty.type.lobby.item.impl.LobbySelector;
import net.swofty.type.lobby.item.impl.PlayCompass;
import net.swofty.type.lobby.item.impl.ProfileItem;
import net.swofty.type.lobby.launchpad.LaunchPad;
import net.swofty.type.lobby.launchpad.LaunchPadHandler;
import net.swofty.type.lobby.parkour.LobbyParkourManager;
import net.swofty.type.lobby.parkour.Parkour;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TypeBedWarsLobbyLoader implements LobbyTypeLoader {
    public static BedWarsLobbyMap bedWarsLobbyMap = new BedWarsLobbyMap();
    public static LobbyParkourManager parkourManager;
    private final Pos spawnPoint = new Pos(-39.5, 72, 0, -90, 0);

    @Getter
    private final LobbyItemHandler itemHandler = new LobbyItemHandler();

    @Override
    public ServerType getType() {
        return ServerType.BEDWARS_LOBBY;
    }

    @Override
    public Parkour getParkour() {
        return new BedWarsLobbyParkour();
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        BedWarsLobbyScoreboard.start();
        bedWarsLobbyMap.placeItemFrames(HypixelConst.getInstanceContainer());

        // Register launch pads
        LaunchPadHandler.register(MinecraftServer.getSchedulerManager(), getLaunchPads());

        // Register all hotbar items
        getHotbarItems().values().forEach(itemHandler::add);

        // Initialize leaderboard holograms
        LeaderboardHologramManager.initialize(HypixelConst.getInstanceContainer());

        // Register commands
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.bedwarslobby.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });

        // Start the leaderboard aggregator (for daily/weekly/monthly leaderboard updates)
        BedWarsLeaderboardAggregator.initialize();

        // Schedule hologram updates every 2 seconds
        MinecraftServer.getSchedulerManager().buildTask(PlayerHolograms::updateExternalHolograms).delay(TaskSchedule.seconds(5))
          .repeat(TaskSchedule.seconds(2))
          .schedule();

        parkourManager = new LobbyParkourManager(getParkour());

        LobbyTypeLoader.registerLobbyCommands();
    }

    @Override
    public List<LaunchPad> getLaunchPads() {
        return Arrays.asList(BedWarsLaunchPads.values());
    }

    @Override
    public @Nullable LobbyParkourManager getParkourManager() {
        return parkourManager;
    }

    @Override
    public Map<Integer, LobbyItem> getHotbarItems() {
        return Map.of(
                0, new PlayCompass(),
                1, new ProfileItem(),
                2, new BedWarsMenu(),
                4, new Collectibles(),
                7, new HidePlayers(),
                8, new LobbySelector()
        );
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
                return List.of(
                        new BedWarsPlayersOnlineModule(1),
                        new BedWarsPlayersOnlineModule(2),
                        new BedWarsPlayersOnlineModule(3),
                        new BedWarsPlayersOnlineModule(4)
                );
            }
        };
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> spawnPoint,
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        List<HypixelEventClass> events = new ArrayList<>(HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.bedwarslobby.events",
                HypixelEventClass.class
        ).toList());
        // Add lobby base events
        events.add(new LobbyItemEvents());
        events.add(new LobbyParkourEvents());
        events.add(new LobbyLaunchPadEvents());
        events.add(new LobbyPlayerJoinEvents());
        events.add(new LobbyBlockBreak());
        events.add(new LobbyPlayerMove(spawnPoint));
        return events;
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.bedwarslobby.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.bedwarslobby.npcs",
                HypixelNPC.class
        ).toList();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.bedwarslobby.redis.service",
                ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.BEDWARS_LOBBY;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(BedWarsDataHandler.class);
    }
}
