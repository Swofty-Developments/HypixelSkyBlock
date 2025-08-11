package net.swofty.type.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.type.island.tab.IslandGuestsModule;
import net.swofty.type.island.tab.IslandMemberModule;
import net.swofty.type.island.tab.IslandServerModule;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.type.skyblockgeneric.entity.npc.SkyBlockNPC;
import net.swofty.type.skyblockgeneric.entity.villager.SkyBlockVillagerNPC;
import net.swofty.type.skyblockgeneric.event.SkyBlockEventClass;
import net.swofty.type.skyblockgeneric.minion.MinionHandler;
import net.swofty.type.skyblockgeneric.tab.TablistManager;
import net.swofty.type.skyblockgeneric.tab.TablistModule;
import net.swofty.type.skyblockgeneric.tab.modules.AccountInformationModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeIslandLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_ISLAND;
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
    public void afterInitialize(MinecraftServer server) {

    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> new Pos(0, 100 ,0), // Spawn position
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
    public List<SkyBlockEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.island.events.traditional",
                SkyBlockEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<SkyBlockEventClass> getCustomEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.island.events.custom",
                SkyBlockEventClass.class
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
    public List<SkyBlockAnimalNPC> getAnimalNPCs() {
        return new ArrayList<>();
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.AUCTION_HOUSE, ServiceType.ITEM_TRACKER, ServiceType.DATA_MUTEX);
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }
}
