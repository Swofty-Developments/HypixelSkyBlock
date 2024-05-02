package net.swofty.velocity.packet;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.packet.BossBarPacket;
import com.velocitypowered.proxy.protocol.packet.JoinGamePacket;
import com.velocitypowered.proxy.protocol.packet.KeepAlivePacket;
import com.velocitypowered.proxy.protocol.packet.RespawnPacket;
import com.velocitypowered.proxy.protocol.packet.config.FinishedUpdatePacket;
import com.velocitypowered.proxy.protocol.packet.config.RegistrySyncPacket;
import com.velocitypowered.proxy.protocol.packet.config.StartUpdatePacket;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.swofty.velocity.gamemanager.TransferHandler;

public final class PlayerChannelHandler extends ChannelDuplexHandler {
    private final Player player;
    private RespawnPacket respawn = null;

    public PlayerChannelHandler(final Player player) {
        this.player = player;
    }

    @Override
    public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
        try {
            if (TransferHandler.playersInLimbo.contains(player)
                    && packet.getClass() != RespawnPacket.class
                    && packet.getClass() != JoinGamePacket.class
                    && packet.getClass() != BossBarPacket.class
                    && packet.getClass() != StartUpdatePacket.class
                    && packet.getClass() != KeepAlivePacket.class
                    && packet.getClass() != RegistrySyncPacket.class
                    && packet.getClass() != FinishedUpdatePacket.class
            ) {
                System.out.println("Blocked packet " + packet.getClass().getSimpleName() + " from being sent to " + player.getUsername() + " because they are in limbo.");
                return;
            }
            if (respawn == null && packet.getClass() == RespawnPacket.class) {
                respawn = (RespawnPacket) packet;
            }
            if (packet.getClass() != RespawnPacket.class && TransferHandler.playersInLimbo.contains(player)) {
                if (respawn != null) {
                    write(ctx, respawn, ctx.newPromise());
                }
            }
        } catch (Exception ignored) {}


        super.write(ctx, packet, promise);
    }
}