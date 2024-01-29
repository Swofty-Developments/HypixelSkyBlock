package net.swofty.velocity.packet;

import com.velocitypowered.proxy.network.Connections;
import com.velocitypowered.proxy.protocol.VelocityConnectionEvent;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.raphimc.vialoader.netty.VLLegacyPipeline;
import net.raphimc.vialoader.netty.ViaDecoder;
import net.raphimc.vialoader.netty.ViaEncoder;

import java.lang.reflect.Method;

public class PlayerChannelInitializer extends ChannelInitializer<io.netty.channel.Channel> {
    public static final String MINECRAFT_ENCODER = "minecraft-encoder";
    public static final String MINECRAFT_DECODER = "minecraft-decoder";
    public static final String VIA_ENCODER = "via-encoder";
    public static final String VIA_DECODER = "via-decoder";
    public static final Object COMPRESSION_ENABLED_EVENT;
    private static final Method INIT_CHANNEL;

    private final ChannelInitializer<?> original;
    private final boolean clientSide;

    static {
        try {
            INIT_CHANNEL = ChannelInitializer.class.getDeclaredMethod("initChannel", Channel.class);
            INIT_CHANNEL.setAccessible(true);

            Class<?> eventClass = Class.forName("com.velocitypowered.proxy.protocol.VelocityConnectionEvent");
            COMPRESSION_ENABLED_EVENT = eventClass.getDeclaredField("COMPRESSION_ENABLED").get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerChannelInitializer(ChannelInitializer<?> original, boolean clientSide) {
        this.original = original;
        this.clientSide = clientSide;
    }

    @Override
    protected void initChannel(io.netty.channel.Channel channel) throws Exception {
        INIT_CHANNEL.invoke(original, channel);

        final UserConnection user = new UserConnectionImpl(channel, false);
        new ProtocolPipelineImpl(user);

        channel.pipeline().addBefore(Connections.MINECRAFT_DECODER,
                VLLegacyPipeline.VIA_DECODER_NAME,
                new PacketDecodeHandler(user));
        channel.pipeline().addBefore(Connections.MINECRAFT_ENCODER,
                VLLegacyPipeline.VIA_ENCODER_NAME,
                new PacketEncodeHandler(user));
    }
}
