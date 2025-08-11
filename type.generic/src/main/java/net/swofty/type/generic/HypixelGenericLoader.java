package net.swofty.type.generic;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.Configuration;
import net.swofty.commons.CustomWorlds;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.mongodb.AttributeDatabase;
import net.swofty.type.generic.data.mongodb.AuthenticationDatabase;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.entity.animalnpc.HypixelAnimalNPC;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public record HypixelGenericLoader(HypixelTypeLoader loader) {
    @Getter
    private static MinecraftServer server;

    @SneakyThrows
    public void initialize(MinecraftServer server) {
        HypixelGenericLoader.server = server;
        HypixelConst.setTypeLoader(loader);
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();

        /**
         * Handle instances
         */
        CustomWorlds mainInstance = loader.getMainInstance();
        if (mainInstance != null) {
            InstanceContainer temporaryInstance = instanceManager.createInstanceContainer();
            temporaryInstance.setChunkLoader(new AnvilLoader(loader.getMainInstance().getFolderName()));

            HypixelConst.setInstanceContainer(instanceManager.createSharedInstance(temporaryInstance));
        }
        HypixelConst.setEmptyInstance(instanceManager.createSharedInstance(instanceManager.createInstanceContainer()));
        HypixelConst.getEmptyInstance().setBlock(0, 99, 0, Block.BEDROCK);
        HypixelConst.setEventHandler(MinecraftServer.getGlobalEventHandler());

        /**
         * Register commands
         */
        MinecraftServer.getCommandManager().setUnknownCommandCallback((sender, command) -> {
            // Large amount of Clients (such as Lunar) send a `/tip all` when joining
            // due to the scoreboard containing `hypixel.net`
            if (command.startsWith("tip ")) return;
            sender.sendMessage("§fUnknown command. Type \"/help\" for help. ('" + command + "')");
        });
        loopThroughPackage("net.swofty.type.generic.command.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error("Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
                e.printStackTrace();
            }
        });

        /**
         * Register events
         */
        loader.getTraditionalEvents().forEach(HypixelEventHandler::registerEventMethods);
        loader.getCustomEvents().forEach(HypixelEventHandler::registerEventMethods);
        loopThroughPackage("net.swofty.type.generic.event.actions", HypixelEventClass.class).forEach(HypixelEventHandler::registerEventMethods);
        // SkyBlockGenericLoader always runs after the generic loader, so if we are a SkyBlock server,
        // we will let that loader register the events
        if (!loader.getType().isSkyBlock()) {
            HypixelEventHandler.register(HypixelConst.getEventHandler());
        }

        /**
         * Register databases
         */
        ConnectionString cs = new ConnectionString(Configuration.get("mongodb"));
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        MongoClient mongoClient = MongoClients.create(settings);

        AuthenticationDatabase.connect(mongoClient);
        ProfilesDatabase.connect(mongoClient);
        AttributeDatabase.connect(mongoClient);
        UserDatabase.connect(mongoClient);

        /**
         * Register Hypixel NPCs
         */
        if (mainInstance != null) {
            loader.getNPCs().forEach(HypixelNPC::register);
            loader.getVillagerNPCs().forEach(HypixelVillagerNPC::register);
            loader.getAnimalNPCs().forEach(HypixelAnimalNPC::register);
        }
    }

    public static List<HypixelPlayer> getLoadedPlayers() {
        List<HypixelPlayer> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .filter(player -> {
                    try {
                        HypixelDataHandler.getUser(player);
                    } catch (Exception e) {
                        return false;
                    }
                    return true;
                })
                .filter(player -> player.getInstance() != null)
                .forEach(player -> players.add((HypixelPlayer) player));
        return players;
    }

    public static @Nullable HypixelPlayer getFromUUID(UUID uuid) {
        return getLoadedPlayers().stream().filter(player -> player.getUuid().toString().equalsIgnoreCase(uuid.toString())).findFirst().orElse(null);
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
