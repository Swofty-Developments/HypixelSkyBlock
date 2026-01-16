package net.swofty.velocity.viaversion.handler;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.CancelDecoderException;
import com.viaversion.viaversion.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.swofty.velocity.packet.PlayerChannelInitializer;

import java.util.List;

@ChannelHandler.Sharable
public class PacketDecodeHandler extends MessageToMessageDecoder<ByteBuf> {
    private final UserConnection info;

    public PacketDecodeHandler(UserConnection info) {
        this.info = info;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> out) {
        if (!info.checkIncomingPacket(0)) throw CancelDecoderException.generate(null);
        if (!info.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }

        ByteBuf transformedBuf = ByteBufUtil.copy(ctx.alloc(), bytebuf);
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

        ChannelHandler encoder = pipeline.remove(PlayerChannelInitializer.VIA_ENCODER);
        pipeline.addBefore(PlayerChannelInitializer.MINECRAFT_ENCODER, PlayerChannelInitializer.VIA_ENCODER, encoder);

        ChannelHandler decoder = pipeline.remove(PlayerChannelInitializer.VIA_DECODER);
        pipeline.addBefore(PlayerChannelInitializer.MINECRAFT_DECODER, PlayerChannelInitializer.VIA_DECODER, decoder);

        super.userEventTriggered(ctx, event);
    }
}