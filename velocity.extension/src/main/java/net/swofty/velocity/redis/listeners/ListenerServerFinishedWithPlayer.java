package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.TransferHandler;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.Optional;
import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.FINISHED_WITH_PLAYER)
public class ListenerServerFinishedWithPlayer extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        UUID playerUUID = UUID.fromString(message.getString("uuid"));

        Optional<Player> potentialPlayer = SkyBlockVelocity.getServer().getPlayer(playerUUID);
        if (potentialPlayer.isEmpty()) {
            return new JSONObject();
        }

        Player player = potentialPlayer.get();
        TransferHandler handler = new TransferHandler(player);
        handler.previousServerIsFinished();

        return new JSONObject();
    }
}
