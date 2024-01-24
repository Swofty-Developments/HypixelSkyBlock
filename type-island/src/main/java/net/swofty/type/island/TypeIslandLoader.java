package net.swofty.type.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.type.island.tab.IslandGuestsModule;
import net.swofty.type.island.tab.IslandMemberModule;
import net.swofty.type.island.tab.IslandServerModule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.types.generic.entity.mob.MobRegistry;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.minion.MinionHandler;
import net.swofty.types.generic.tab.TablistManager;
import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.modules.AccountInformationModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeIslandLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.ISLAND;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeIslandLoader initialized!");

        /**
         * Initialize Minions
         */
        new MinionHandler(MinecraftServer.getSchedulerManager()).start();
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                new Pos(0, 100 ,0), // Spawn position
                true // Announce death messages
        );
    }

    @Override
    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(
                        new IslandMemberModule(),
                        new IslandGuestsModule(),
                        new IslandServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<SkyBlockEvent> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.island.events.traditional",
                SkyBlockEvent.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<MobRegistry> getMobs() {
        return new ArrayList<>();
    }

    @Override
    public List<SkyBlockEvent> getCustomEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.island.events.custom",
                SkyBlockEvent.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<SkyBlockNPC> getNPCs() {
        return new ArrayList<>();
    }

    @Override
    public List<SkyBlockVillagerNPC> getVillagerNPCs() {
        return new ArrayList<>();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }
}
