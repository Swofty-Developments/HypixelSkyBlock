package net.swofty.type.ravengardlobby;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.advancements.AdvancementRoot;
import net.minestom.server.advancements.AdvancementTab;
import net.minestom.server.advancements.FrameType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.generic.RavengardTypeLoader;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.EmptyTabModule;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.ravengardgeneric.RavengardGenericLoader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeRavengardLobbyLoader implements RavengardTypeLoader {

    // the tab that is used to detect when player opens the advancement menu
    public static AdvancementTab detectorTab;

    @Override
    public ServerType getType() {
        return ServerType.RAVENGARD_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        var advancementManager = MinecraftServer.getAdvancementManager();

        // TODO: use recreationmod "/recreation copyadvancements" to make this look the same as theirs.
        AdvancementRoot root = new AdvancementRoot(
                Component.text("."),
                Component.text("."),
                Material.COMPASS,
                FrameType.TASK,
                0f,
                0f,
                "minecraft:textures/gui/advancements/backgrounds/stone.png"
        );

        detectorTab = advancementManager.createTab(
                "test:t",
                root
        );
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
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.RAVENGARD_LOBBY;
    }
}
