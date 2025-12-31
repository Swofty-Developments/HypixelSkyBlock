package net.swofty.type.murdermysteryconfigurator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.EmptyTabModule;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TypeMurderMysteryConfiguratorLoader implements HypixelTypeLoader {
    private static Gson gson;

    @Getter
    private static MurderMysteryMapsConfig mapsConfig;

    @Override
    public ServerType getType() {
        return ServerType.MURDER_MYSTERY_CONFIGURATOR;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        gson = new GsonBuilder().create();
        loadMapsConfig();
    }

    private static void loadMapsConfig() {
        Path mapsPath = Path.of("./configuration/murdermystery/maps.json");
        if (!Files.exists(mapsPath)) {
            Logger.warn("maps.json not found at {}, creating empty config", mapsPath.toAbsolutePath());
            mapsConfig = new MurderMysteryMapsConfig();
            return;
        }
        try (InputStream in = Files.newInputStream(mapsPath)) {
            byte[] bytes = in.readAllBytes();
            String json = new String(bytes, StandardCharsets.UTF_8);
            mapsConfig = gson.fromJson(json, MurderMysteryMapsConfig.class);
            if (mapsConfig == null || mapsConfig.getMaps() == null) {
                mapsConfig = new MurderMysteryMapsConfig();
            }
        } catch (Exception e) {
            Logger.error("Failed to load maps.json", e);
            mapsConfig = new MurderMysteryMapsConfig();
        }
    }

    /**
     * Reloads the maps configuration from file
     */
    public static void reloadMapsConfig() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        loadMapsConfig();
        Logger.info("Reloaded maps.json configuration");
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return gson;
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.murdermysteryconfigurator.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });
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
                (type) -> new Pos(0, 100, 0, -90, 0), // Spawn position
                false // Announce death messages
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysteryconfigurator.events",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysteryconfigurator.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysteryconfigurator.npcs",
                HypixelNPC.class
        ).toList();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysteryconfigurator.redis.service",
                ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.MURDER_MYSTERY_LOBBY;
    }
}
