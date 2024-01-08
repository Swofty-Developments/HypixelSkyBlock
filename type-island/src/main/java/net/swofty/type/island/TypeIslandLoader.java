package net.swofty.type.island;

import net.minestom.server.MinecraftServer;
import net.swofty.commons.ServerType;
import net.swofty.types.SkyBlockTypeLoader;

public class TypeIslandLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.ISLAND;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        System.out.println("TypeIslandLoader initialized!");
    }
}
