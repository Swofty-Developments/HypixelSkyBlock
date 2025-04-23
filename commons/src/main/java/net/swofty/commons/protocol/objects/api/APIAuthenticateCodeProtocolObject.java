package net.swofty.commons.protocol.objects.api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.objects.bazaar.BazaarBuyProtocolObject;
import org.json.JSONObject;

import java.util.UUID;

public class APIAuthenticateCodeProtocolObject extends ProtocolObject
        <APIAuthenticateCodeProtocolObject.AuthenticateCodeMessage, APIAuthenticateCodeProtocolObject.AuthenticateCodeResponse> {

    @Override
    public Serializer<AuthenticateCodeMessage> getSerializer() {
        return new Serializer<AuthenticateCodeMessage>() {
            @Override
            public String serialize(AuthenticateCodeMessage value) {
                JSONObject json = new JSONObject();
                json.put("auth-code", value.authCode);
                json.put("player-name", value.playerName);
                json.put("player-uuid", value.playerUUID);
                return json.toString();
            }

            @Override
            public AuthenticateCodeMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                AuthenticateCodeMessage message = new AuthenticateCodeMessage();
                message.authCode = jsonObject.getString("auth-code");
                message.playerName = jsonObject.getString("player-name");
                message.playerUUID = UUID.fromString(jsonObject.getString("player-uuid"));
                return message;
            }

            @Override
            public AuthenticateCodeMessage clone(AuthenticateCodeMessage value) {
                return new AuthenticateCodeMessage(value.authCode, value.playerName, value.playerUUID);
            }
        };
    }

    @Override
    public Serializer<AuthenticateCodeResponse> getReturnSerializer() {
        return new Serializer<AuthenticateCodeResponse>() {
            public String serialize(AuthenticateCodeResponse value) {
                JSONObject json = new JSONObject();
                json.put("successful", value.successful);
                return json.toString();
            }

            public AuthenticateCodeResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                AuthenticateCodeResponse message = new AuthenticateCodeResponse();
                message.successful = jsonObject.getBoolean("successful");
                return message;
            }

            public AuthenticateCodeResponse clone(AuthenticateCodeResponse value) {
                return new AuthenticateCodeResponse(value.successful);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticateCodeMessage {
        public String authCode;
        public String playerName;
        public UUID playerUUID;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticateCodeResponse {
        public boolean successful;
    }
}

