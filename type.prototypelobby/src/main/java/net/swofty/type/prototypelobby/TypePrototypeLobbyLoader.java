package net.swofty.type.prototypelobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.HypixelTypeLoader;

import net.swofty.type.generic.entity.npc.HypixelNPC;

import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.EmptyTabModule;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.prototypelobby.util.PrototypeLobbyMap;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TypePrototypeLobbyLoader implements HypixelTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.PROTOTYPE_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        /**
         * Start Prototype Lobby Scoreboard
         */
        PrototypeLobbyScoreboard.start();

        PrototypeLobbyMap prototypeLobbyMap = new PrototypeLobbyMap();
        prototypeLobbyMap.placeItemFrames(HypixelConst.getInstanceContainer());
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
                (type) -> new Pos(11.5, 76, 0.5, 90, 0), // Spawn position
                false // Announce death messages
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.prototypelobby.events",
                HypixelEventClass.class
        ).toList();
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
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.PROTOTYPE_LOBBY;
    }
}
