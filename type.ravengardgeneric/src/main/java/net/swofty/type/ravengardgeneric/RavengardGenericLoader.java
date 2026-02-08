package net.swofty.type.ravengardgeneric;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.packet.HypixelPacketClientListener;
import net.swofty.type.generic.packet.HypixelPacketServerListener;
import net.swofty.type.generic.redis.RedisOriginServer;
import net.swofty.type.generic.resourcepack.ResourcePackManager;
import net.swofty.type.ravengardgeneric.resourcepack.TestingPack;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public record RavengardGenericLoader(HypixelTypeLoader typeLoader) {

    @Getter
    private static MinecraftServer server;

    @SneakyThrows
    public void initialize(MinecraftServer server) {
        RavengardGenericLoader.server = server;

        loopThroughPackage("net.swofty.type.ravengardgeneric.event.actions", HypixelEventClass.class)
                .forEach(HypixelEventHandler::registerEventMethods);

        loopThroughPackage("net.swofty.type.ravengardgeneric.commands", HypixelCommand.class)
                .forEach(command -> {
                    try {
                        MinecraftServer.getCommandManager().register(command.getCommand());
                    } catch (Exception e) {
                        Logger.error(e, "Failed to register command {} in class {}",
                                command.getCommand().getName(), command.getClass().getSimpleName());
                    }
                });

        loopThroughPackage("net.swofty.type.ravengardgeneric.packets.client", HypixelPacketClientListener.class)
                .forEach(HypixelPacketClientListener::cacheListener);
        loopThroughPackage("net.swofty.type.ravengardgeneric.packets.server", HypixelPacketServerListener.class)
                .forEach(HypixelPacketServerListener::cacheListener);

        HypixelPacketClientListener.register(HypixelConst.getEventHandler());
        HypixelPacketServerListener.register(HypixelConst.getEventHandler());
        HypixelEventHandler.register(HypixelConst.getEventHandler());

        TestingPack testingPack = TestingPack.fromConfig();
        ResourcePackManager packManager = new ResourcePackManager(testingPack);
        HypixelConst.setResourcePackManager(packManager);
        packManager.initialize();

        MinecraftServer.getConnectionManager().setPlayerProvider((playerConnection, gameProfile) -> {
            RavengardPlayer player = new RavengardPlayer(playerConnection, gameProfile);

            UUID uuid = playerConnection.getPlayer().getUuid();
            String username = playerConnection.getPlayer().getUsername();

            if (RedisOriginServer.origin.containsKey(uuid)) {
                player.setOriginServer(RedisOriginServer.origin.get(uuid));
                RedisOriginServer.origin.remove(uuid);
            }

            Logger.info("Received new Ravengard player: {} ({})", username, uuid);
            return player;
        });
    }

    public static List<RavengardPlayer> getLoadedPlayers() {
        List<RavengardPlayer> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .filter(player -> player instanceof RavengardPlayer)
                .filter(player -> player.getInstance() != null)
                .forEach(player -> players.add((RavengardPlayer) player));
        return players;
    }

    public static @Nullable RavengardPlayer getFromUUID(UUID uuid) {
        return getLoadedPlayers().stream()
                .filter(player -> player.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}
