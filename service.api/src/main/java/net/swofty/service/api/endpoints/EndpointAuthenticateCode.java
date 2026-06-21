package net.swofty.service.api.endpoints;

import net.swofty.commons.protocol.objects.api.APIAuthenticateCodeProtocol;
import net.swofty.service.api.APIAdminDatabase;
import net.swofty.service.api.APIAdminDatabaseObject;
import net.swofty.commons.redis.RedisMessageHandler;
import org.jetbrains.annotations.Nullable;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointAuthenticateCode implements RedisMessageHandler<
        APIAuthenticateCodeProtocol.AuthenticateCodeMessage,
        APIAuthenticateCodeProtocol.AuthenticateCodeResponse> {

    @Override
    public APIAuthenticateCodeProtocol protocol() {
        return new APIAuthenticateCodeProtocol();
    }

    @Override
    public APIAuthenticateCodeProtocol.AuthenticateCodeResponse handle(APIAuthenticateCodeProtocol.AuthenticateCodeMessage messageObject, RedisMessageContext context) {
        @Nullable APIAdminDatabaseObject document = APIAdminDatabase.getFromCode(messageObject.authCode());

        if (document == null) {
            return new APIAuthenticateCodeProtocol.AuthenticateCodeResponse(false, "Authentication failed");
        }

        document.setAuthenticatorName(messageObject.playerName());
        document.setAuthenticatorUUID(messageObject.playerUUID());

        APIAdminDatabase.replaceOrInsert(document);

        return new APIAuthenticateCodeProtocol.AuthenticateCodeResponse(true, null);
    }
}
