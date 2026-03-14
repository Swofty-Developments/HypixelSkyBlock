package net.swofty.type.ravengardlobby;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.RavengardTypeLoader;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.EmptyTabModule;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.ravengardgeneric.RavengardGenericLoader;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeRavengardLobbyLoader implements RavengardTypeLoader {

    @Override
    public ServerType getType() {
        return ServerType.RAVENGARD_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        RegistryKey<DimensionType> fullbrightDimension = MinecraftServer.getDimensionTypeRegistry().register(
                Key.key("ravengard:lobby_fullbright"),
                DimensionType.builder().ambientLight(1.0f).build()
        );

        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer(fullbrightDimension);
        instance.setTime(6000);
        instance.setTimeRate(0);

        HypixelConst.setInstanceContainer(MinecraftServer.getInstanceManager().createSharedInstance(instance));
        int minimapRadiusChunks = 8;
        for (int chunkX = -minimapRadiusChunks; chunkX <= minimapRadiusChunks; chunkX++) {
            for (int chunkZ = -minimapRadiusChunks; chunkZ <= minimapRadiusChunks; chunkZ++) {
                HypixelConst.getInstanceContainer().loadChunk(chunkX, chunkZ).join();
            }
        }

        int minimapRadiusBlocks = 128;
        for (int x = -minimapRadiusBlocks; x <= minimapRadiusBlocks; x++) {
            for (int z = -minimapRadiusBlocks; z <= minimapRadiusBlocks; z++) {
                HypixelConst.getInstanceContainer().setBlock(x, 64, z, Block.STONE);
            }
        }

        Logger.info("TypeRavengardLobbyLoader initialized with preloaded minimap terrain");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
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
                (_) -> new Pos(0.5, 65, 0.5, 0, 0),
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return RavengardGenericLoader.loopThroughPackage(
                "net.swofty.type.ravengardlobby.events",
                HypixelEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return new ArrayList<>();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return new ArrayList<>();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return List.of();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }
}
