package net.swofty.type.dungeonhub;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.type.dungeonhub.tab.DungeonServerModule;
import net.swofty.type.generic.SkyBlockGenericLoader;
import net.swofty.type.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.type.generic.entity.npc.SkyBlockNPC;
import net.swofty.type.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.type.generic.event.SkyBlockEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.tab.modules.AccountInformationModule;
import net.swofty.type.generic.tab.modules.PlayersOnlineModule;

import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeDungeonHubLoader implements SkyBlockTypeLoader {

    @Override
    public ServerType getType() {
        return ServerType.DUNGEON_HUB;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeDungeonHubLoader initialized!");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {

    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> switch (type) {
                    default -> new Pos(-28.5, 121, 0.5, 90, 0);
                }, // Spawn position
                false // Announce death messages
        );
    }

    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(
                        new PlayersOnlineModule(1),
                        new PlayersOnlineModule(2),
                        new DungeonServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<SkyBlockEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.dungeonhub.events",
                SkyBlockEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<SkyBlockEventClass> getCustomEvents() {
        return new ArrayList<>();
    }

    @Override
    public List<SkyBlockNPC> getNPCs() {
        List<SkyBlockNPC> npcs = new ArrayList<>();

        npcs.addAll(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.dungeonhub.npcs",
                SkyBlockNPC.class
        ).toList());

        return npcs;
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.ITEM_TRACKER, ServiceType.DATA_MUTEX);
    }

    @Override
    public List<SkyBlockVillagerNPC> getVillagerNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.dungeonhub.villagers",
                SkyBlockVillagerNPC.class
        ).toList());
    }

    @Override
    public List<SkyBlockAnimalNPC> getAnimalNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.dungeonhub.animalnpcs",
                SkyBlockAnimalNPC.class
        ).toList());
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.DUNGEON_HUB;
    }
}
