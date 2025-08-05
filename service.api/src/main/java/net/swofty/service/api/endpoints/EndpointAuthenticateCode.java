package net.swofty.service.api.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.api.APIAuthenticateCodeProtocolObject;
import net.swofty.service.api.APIAdminDatabase;
import net.swofty.service.api.APIAdminDatabaseObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.jetbrains.annotations.Nullable;

public class EndpointAuthenticateCode implements ServiceEndpoint<
        APIAuthenticateCodeProtocolObject.AuthenticateCodeMessage,
        APIAuthenticateCodeProtocolObject.AuthenticateCodeResponse> {

    @Override
    public APIAuthenticateCodeProtocolObject associatedProtocolObject() {
        return new APIAuthenticateCodeProtocolObject();
    }

    @Override
    public APIAuthenticateCodeProtocolObject.AuthenticateCodeResponse onMessage(
            ServiceProxyRequest message,
            APIAuthenticateCodeProtocolObject.AuthenticateCodeMessage messageObject) {
        @Nullable APIAdminDatabaseObject document = APIAdminDatabase.getFromCode(messageObject.authCode);

        if (document == null) {
            return new APIAuthenticateCodeProtocolObject.AuthenticateCodeResponse(false);
        }

        document.setAuthenticatorName(messageObject.playerName);
        document.setAuthenticatorUUID(messageObject.playerUUID);

        APIAdminDatabase.replaceOrInsert(document);

        return new APIAuthenticateCodeProtocolObject.AuthenticateCodeResponse(true);
    }
}
