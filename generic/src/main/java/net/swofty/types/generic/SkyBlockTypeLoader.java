package net.swofty.types.generic;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.types.generic.entity.mob.MobRegistry;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.tab.TablistManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public interface SkyBlockTypeLoader {

    ServerType getType();

    void onInitialize(MinecraftServer server);

    void afterInitialize(MinecraftServer server);

    LoaderValues getLoaderValues();

    TablistManager getTablistManager();

    List<SkyBlockEventClass> getTraditionalEvents();
    List<SkyBlockEventClass> getCustomEvents();

    List<MobRegistry> getMobs();

    List<SkyBlockNPC> getNPCs();

    List<SkyBlockVillagerNPC> getVillagerNPCs();

    List<SkyBlockAnimalNPC> getAnimalNPCs();

    List<ServiceType> getRequiredServices();

    @Nullable CustomWorlds getMainInstance();

    record LoaderValues(Function<ServerType, Pos> spawnPosition, boolean announceDeathMessages) { }
}
