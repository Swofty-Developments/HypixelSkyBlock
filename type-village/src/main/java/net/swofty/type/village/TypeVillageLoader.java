package net.swofty.type.village;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.types.generic.event.SkyBlockEvent;

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
        System.out.println("TypeVillageLoader initialized!");
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                new Pos(-2.5, 70, -69.5, 180, 0) // Spawn position
        );
    }

    @Override
    public List<SkyBlockEvent> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.village.events",
                SkyBlockEvent.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<SkyBlockEvent> getCustomEvents() {
        return new ArrayList<>();
    }
}
