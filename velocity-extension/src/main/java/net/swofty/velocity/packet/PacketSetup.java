package net.swofty.velocity.packet;

import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.ChannelInitializer;
import lombok.SneakyThrows;
import net.swofty.velocity.SkyBlockVelocity;

public class PacketSetup {
    @SneakyThrows
    public static void inject() {
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
}
