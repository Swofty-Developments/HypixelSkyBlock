package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class ChooseGameProtocolObject extends ProtocolObject
        <ChooseGameProtocolObject.ChooseGameMessage,
                ChooseGameProtocolObject.ChooseGameResponse> {

    @Override
    public Serializer<ChooseGameMessage> getSerializer() {
        return new JacksonSerializer<>(ChooseGameMessage.class);
    }

    @Override
    public Serializer<ChooseGameResponse> getReturnSerializer() {
        return new JacksonSerializer<>(ChooseGameResponse.class);
    }

    public record ChooseGameMessage(UUID player, UnderstandableProxyServer server, String gameId) {
    }

    public record ChooseGameResponse(boolean success, @Nullable String error) {
    }
}
