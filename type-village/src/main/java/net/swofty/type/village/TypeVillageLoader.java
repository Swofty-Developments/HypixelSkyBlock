package net.swofty.type.village;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.type.village.mobs.MobGraveyardZombie;
import net.swofty.type.village.tab.VillageServerModule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.types.generic.entity.mob.MobRegistry;
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

public class TypeVillageLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.VILLAGE;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeVillageLoader initialized!");
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                new Pos(-2.5, 72.5, -69.5, 180, 0), // Spawn position
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
                        new VillageServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<SkyBlockEvent> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.village.events",
                SkyBlockEvent.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<MobRegistry> getMobs() {
        return new ArrayList<>(List.of(
                new MobRegistry(EntityType.ZOMBIE, MobGraveyardZombie.class)
        ));
    }

    @Override
    public List<SkyBlockEvent> getCustomEvents() {
        return new ArrayList<>();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.HUB;
    }
}
