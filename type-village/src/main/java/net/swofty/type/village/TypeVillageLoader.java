package net.swofty.type.village;

import net.minestom.server.MinecraftServer;
import net.swofty.commons.ServerType;
import net.swofty.types.SkyBlockTypeLoader;

public class TypeVillageLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.VILLAGE;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        System.out.println("TypeVillageLoader initialized!");
    }
}
