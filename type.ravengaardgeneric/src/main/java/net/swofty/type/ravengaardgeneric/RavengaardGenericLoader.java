package net.swofty.type.ravengaardgeneric;

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
import net.swofty.type.ravengaardgeneric.resourcepack.TestingPack;
import net.swofty.type.ravengaardgeneric.user.RavengaardPlayer;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public record RavengaardGenericLoader(HypixelTypeLoader typeLoader) {

    @Getter
    private static MinecraftServer server;

    @SneakyThrows
    public void initialize(MinecraftServer server) {
        RavengaardGenericLoader.server = server;

        loopThroughPackage("net.swofty.type.ravengaardgeneric.event.actions", HypixelEventClass.class)
                .forEach(HypixelEventHandler::registerEventMethods);

        loopThroughPackage("net.swofty.type.ravengaardgeneric.commands", HypixelCommand.class)
                .forEach(command -> {
                    try {
                        MinecraftServer.getCommandManager().register(command.getCommand());
                    } catch (Exception e) {
                        Logger.error(e, "Failed to register command {} in class {}",
                                command.getCommand().getName(), command.getClass().getSimpleName());
                    }
                });

        loopThroughPackage("net.swofty.type.ravengaardgeneric.packets.client", HypixelPacketClientListener.class)
                .forEach(HypixelPacketClientListener::cacheListener);
        loopThroughPackage("net.swofty.type.ravengaardgeneric.packets.server", HypixelPacketServerListener.class)
                .forEach(HypixelPacketServerListener::cacheListener);

        HypixelPacketClientListener.register(HypixelConst.getEventHandler());
        HypixelPacketServerListener.register(HypixelConst.getEventHandler());
        HypixelEventHandler.register(HypixelConst.getEventHandler());

        TestingPack testingPack = TestingPack.fromConfig();
        ResourcePackManager packManager = new ResourcePackManager(testingPack);
        HypixelConst.setResourcePackManager(packManager);
        packManager.initialize();

        MinecraftServer.getConnectionManager().setPlayerProvider((playerConnection, gameProfile) -> {
            RavengaardPlayer player = new RavengaardPlayer(playerConnection, gameProfile);

            UUID uuid = playerConnection.getPlayer().getUuid();
            String username = playerConnection.getPlayer().getUsername();

            if (RedisOriginServer.origin.containsKey(uuid)) {
                player.setOriginServer(RedisOriginServer.origin.get(uuid));
                RedisOriginServer.origin.remove(uuid);
            }

            Logger.info("Received new Ravengaard player: {} ({})", username, uuid);
            return player;
        });
    }

    public static List<RavengaardPlayer> getLoadedPlayers() {
        List<RavengaardPlayer> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .filter(player -> player instanceof RavengaardPlayer)
                .filter(player -> player.getInstance() != null)
                .forEach(player -> players.add((RavengaardPlayer) player));
        return players;
    }

    public static @Nullable RavengaardPlayer getFromUUID(UUID uuid) {
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
