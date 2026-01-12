package net.swofty.type.prototypelobby;

import io.sentry.Sentry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.handlers.PrototypeLobbyDataHandler;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.EmptyTabModule;
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
import net.swofty.type.prototypelobby.parkour.PrototypeLobbyParkour;
import net.swofty.type.prototypelobby.util.PrototypeLobbyMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypePrototypeLobbyLoader implements LobbyTypeLoader {
    private static final LobbyItemHandler itemHandler = new LobbyItemHandler();
    public static LobbyParkourManager parkourManager;
    private final Pos spawnPoint = new Pos(11.5, 76, 0.5, 90, 0);

    @Override
    public ServerType getType() {
        return ServerType.PROTOTYPE_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        PrototypeLobbyScoreboard.start();

        PrototypeLobbyMap prototypeLobbyMap = new PrototypeLobbyMap();
        prototypeLobbyMap.placeItemFrames(HypixelConst.getInstanceContainer());

        // Register all hotbar items
        getHotbarItems().values().forEach(itemHandler::add);

        parkourManager = new LobbyParkourManager(getParkour());

        LobbyTypeLoader.registerLobbyCommands();
    }

    @Override
    public Parkour getParkour() {
        return new PrototypeLobbyParkour();
    }

    @Override
    public @Nullable LobbyParkourManager getParkourManager() {
        return parkourManager;
    }

    @Override
    public LobbyItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public List<LaunchPad> getLaunchPads() {
        return List.of(); // No launch pads for prototype lobby
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
    public List<ServiceType> getRequiredServices() {
        return List.of();
    }

    @Override
    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return List.of(
                        new EmptyTabModule(),
                        new EmptyTabModule(),
                        new EmptyTabModule(),
                        new EmptyTabModule()
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
                "net.swofty.type.prototypelobby.events",
                HypixelEventClass.class
        ).toList());
        // Add lobby base events
        events.add(new LobbyItemEvents());
        events.add(new LobbyPlayerJoinEvents());
        events.add(new LobbyParkourEvents());
        events.add(new LobbyBlockBreak());
        events.add(new LobbyPlayerMove(spawnPoint));
        return events;
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.prototypelobby.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.prototypelobby.npcs",
                HypixelNPC.class
        ).toList();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.prototypelobby.redis.service",
                ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(PrototypeLobbyDataHandler.class);
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.PROTOTYPE_LOBBY;
    }
}
