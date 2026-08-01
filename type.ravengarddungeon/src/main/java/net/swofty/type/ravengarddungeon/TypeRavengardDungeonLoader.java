package net.swofty.type.ravengarddungeon;

import com.google.gson.Gson;
import net.hollowcube.polar.PolarLoader;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.Tuple;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.RavengardTypeLoader;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.ravengarddungeon.config.RavengardDungeonConfig;
import net.swofty.type.ravengarddungeon.events.ActionPlayerJoin;
import net.swofty.type.ravengarddungeon.game.RavengardDungeonGame;
import net.swofty.type.ravengarddungeon.user.RavengardDungeonPlayer;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TypeRavengardDungeonLoader implements RavengardTypeLoader {
    private static final Path CONFIGURATION = Path.of("./configuration/ravengard/dungeon1.json");

    private static RavengardDungeonGame game;
    private RavengardDungeonConfig config;

    @Override
    public ServerType getType() {
        return ServerType.RAVENGARD_DUNGEON;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        try {
            config = new Gson().fromJson(Files.readString(CONFIGURATION), RavengardDungeonConfig.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load " + CONFIGURATION, exception);
        }
        Path polarPath = CONFIGURATION.getParent().resolve(config.polar());
        if (!Files.isRegularFile(polarPath)) {
            throw new IllegalStateException("Ravengard dungeon map not found: " + polarPath);
        }
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        try {
            instance.setChunkLoader(new PolarLoader(polarPath));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load Ravengard dungeon map: " + polarPath, exception);
        }
        game = new RavengardDungeonGame(instance, config);
        MinecraftServer.getConnectionManager().setPlayerProvider((connection, profile) ->
                new RavengardDungeonPlayer(connection, profile));
    }

    public static RavengardDungeonGame getGame() {
        return game;
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
                return List.of();
            }
        };
    }

    @Override
    public HypixelTypeLoader.LoaderValues getLoaderValues() {
        Pos spawn = config == null ? new Pos(0.5, 65, 0.5) : config.spawnPosition();
        return new HypixelTypeLoader.LoaderValues(spawn, false);
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return List.of(new ActionPlayerJoin());
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
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }

    @Override
    public boolean headerFooterPerPlayer() {
        return true;
    }

    @Override
    public @NonNull Optional<Tuple<Component, Component>> headerFooter() {
        return Optional.of(new Tuple<>(Component.empty(), Component.empty()));
    }
}
