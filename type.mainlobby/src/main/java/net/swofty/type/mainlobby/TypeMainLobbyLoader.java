package net.swofty.type.mainlobby;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.TypedProxyHandler;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.lobby.LobbyTypeLoader;
import net.swofty.type.lobby.events.LobbyAFKEvents;
import net.swofty.type.lobby.events.LobbyItemEvents;
import net.swofty.type.lobby.events.LobbyLaunchPadEvents;
import net.swofty.type.lobby.events.LobbyParkourEvents;
import net.swofty.type.lobby.events.LobbyPlayerJoinEvents;
import net.swofty.type.lobby.events.LobbyPlayerMove;
import net.swofty.type.lobby.events.LobbyPlayerSpawnEvents;
import net.swofty.type.lobby.events.LobbyWorldEvent;
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
import net.swofty.type.mainlobby.item.impl.Collectibles;
import net.swofty.type.mainlobby.parkour.MainLobbyParkour;
import net.swofty.type.mainlobby.tab.MainLobbyPlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeMainLobbyLoader implements LobbyTypeLoader {
    public static LobbyParkourManager parkourManager;
    private final Pos spawnPoint = new Pos(-52.5, 97, 0.5, -90, 0);

    @Getter
    private final LobbyItemHandler itemHandler = new LobbyItemHandler();

    @Override
    public ServerType getType() {
        return ServerType.MAIN_LOBBY;
    }

    @Override
    public Parkour getParkour() {
        return new MainLobbyParkour();
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        MainLobbyScoreboard.start();

        // Register launch pads
        LaunchPadHandler.register(MinecraftServer.getSchedulerManager(), getLaunchPads());

        // Register all hotbar items
        getHotbarItems().values().forEach(itemHandler::add);

        // Register commands
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.mainlobby.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });

        // Schedule hologram updates every 2 seconds
        MinecraftServer.getSchedulerManager().buildTask(PlayerHolograms::updateExternalHolograms).delay(TaskSchedule.seconds(5))
            .repeat(TaskSchedule.seconds(2))
            .schedule();

        parkourManager = new LobbyParkourManager(getParkour());

        LobbyTypeLoader.registerLobbyCommands();
    }

    @Override
    public @Nullable LobbyParkourManager getParkourManager() {
        return parkourManager;
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
                    new MainLobbyPlayersOnlineModule(1),
                    new MainLobbyPlayersOnlineModule(2),
                    new MainLobbyPlayersOnlineModule(3),
                    new MainLobbyPlayersOnlineModule(4)
                );
            }
        };
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
            spawnPoint,
            false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        List<HypixelEventClass> events = new ArrayList<>(HypixelGenericLoader.loopThroughPackage(
            "net.swofty.type.mainlobby.events",
            HypixelEventClass.class
        ).toList());
        // Add lobby base events
        events.add(new LobbyAFKEvents());
        events.add(new LobbyItemEvents());
        events.add(new LobbyParkourEvents());
        events.add(new LobbyLaunchPadEvents());
        events.add(new LobbyPlayerJoinEvents());
        events.add(new LobbyPlayerSpawnEvents());
        events.add(new LobbyWorldEvent());
        events.add(new LobbyPlayerMove(spawnPoint));
        return events;
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
            "net.swofty.type.mainlobby.events.custom",
            HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
            "net.swofty.type.mainlobby.npcs",
            HypixelNPC.class
        ).toList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<TypedServiceHandler<?, ?>> getTypedServiceHandlers() {
        return (List) HypixelGenericLoader.loopThroughPackage(
            "net.swofty.type.mainlobby.redis.service",
            TypedServiceHandler.class
        ).toList();
    }

    @Override
    public List<TypedProxyHandler<?, ?>> getTypedProxyHandlers() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.MAIN_LOBBY;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(BedWarsDataHandler.class);
    }
}
