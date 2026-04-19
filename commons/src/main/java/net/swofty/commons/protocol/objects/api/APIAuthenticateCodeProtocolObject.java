package net.swofty.commons.protocol.objects.api;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class APIAuthenticateCodeProtocolObject extends ProtocolObject
        <APIAuthenticateCodeProtocolObject.AuthenticateCodeMessage, APIAuthenticateCodeProtocolObject.AuthenticateCodeResponse> {

    @Override
    public Serializer<AuthenticateCodeMessage> getSerializer() {
        return new JacksonSerializer<>(AuthenticateCodeMessage.class);
    }

    @Override
    public Serializer<AuthenticateCodeResponse> getReturnSerializer() {
        return new JacksonSerializer<>(AuthenticateCodeResponse.class);
    }

    public record AuthenticateCodeMessage(String authCode, String playerName, UUID playerUUID) {}

    public record AuthenticateCodeResponse(boolean successful) {}
}
