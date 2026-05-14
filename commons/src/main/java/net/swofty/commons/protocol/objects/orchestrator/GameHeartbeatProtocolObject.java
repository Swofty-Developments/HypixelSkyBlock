package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.game.Game;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GameHeartbeatProtocolObject extends ProtocolObject
        <GameHeartbeatProtocolObject.HeartbeatMessage,
                GameHeartbeatProtocolObject.HeartbeatResponse> {
    private static final Serializer<HeartbeatMessage> SERIALIZER =
            new JacksonSerializer<>(HeartbeatMessage.class);
    private static final Serializer<HeartbeatResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(HeartbeatResponse.class);

    @Override

    public Serializer<HeartbeatMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<HeartbeatResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record HeartbeatMessage(UUID uuid, String shortName, ServerType type, int maxPlayers, int onlinePlayers, List<Game> games) { }

    public record HeartbeatResponse(boolean success, @Nullable String error) { }
}
