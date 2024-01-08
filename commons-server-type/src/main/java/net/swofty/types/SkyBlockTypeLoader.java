package net.swofty.types;

import net.minestom.server.MinecraftServer;
import net.swofty.commons.ServerType;

public interface SkyBlockTypeLoader {

    public ServerType getType();

    public void onInitialize(MinecraftServer server);


}
