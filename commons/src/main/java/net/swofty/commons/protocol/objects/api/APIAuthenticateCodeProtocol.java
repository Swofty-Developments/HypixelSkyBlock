package net.swofty.commons.protocol.objects.api;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class APIAuthenticateCodeProtocol extends RedisProtocol
        <APIAuthenticateCodeProtocol.AuthenticateCodeMessage, APIAuthenticateCodeProtocol.AuthenticateCodeResponse> {
    private static final Serializer<AuthenticateCodeMessage> SERIALIZER =
            new JacksonSerializer<>(AuthenticateCodeMessage.class);
    private static final Serializer<AuthenticateCodeResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(AuthenticateCodeResponse.class);

    @Override

    public Serializer<AuthenticateCodeMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<AuthenticateCodeResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record AuthenticateCodeMessage(String authCode, String playerName, UUID playerUUID) {}

    public record AuthenticateCodeResponse(boolean success, @Nullable String error) {}
}
