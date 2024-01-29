package net.swofty.velocity.packet;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.protocol.packet.BossBar;
import com.velocitypowered.proxy.protocol.packet.JoinGame;
import com.velocitypowered.proxy.protocol.packet.Respawn;
import com.velocitypowered.proxy.protocol.packet.config.FinishedUpdate;
import com.velocitypowered.proxy.protocol.packet.config.RegistrySync;
import com.velocitypowered.proxy.protocol.packet.config.StartUpdate;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.swofty.velocity.gamemanager.TransferHandler;

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
                    && packet.getClass() != StartUpdate.class
                    && packet.getClass() != RegistrySync.class
                    && packet.getClass() != FinishedUpdate.class
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