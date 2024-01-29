package net.swofty.velocity.packet;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.CancelDecoderException;
import com.viaversion.viaversion.velocity.handlers.VelocityChannelInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.raphimc.vialoader.netty.ViaDecoder;
import net.raphimc.vialoader.netty.ViaEncoder;

import java.util.List;

public class PacketDecodeHandler extends MessageToMessageDecoder<ByteBuf> {
    private final UserConnection info;

    public PacketDecodeHandler(UserConnection info) {
        this.info = info;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> out) throws Exception {
        if (!info.checkIncomingPacket()) throw CancelDecoderException.generate(null);
        if (!info.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }

        ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
        try {
            info.transformIncoming(transformedBuf, CancelDecoderException::generate);
            out.add(transformedBuf.retain());
        } finally {
            transformedBuf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof CancelCodecException) return;
        super.exceptionCaught(ctx, cause);
    }

    // Abuse decoder handler to catch events
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        if (event != PlayerChannelInitializer.COMPRESSION_ENABLED_EVENT) {
            super.userEventTriggered(ctx, event);
            return;
        }

        // When Velocity adds the compression handlers, the order becomes Minecraft Encoder -> Compressor -> Via Encoder
        // Move Via codec handlers back to right position
        ChannelPipeline pipeline = ctx.pipeline();

        // Check if the pipeline is already modified
        if (pipeline.get(PlayerChannelInitializer.VIA_ENCODER) != null) {
            super.userEventTriggered(ctx, event);
            return;
        }

        pipeline.addBefore(PlayerChannelInitializer.MINECRAFT_ENCODER, PlayerChannelInitializer.VIA_ENCODER,
                new ViaEncoder(info));
        pipeline.addBefore(PlayerChannelInitializer.MINECRAFT_DECODER, PlayerChannelInitializer.VIA_DECODER,
                new ViaDecoder(info));

        super.userEventTriggered(ctx, event);
    }
}
