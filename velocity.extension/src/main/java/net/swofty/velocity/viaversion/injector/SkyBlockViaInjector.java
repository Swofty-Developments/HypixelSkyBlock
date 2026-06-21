package net.swofty.velocity.viaversion.injector;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.swofty.velocity.SkyBlockVelocity;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.SortedSet;

public class SkyBlockViaInjector implements ViaInjector {
    public static final Method GET_PLAYER_INFO_FORWARDING_MODE = getPlayerInfoForwardingModeMethod();

    private static @Nullable Method getPlayerInfoForwardingModeMethod() {
        try {
            return Class.forName("com.velocitypowered.proxy.config.VelocityConfiguration").getMethod("getPlayerInfoForwardingMode");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            Logger.warn(e, "Failed to get getPlayerInfoForwardingMode method from Velocity");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private ChannelInitializer<Channel> getInitializer() throws Exception {
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        return (ChannelInitializer<Channel>) ReflectionUtil.invoke(channelInitializerHolder, "get");
    }

    @SuppressWarnings("unchecked")
    private ChannelInitializer<Channel> getBackendInitializer() throws Exception {
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        return (ChannelInitializer<Channel>) ReflectionUtil.invoke(channelInitializerHolder, "get");
    }

    @Override
    public void inject() throws Exception {
        Via.getPlatform().getLogger().info("Replacing channel initializers");

        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        ChannelInitializer<Channel> originalInitializer = getInitializer();
        channelInitializerHolder.getClass().getMethod("set", ChannelInitializer.class)
            .invoke(channelInitializerHolder, new SkyBlockViaChannelInitializer(originalInitializer, false));

        Object backendInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        ChannelInitializer<Channel> backendInitializer = getBackendInitializer();
        backendInitializerHolder.getClass().getMethod("set", ChannelInitializer.class)
            .invoke(backendInitializerHolder, new SkyBlockViaChannelInitializer(backendInitializer, true));
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
        } catch (IllegalAccessException | InvocationTargetException _) {
        }
        return com.velocitypowered.api.network.ProtocolVersion.MINIMUM_VERSION.getProtocol();
    }

    @Override
    public JsonObject getDump() {
        JsonObject dump = new JsonObject();
        dump.addProperty("currentInitializer", getInitializerName());

        return dump;
    }

    @Nullable
    private String getInitializerName() {
        try {
            return getInitializer().getClass().getName();
        } catch (Exception exception) {
            Logger.error(exception);
            return null;
        }
    }
}
