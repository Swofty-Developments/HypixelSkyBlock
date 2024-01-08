package net.swofty.velocity.packet;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.StateRegistry;
import com.velocitypowered.proxy.protocol.packet.Respawn;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.gamemanager.TransferHandler;
import org.jetbrains.annotations.NotNull;

public final class PlayerChannelHandler extends ChannelDuplexHandler {
    private final Player player;

    public PlayerChannelHandler(final Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(final @NotNull ChannelHandlerContext ctx, final @NotNull Object packet) throws Exception {
        try {
            if (TransferHandler.playersInLimbo.contains(player) && packet.getClass() != Respawn.class) {
                System.out.println("Blocked packet " + packet.getClass().getSimpleName() + " from being sent to " + player.getUsername() + " because they are in limbo.");
                return;
            }
        } catch (Exception e) {}

        super.channelRead(ctx, packet);
    }

    @Override
    public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
        try {
            if (TransferHandler.playersInLimbo.contains(player) && packet.getClass() != Respawn.class) {
                System.out.println("Blocked packet " + packet.getClass().getSimpleName() + " from being sent to " + player.getUsername() + " because they are in limbo.");
                return;
            }
        } catch (Exception e) {}

        super.write(ctx, packet, promise);
    }
}