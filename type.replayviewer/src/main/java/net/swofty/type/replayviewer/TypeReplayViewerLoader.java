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
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.item.ReplayItemHandler;
import net.swofty.type.replayviewer.playback.ReplaySession;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TypeReplayViewerLoader implements HypixelTypeLoader {

    @Getter
    private static InstanceManager instanceManager;

    @Getter
    private static ReplayItemHandler itemHandler = new ReplayItemHandler();

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
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.replayviewer.command", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command {} in class {}",
                    command.getCommand().getName(), command.getClass().getSimpleName());
            }
        });
    }

    private static void setItem(HypixelPlayer player, String key, int slot) {
        itemHandler.getItem(key).ifPresent(item -> player.getInventory().setItemStack(slot, item.getItemStack(player)));
    }

    public static void populateInventory(HypixelPlayer player) {
        setItem(player, "teleporter", 0);
        setItem(player, "slower", 2);
        setItem(player, "backward", 3);
        setItem(player, "playback", 4);
        setItem(player, "faster", 5);
        setItem(player, "forward", 6);
        setItem(player, "menu", 8);
    }


    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.REPLAY);
    }

    @Override
    public TablistManager getTablistManager() {
        return TablistManager.create(
            List.of(new ReplayTablistModule())
        );
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
        return HypixelGenericLoader.loopThroughPackage(
            "net.swofty.type.replayviewer.redis.service",
            ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null; // Each replay creates its own instance
    }

    public static Optional<ReplaySession> getSession(UUID playerId) {
        return Optional.ofNullable(activeSessions.get(playerId));
    }

    public static Optional<ReplaySession> getSession(Player player) {
        return getSession(player.getUuid());
    }

    public static void registerSession(UUID playerId, ReplaySession session) {
        removeSession(playerId);
        activeSessions.put(playerId, session);
    }

    public static void removeSession(UUID playerId) {
        ReplaySession session = activeSessions.remove(playerId);
        if (session != null) {
            session.stop();
        }
    }
}
