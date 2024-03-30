package net.swofty.spark;

import me.lucko.spark.common.platform.PlatformInfo;
import net.minestom.server.MinecraftServer;

public final class MinestomPlatformInfo implements PlatformInfo {

    public MinestomPlatformInfo() {
    }

    public Type getType() {
        return Type.SERVER;
    }

    public String getName() {
        return "Minestom";
    }

    public String getVersion() {
        return getMinecraftVersion() + "-" + MinecraftServer.getBrandName();
    }

    public String getMinecraftVersion() {
        return MinecraftServer.VERSION_NAME;
    }

}
