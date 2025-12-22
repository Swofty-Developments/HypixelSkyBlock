package net.swofty.type.generic;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public interface HypixelTypeLoader {
    ServerType getType();

    void onInitialize(MinecraftServer server);

    void afterInitialize(MinecraftServer server);

    List<ServiceType> getRequiredServices();

    TablistManager getTablistManager();

    LoaderValues getLoaderValues();

    List<HypixelEventClass> getTraditionalEvents();

    List<HypixelEventClass> getCustomEvents();

    List<HypixelNPC> getNPCs();

    List<ServiceToClient> getServiceRedisListeners();

    List<ProxyToClient> getProxyRedisListeners();

    record LoaderValues(Function<ServerType, Pos> spawnPosition, boolean announceDeathMessages) {}

    @Nullable CustomWorlds getMainInstance();

    /**
     * Gets the dimension type for this server type, or null if the default should be used. You should also register
     * the DimensionType to the registry in the same method.
     * @return The dimension type for this server type, or null if the default should be used.
     */
    @Nullable default RegistryKey<DimensionType> getDimensionType() {
        return null;
    }
}
