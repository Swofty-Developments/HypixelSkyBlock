package net.swofty.velocity.viaversion.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.velocitypowered.proxy.network.Connections;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.platform.ViaChannelInitializer;
import com.viaversion.viaversion.platform.ViaDecodeHandler;
import com.viaversion.viaversion.platform.ViaEncodeHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

import java.lang.reflect.Field;

public final class SkyBlockViaChannelInitializer extends ViaChannelInitializer {
    private static final Object COMPRESSION_ENABLED_EVENT = compressionEnabledEvent();

    public SkyBlockViaChannelInitializer(ChannelInitializer<Channel> original, boolean clientSide) {
        super(original, clientSide);
    }

    @Override
    protected void injectPipeline(ChannelPipeline pipeline, UserConnection connection) {
        addViaHandlers(pipeline, new ViaEncodeHandler(connection), new SkyBlockViaDecodeHandler(connection));
    }

    private static void addViaHandlers(ChannelPipeline pipeline, ViaEncodeHandler encoder, ViaDecodeHandler decoder) {
        var injector = Via.getManager().getInjector();
        if (pipeline.get(PacketEvents.ENCODER_NAME) != null) {
            pipeline.addAfter(PacketEvents.ENCODER_NAME, injector.getEncoderName(), encoder);
        } else {
            pipeline.addBefore(Connections.MINECRAFT_ENCODER, injector.getEncoderName(), encoder);
        }

        if (pipeline.get(PacketEvents.DECODER_NAME) != null) {
            pipeline.addAfter(PacketEvents.DECODER_NAME, injector.getDecoderName(), decoder);
        } else {
            pipeline.addBefore(Connections.MINECRAFT_DECODER, injector.getDecoderName(), decoder);
        }
    }

    private static void reorderViaHandlers(ChannelPipeline pipeline) {
        var injector = Via.getManager().getInjector();

        ViaEncodeHandler encoder = (ViaEncodeHandler) pipeline.remove(injector.getEncoderName());
        ViaDecodeHandler decoder = (ViaDecodeHandler) pipeline.remove(injector.getDecoderName());
        addViaHandlers(pipeline, encoder, decoder);
    }

    private static Object compressionEnabledEvent() {
        try {
            Class<?> eventClass = Class.forName("com.velocitypowered.proxy.protocol.VelocityConnectionEvent");
            Field field = eventClass.getDeclaredField("COMPRESSION_ENABLED");
            return field.get(null);
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static final class SkyBlockViaDecodeHandler extends ViaDecodeHandler {
        private SkyBlockViaDecodeHandler(UserConnection connection) {
            super(connection);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
            if (event == COMPRESSION_ENABLED_EVENT) {
                reorderViaHandlers(ctx.pipeline());
            }
            super.userEventTriggered(ctx, event);
        }
    }
}
