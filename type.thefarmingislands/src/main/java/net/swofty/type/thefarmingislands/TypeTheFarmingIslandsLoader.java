package net.swofty.type.thefarmingislands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.type.thefarmingislands.tab.TheFarmingIslandsServerModule;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.type.skyblockgeneric.entity.npc.SkyBlockNPC;
import net.swofty.type.skyblockgeneric.entity.villager.SkyBlockVillagerNPC;
import net.swofty.type.skyblockgeneric.event.SkyBlockEventClass;
import net.swofty.type.skyblockgeneric.tab.TablistManager;
import net.swofty.type.skyblockgeneric.tab.TablistModule;
import net.swofty.type.skyblockgeneric.tab.modules.AccountInformationModule;
import net.swofty.type.skyblockgeneric.tab.modules.PlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeTheFarmingIslandsLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_THE_FARMING_ISLANDS;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeTheFarmingIslandsLoader initialized!");
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
                        new TheFarmingIslandsServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<SkyBlockEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.thefarmingislands.events",
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
                "net.swofty.type.thefarmingislands.npcs",
                SkyBlockNPC.class
        ).toList());

        return npcs;
    }

    @Override
    public List<SkyBlockVillagerNPC> getVillagerNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.thefarmingislands.villagers",
                SkyBlockVillagerNPC.class
        ).toList());
    }

    @Override
    public List<SkyBlockAnimalNPC> getAnimalNPCs() {
        return new ArrayList<>();
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return new ArrayList<>(List.of(ServiceType.DATA_MUTEX));
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.SKYBLOCK_HUB;
    }
}
