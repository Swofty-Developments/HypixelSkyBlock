package net.swofty.type.generic;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.type.generic.entity.animalnpc.HypixelAnimalNPC;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.tab.TablistManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public interface HypixelTypeLoader {
    ServerType getType();

    void onInitialize(MinecraftServer server);

    void afterInitialize(MinecraftServer server);

    List<ServiceType> getRequiredServices();

    TablistManager getTablistManager();

    LoaderValues getLoaderValues();

    List<HypixelEventClass> getTraditionalEvents();

    List<HypixelEventClass> getCustomEvents();

    List<HypixelVillagerNPC> getVillagerNPCs();

    List<HypixelAnimalNPC> getAnimalNPCs();

    List<HypixelNPC> getNPCs();

    record LoaderValues(Function<ServerType, Pos> spawnPosition, boolean announceDeathMessages) { }

    @Nullable CustomWorlds getMainInstance();
}
