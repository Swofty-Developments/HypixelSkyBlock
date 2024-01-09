package net.swofty.types.generic;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.event.SkyBlockEvent;
import org.tinylog.Logger;

import java.util.List;

public interface SkyBlockTypeLoader {

    ServerType getType();

    void onInitialize(MinecraftServer server);

    LoaderValues getLoaderValues();

    List<SkyBlockEvent> getTraditionalEvents();

    List<SkyBlockEvent> getCustomEvents();

    record LoaderValues(Pos spawnPosition) { }
}
