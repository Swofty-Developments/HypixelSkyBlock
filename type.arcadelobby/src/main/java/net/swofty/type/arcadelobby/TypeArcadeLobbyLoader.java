package net.swofty.type.arcadelobby;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.arcadelobby.impl.Collectibles;
import net.swofty.type.arcadelobby.impl.ShopsCosmetics;
import net.swofty.type.arcadelobby.launchpad.ArcadeLaunchPads;
import net.swofty.type.arcadelobby.tab.tab.ArcadePlayersOnlineModule;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.handlers.ArcadeDataHandler;
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
import net.swofty.type.lobby.launchpad.LaunchPadHandler;
import net.swofty.type.lobby.parkour.LobbyParkourManager;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TypeArcadeLobbyLoader implements LobbyTypeLoader {

    private final Pos spawnPoint = new Pos(-18.5, 67, 0.5, -90, 0);

    @Getter
    private final LobbyItemHandler itemHandler = new LobbyItemHandler();

    @Override
    public ServerType getType() {
        return ServerType.ARCADE_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        ArcadeLobbyScoreboard.start();
        LaunchPadHandler.register(MinecraftServer.getSchedulerManager(), getLaunchPads());

        getHotbarItems().values().forEach(itemHandler::add);

        HypixelGenericLoader.loopThroughPackage("net.swofty.type.arcadelobby.commands", HypixelCommand.class).forEach(command -> {
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

        LobbyTypeLoader.registerLobbyCommands();
    }

    @Override
    public List<LaunchPad> getLaunchPads() {
        return Arrays.asList(ArcadeLaunchPads.values());
    }

    @Override
    public @Nullable LobbyParkourManager getParkourManager() {
        return null;
    }

    @Override
    public Map<Integer, LobbyItem> getHotbarItems() {
        return Map.of(
                0, new PlayCompass(),
                1, new ProfileItem(),
                2, new ShopsCosmetics(),
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
                        new ArcadePlayersOnlineModule(1),
                        new ArcadePlayersOnlineModule(2),
                        new ArcadePlayersOnlineModule(3),
                        new ArcadePlayersOnlineModule(4)
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
                "net.swofty.type.arcadelobby.events",
                HypixelEventClass.class
        ).toList());
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
                "net.swofty.type.arcadelobby.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.arcadelobby.npcs",
                HypixelNPC.class
        ).toList();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.arcadelobby.redis.service",
                ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.ARCADE_LOBBY;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(ArcadeDataHandler.class);
    }
}
