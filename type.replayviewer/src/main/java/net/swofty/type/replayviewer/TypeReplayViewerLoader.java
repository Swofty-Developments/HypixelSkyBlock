package net.swofty.type.replayviewer;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceManager;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.replayviewer.playback.ReplaySession;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Replay Viewer Server Type.
 * Players are sent here to watch replays.
 */
public class TypeReplayViewerLoader implements HypixelTypeLoader {

    @Getter
	private static InstanceManager instanceManager;

	// Active replay sessions per player
    @Getter
    private static final Map<UUID, ReplaySession> activeSessions = new ConcurrentHashMap<>();

    @Override
    public ServerType getType() {
        return ServerType.REPLAY_VIEWER;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        instanceManager = MinecraftServer.getInstanceManager();
        Logger.info("Replay Viewer initialized");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.REPLAY);
    }

    @Override
    public TablistManager getTablistManager() {
        return new ReplayTablistManager();
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                serverType -> new Pos(0, 100, 0),
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return new ArrayList<>(HypixelGenericLoader.loopThroughPackage(
            "net.swofty.type.replayviewer.event",
            HypixelEventClass.class
        ).toList());
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return List.of();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return List.of();
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
        return null; // Each replay creates its own instance
    }

    /**
     * Gets a player's active replay session.
     */
    public static Optional<ReplaySession> getSession(UUID playerId) {
        return Optional.ofNullable(activeSessions.get(playerId));
    }

    /**
     * Gets a player's active replay session.
     */
    public static Optional<ReplaySession> getSession(Player player) {
        return getSession(player.getUuid());
    }

    /**
     * Registers a new replay session for a player.
     */
    public static void registerSession(UUID playerId, ReplaySession session) {
        // Clean up any existing session
        ReplaySession existing = activeSessions.remove(playerId);
        if (existing != null) {
            existing.stop();
        }
        activeSessions.put(playerId, session);
    }

    /**
     * Removes a player's replay session.
     */
    public static void removeSession(UUID playerId) {
        ReplaySession session = activeSessions.remove(playerId);
        if (session != null) {
            session.stop();
        }
    }
}
