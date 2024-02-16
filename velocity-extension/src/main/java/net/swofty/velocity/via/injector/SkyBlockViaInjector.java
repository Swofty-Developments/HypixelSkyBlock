package net.swofty.velocity.via.injector;

import com.velocitypowered.api.network.ProtocolVersion;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.libs.fastutil.ints.IntLinkedOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.ChannelInitializer;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.packet.PlayerChannelInitializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SkyBlockViaInjector implements ViaInjector {

    public static final Method GET_PLAYER_INFO_FORWARDING_MODE = getPlayerInfoForwardingModeMethod();

    private static Method getPlayerInfoForwardingModeMethod() {
        try {
            return Class.forName("com.velocitypowered.proxy.config.VelocityConfiguration").getMethod("getPlayerInfoForwardingMode");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void inject() throws Exception{
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        ChannelInitializer<?> originalInitializer = getInitializer();
        channelInitializerHolder.getClass().getMethod("set", ChannelInitializer.class)
                .invoke(channelInitializerHolder, new PlayerChannelInitializer(originalInitializer, false));

        Object backendInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        ChannelInitializer<?> backendInitializer = getBackendInitializer();
        backendInitializerHolder.getClass().getMethod("set", ChannelInitializer.class)
                .invoke(backendInitializerHolder, new PlayerChannelInitializer(backendInitializer, true));
    }

    private static ChannelInitializer<?> getInitializer() throws Exception {
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        return (ChannelInitializer<?>) ReflectionUtil.invoke(channelInitializerHolder, "get");
    }

    private static ChannelInitializer<?> getBackendInitializer() throws Exception {
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        return (ChannelInitializer<?>) ReflectionUtil.invoke(channelInitializerHolder, "get");
    }

    @Override
    public void uninject() throws Exception {
        // not supported
    }

    @Override
    public int getServerProtocolVersion() {
        return getLowestSupportedProtocolVersion();
    }

    @Override
    public IntSortedSet getServerProtocolVersions() {
        int lowestSupportedProtocolVersion = getLowestSupportedProtocolVersion();
        IntSortedSet set = new IntLinkedOpenHashSet();

        for (ProtocolVersion version : ProtocolVersion.SUPPORTED_VERSIONS) {
            if (version.getProtocol() >= lowestSupportedProtocolVersion) {
                set.add(version.getProtocol());
            }
        }

        return set;
    }

    public static int getLowestSupportedProtocolVersion() {
        try {
            if (GET_PLAYER_INFO_FORWARDING_MODE != null && ((Enum<?>)GET_PLAYER_INFO_FORWARDING_MODE.invoke(SkyBlockVelocity.getServer().getConfiguration())).name().equals("MODERN")) {
                return com.viaversion.viaversion.api.protocol.version.ProtocolVersion.v1_13.getVersion();
            }
        } catch (InvocationTargetException | IllegalAccessException ignored) {}

        return ProtocolVersion.MINIMUM_VERSION.getProtocol();
    }

    @Override
    public JsonObject getDump() {
        JsonObject data = new JsonObject();
        try {
            data.addProperty("currentInitializer", getInitializer().getClass().getName());
        } catch (Exception ignored) {}
        return data;
    }
}