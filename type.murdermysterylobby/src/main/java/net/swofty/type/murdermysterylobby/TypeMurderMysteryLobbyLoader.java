package net.swofty.type.murdermysterylobby;

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
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.lobby.events.*;
import net.swofty.type.murdermysterylobby.hologram.LeaderboardHologramManager;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.murdermysterylobby.tab.MurderMysteryPlayersOnlineModule;
import net.swofty.type.murdermysterylobby.util.MurderMysteryLobbyMap;
import net.swofty.type.lobby.LobbyTypeLoader;
import net.swofty.type.lobby.item.LobbyItem;
import net.swofty.type.lobby.item.LobbyItemHandler;
import net.swofty.type.lobby.item.impl.HidePlayers;
import net.swofty.type.lobby.item.impl.LobbySelector;
import net.swofty.type.lobby.item.impl.PlayCompass;
import net.swofty.type.lobby.item.impl.ProfileItem;
import net.swofty.type.lobby.launchpad.LaunchPad;
import net.swofty.type.lobby.parkour.LobbyParkourManager;
import net.swofty.type.lobby.parkour.Parkour;
import net.swofty.type.murdermysterylobby.parkour.MurderMysteryLobbyParkour;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeMurderMysteryLobbyLoader implements LobbyTypeLoader {
    public static MurderMysteryLobbyMap lobbyMap = new MurderMysteryLobbyMap();
    private final Pos spawnPoint = new Pos(1.5, 72, 0.5, -90, 0);

    @Getter
    private final LobbyItemHandler itemHandler = new LobbyItemHandler();
    @Getter
    private LobbyParkourManager parkourManager;

    @Override
    public ServerType getType() {
        return ServerType.MURDER_MYSTERY_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        MurderMysteryLobbyScoreboard.start();
        lobbyMap.placeItemFrames(HypixelConst.getInstanceContainer());

        // Initialize parkour
        parkourManager = new LobbyParkourManager(getParkour());

        // Register all hotbar items
        getHotbarItems().values().forEach(itemHandler::add);

        // Register commands
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.murdermysterylobby.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });

        // Initialize leaderboard holograms
        LeaderboardHologramManager.initialize(HypixelConst.getInstanceContainer());

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
                7, new HidePlayers(),
                8, new LobbySelector()
        );
    }

    @Override
    public Parkour getParkour() {
        return new MurderMysteryLobbyParkour();
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
                (type) -> spawnPoint,
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        List<HypixelEventClass> events = new ArrayList<>(HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterylobby.events",
                HypixelEventClass.class
        ).toList());
        // Add lobby base events
        events.add(new LobbyItemEvents());
        events.add(new LobbyLaunchPadEvents());
        events.add(new LobbyPlayerJoinEvents());
        events.add(new LobbyBlockBreak());
        events.add(new LobbyParkourEvents());
        events.add(new LobbyPlayerMove(spawnPoint));
        return events;
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterylobby.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterylobby.npcs",
                HypixelNPC.class
        ).toList();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterylobby.redis.service",
                ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.MURDER_MYSTERY_LOBBY;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(MurderMysteryDataHandler.class);
    }
}
