package net.swofty.velocity.packet;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.MinecraftPacket;
import com.velocitypowered.proxy.protocol.StateRegistry;
import com.velocitypowered.proxy.protocol.packet.BossBar;
import com.velocitypowered.proxy.protocol.packet.JoinGame;
import com.velocitypowered.proxy.protocol.packet.Respawn;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.gamemanager.TransferHandler;
import org.jetbrains.annotations.NotNull;

public final class PlayerChannelHandler extends ChannelDuplexHandler {
    private final Player player;
    private Respawn respawn = null;

    public PlayerChannelHandler(final Player player) {
        this.player = player;
    }

    @Override
    public void write(final ChannelHandlerContext ctx, final Object packet, final ChannelPromise promise) throws Exception {
        try {
            if (TransferHandler.playersInLimbo.contains(player)
                    && packet.getClass() != Respawn.class
                    && packet.getClass() != JoinGame.class
                    && packet.getClass() != BossBar.class
            ) {
                System.out.println("Blocked packet " + packet.getClass().getSimpleName() + " from being sent to " + player.getUsername() + " because they are in limbo.");
                return;
            }
            if (respawn == null && packet.getClass() == Respawn.class) {
                respawn = (Respawn) packet;
            }
            if (packet.getClass() != Respawn.class && TransferHandler.playersInLimbo.contains(player)) {
                if (respawn != null) {
                    write(ctx, respawn, ctx.newPromise());
                }
            }
        } catch (Exception e) {}


        super.write(ctx, packet, promise);
    }
}