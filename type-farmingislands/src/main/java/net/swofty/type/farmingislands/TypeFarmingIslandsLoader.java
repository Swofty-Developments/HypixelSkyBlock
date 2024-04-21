package net.swofty.type.farmingislands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.type.farmingislands.tab.FarmingIslandsServerModule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.types.generic.entity.mob.MobRegistry;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.tab.TablistManager;
import net.swofty.types.generic.tab.TablistModule;
import net.swofty.types.generic.tab.modules.AccountInformationModule;
import net.swofty.types.generic.tab.modules.PlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeFarmingIslandsLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.FARMING_ISLANDS;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeFarmingIslandsLoader initialized!");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {

    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> switch (type) {
                    default -> new Pos(115, 71, -208.5, -130, 0);
                }, // Spawn position
                true // Announce death messages
        );
    }

    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(
                        new PlayersOnlineModule(1),
                        new PlayersOnlineModule(2),
                        new FarmingIslandsServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<SkyBlockEvent> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.farmingislands.events",
                SkyBlockEvent.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<MobRegistry> getMobs() {
        return new ArrayList<>();
    }

    @Override
    public List<SkyBlockEvent> getCustomEvents() {
        return new ArrayList<>();
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
        return new ArrayList<>();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.HUB;
    }
}
