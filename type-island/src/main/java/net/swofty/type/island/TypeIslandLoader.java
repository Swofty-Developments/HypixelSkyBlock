package net.swofty.type.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.types.generic.event.SkyBlockEvent;
import org.tinylog.Logger;

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
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                new Pos(0, 100 ,0) // Spawn position
        );
    }

    @Override
    public List<SkyBlockEvent> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.island.events.traditional",
                SkyBlockEvent.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<SkyBlockEvent> getCustomEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.type.island.events.custom",
                SkyBlockEvent.class
        ).collect(Collectors.toList());
    }
}
