package net.swofty.velocity.viaversion.injector;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.ChannelInitializer;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.SortedSet;

import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.packet.PlayerChannelInitializer;
import org.jetbrains.annotations.Nullable;

public class SkyBlockViaInjector implements ViaInjector {
    public static final Method GET_PLAYER_INFO_FORWARDING_MODE = getPlayerInfoForwardingModeMethod();

    private static @Nullable Method getPlayerInfoForwardingModeMethod() {
        try {
            return Class.forName("com.velocitypowered.proxy.config.VelocityConfiguration").getMethod("getPlayerInfoForwardingMode");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            System.out.println("Failed to get getPlayerInfoForwardingMode method from Velocity");
            return null;
        }
    }

    private ChannelInitializer getInitializer() throws Exception {
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        return (ChannelInitializer) ReflectionUtil.invoke(channelInitializerHolder, "get");
    }

    private ChannelInitializer getBackendInitializer() throws Exception {
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        return (ChannelInitializer) ReflectionUtil.invoke(channelInitializerHolder, "get");
    }

    @Override
    public void inject() throws Exception {
        Via.getPlatform().getLogger().info("Replacing channel initializers");

        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        ChannelInitializer originalInitializer = getInitializer();
        channelInitializerHolder.getClass().getMethod("set", ChannelInitializer.class)
                .invoke(channelInitializerHolder, new PlayerChannelInitializer(originalInitializer, false));

        Object backendInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        ChannelInitializer backendInitializer = getBackendInitializer();
        backendInitializerHolder.getClass().getMethod("set", ChannelInitializer.class)
                .invoke(backendInitializerHolder, new PlayerChannelInitializer(backendInitializer, true));
    }

    @Override
    public void uninject() {
    }

    @Override
    public ProtocolVersion getServerProtocolVersion() {
        return ProtocolVersion.getProtocol(getLowestSupportedProtocolVersion());
    }

    @Override
    public SortedSet<ProtocolVersion> getServerProtocolVersions() {
        int lowestSupportedProtocolVersion = getLowestSupportedProtocolVersion();

        SortedSet<ProtocolVersion> set = new ObjectLinkedOpenHashSet<>();
        for (com.velocitypowered.api.network.ProtocolVersion version : com.velocitypowered.api.network.ProtocolVersion.SUPPORTED_VERSIONS) {
            if (version.getProtocol() >= lowestSupportedProtocolVersion) {
                set.add(ProtocolVersion.getProtocol(version.getProtocol()));
            }
        }
        return set;
    }

    public static int getLowestSupportedProtocolVersion() {
        try {
            if (GET_PLAYER_INFO_FORWARDING_MODE != null
                    && ((Enum<?>) GET_PLAYER_INFO_FORWARDING_MODE.invoke(SkyBlockVelocity.getServer().getConfiguration()))
                    .name().equals("MODERN")) {
                return ProtocolVersion.v1_13.getVersion();
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
        return com.velocitypowered.api.network.ProtocolVersion.MINIMUM_VERSION.getProtocol();
    }

    @Override
    public JsonObject getDump() {
        JsonObject data = new JsonObject();
        try {
            data.addProperty("currentInitializer", getInitializer().getClass().getName());
        } catch (Exception e) {
        }
        return data;
    }
}