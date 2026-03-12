package net.swofty.type.garden;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.garden.npc.NPCAnita;
import net.swofty.type.garden.npc.NPCCarpenter;
import net.swofty.type.garden.npc.NPCDesk;
import net.swofty.type.garden.npc.NPCJacob;
import net.swofty.type.garden.npc.NPCJeff;
import net.swofty.type.garden.npc.NPCPesthunterPhillip;
import net.swofty.type.garden.npc.NPCSam;
import net.swofty.type.garden.npc.NPCShifty;
import net.swofty.type.garden.pest.GardenPestRuntime;
import net.swofty.type.garden.user.SkyBlockGarden;
import net.swofty.type.garden.visitor.GardenVisitorRuntime;
import net.swofty.type.generic.SkyBlockTypeLoader;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.tabmodules.AccountInformationModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeGardenLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_GARDEN;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeGardenLoader initialized!");
        GardenServices.initialize();
        GardenPestRuntime.start(MinecraftServer.getSchedulerManager());
        GardenVisitorRuntime.start(MinecraftServer.getSchedulerManager());
        SkyBlockGarden.runVacantLoop(MinecraftServer.getSchedulerManager());
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues((type) -> new Pos(0, 100, 0), true);
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(
            ServiceType.AUCTION_HOUSE,
            ServiceType.ITEM_TRACKER,
            ServiceType.DATA_MUTEX,
            ServiceType.JACOBS_CONTEST
        );
    }

    @Override
    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(new AccountInformationModule()));
            }
        };
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
            "net.swofty.type.garden.events.traditional",
            HypixelEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
            "net.swofty.type.garden.events.custom",
            HypixelEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return List.of(
            new NPCDesk(),
            new NPCSam(),
            new NPCAnita(),
            new NPCJacob(),
            new NPCJeff(),
            new NPCPesthunterPhillip(),
            new NPCCarpenter(),
            new NPCShifty()
        );
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return List.of();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }
}
