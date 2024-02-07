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

import static com.viaversion.viaversion.velocity.platform.VelocityViaInjector.GET_PLAYER_INFO_FORWARDING_MODE;

public class SkyBlockViaInjector implements ViaInjector {
    @Override
    public void inject() throws Exception{
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

    private static ChannelInitializer getInitializer() throws Exception {
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getServerChannelInitializer");
        return (ChannelInitializer) ReflectionUtil.invoke(channelInitializerHolder, "get");
    }

    private static ChannelInitializer getBackendInitializer() throws Exception {
        Object connectionManager = ReflectionUtil.get(SkyBlockVelocity.getServer(), "cm", Object.class);
        Object channelInitializerHolder = ReflectionUtil.invoke(connectionManager, "getBackendChannelInitializer");
        return (ChannelInitializer) ReflectionUtil.invoke(channelInitializerHolder, "get");
    }

    @Override
    public void uninject() throws Exception {
        // not supported
    }

    @Override
    public int getServerProtocolVersion() throws Exception {
        return getLowestSupportedProtocolVersion();
    }

    @Override
    public IntSortedSet getServerProtocolVersions() throws Exception {
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
                return com.viaversion.viaversion.api.protocol.version.ProtocolVersion.v1_13_2.getVersion();
            }
        } catch (InvocationTargetException | IllegalAccessException ignored) {}

        return ProtocolVersion.MINIMUM_VERSION.getProtocol();
    }



    @Override
    public JsonObject getDump() {
        return null;
    }
}