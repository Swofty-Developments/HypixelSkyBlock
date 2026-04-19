package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.game.Game;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public class GameHeartbeatProtocolObject extends ProtocolObject
        <GameHeartbeatProtocolObject.HeartbeatMessage,
                GameHeartbeatProtocolObject.HeartbeatResponse> {

    @Override
    public Serializer<HeartbeatMessage> getSerializer() {
        return new JacksonSerializer<>(HeartbeatMessage.class);
    }

    @Override
    public Serializer<HeartbeatResponse> getReturnSerializer() {
        return new JacksonSerializer<>(HeartbeatResponse.class);
    }

    public record HeartbeatMessage(UUID uuid, String shortName, ServerType type, int maxPlayers, int onlinePlayers, List<Game> games) { }

    public record HeartbeatResponse(boolean ok) { }
}
