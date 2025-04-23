package net.swofty.service.api.http.endpoints;

import net.swofty.service.api.APIAdminDatabase;
import net.swofty.service.api.APIAdminDatabaseObject;
import net.swofty.service.api.http.APIResponse;
import net.swofty.service.api.http.EndpointType;
import net.swofty.service.api.http.SkyBlockEndpoint;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.UUID;

public class InitiateAuthEndpoint extends SkyBlockEndpoint {
    @Override
    public EndpointType getType() {
        return EndpointType.ADMINISTRATIVE;
    }

    @Override
    public String getPath() {
        return "/initiateAuth";
    }

    @Override
    public APIResponse handle(Map<String, String> headers, Request req, Response res) {
        String authCode = headers.get("X-Auth-Code");
        String sessionId = headers.get("X-Session-ID");

        if (authCode == null || sessionId == null) {
            return APIResponse.error("Missing required headers");
        }

        if (authCode.length() != 8) {
            return APIResponse.error("Invalid auth code");
        }

        APIAdminDatabaseObject document = new APIAdminDatabaseObject();
        document.setSessionId(sessionId);
        document.setAuthCode(authCode);
        document.setAuthenticatorName("");
        document.setAuthenticatorUUID(UUID.randomUUID());

        APIAdminDatabase.replaceOrInsert(document);

        // Set cookie with more explicit parameters
        res.header("Set-Cookie", "sessionId=" + sessionId +
                "; Path=/; Max-Age=3600; SameSite=Lax; HttpOnly");

        return APIResponse.success("Initiated auth");
    }
}
