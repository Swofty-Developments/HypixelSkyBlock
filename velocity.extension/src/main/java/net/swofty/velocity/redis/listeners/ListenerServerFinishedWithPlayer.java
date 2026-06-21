package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.to.FinishedWithPlayerProtocol;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.TransferHandler;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.Optional;
import java.util.UUID;

public class ListenerServerFinishedWithPlayer implements RedisMessageHandler<
        FinishedWithPlayerProtocol.Request,
        FinishedWithPlayerProtocol.Response> {

    @Override
    public RedisProtocol<FinishedWithPlayerProtocol.Request, FinishedWithPlayerProtocol.Response> protocol() {
        return new FinishedWithPlayerProtocol();
    }

    @Override
    public FinishedWithPlayerProtocol.Response handle(FinishedWithPlayerProtocol.Request message, RedisMessageContext context) {
        UUID playerUUID = UUID.fromString(message.uuid());

        Optional<Player> potentialPlayer = SkyBlockVelocity.getServer().getPlayer(playerUUID);
        if (potentialPlayer.isEmpty()) {
            return new FinishedWithPlayerProtocol.Response();
        }

        Player player = potentialPlayer.get();
        TransferHandler handler = new TransferHandler(player);
        if (!handler.isInLimbo()) {
            return new FinishedWithPlayerProtocol.Response();
        }
        handler.previousServerIsFinished();

        return new FinishedWithPlayerProtocol.Response();
    }
}
