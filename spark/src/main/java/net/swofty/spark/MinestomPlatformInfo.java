package net.swofty.spark;

import me.lucko.spark.common.platform.PlatformInfo;
import net.minestom.server.MinecraftServer;

public final class MinestomPlatformInfo implements PlatformInfo {

    @Override
    public Type getType() {
        return Type.SERVER;
    }

    @Override
    public String getName() {
        return "Minestom";
    }

    @Override
    public String getBrand() {
        return "Hypixel";
    }

    @Override
    public String getVersion() {
        return getMinecraftVersion() + "-" + MinecraftServer.getBrandName();
    }

    @Override
    public String getMinecraftVersion() {
        return MinecraftServer.VERSION_NAME;
    }
}
