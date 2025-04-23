package net.swofty.service.api.http.endpoints;

import net.swofty.service.api.APIAdminDatabase;
import net.swofty.service.api.APIAdminDatabaseObject;
import net.swofty.service.api.http.APIResponse;
import net.swofty.service.api.http.EndpointType;
import net.swofty.service.api.http.SkyBlockEndpoint;
import net.swofty.service.api.http.StatusResponse;
import org.jetbrains.annotations.Nullable;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.UUID;

public class LogoutEndpoint extends SkyBlockEndpoint {
    @Override
    public EndpointType getType() {
        return EndpointType.ADMINISTRATIVE;
    }

    @Override
    public String getPath() {
        return "/logout";
    }

    @Override
    public @Nullable APIResponse handle(Map<String, String> headers, Request req, Response res) {
        String sessionId = req.cookie("sessionId");
        System.out.println("Session ID: " + sessionId);

        if (sessionId == null) {
            return APIResponse.error("Missing required headers");
        }

        APIAdminDatabase adminDatabase = new APIAdminDatabase();
        APIAdminDatabaseObject document = adminDatabase.getFromSessionId(sessionId);

        if (document == null) {
            return APIResponse.error("Invalid session ID");
        }

        if (!document.isAuthenticated()) {
            return APIResponse.error("Session is not authenticated");
        }

        document.setAuthenticatorName("");
        document.setAuthenticatorUUID(UUID.randomUUID());

        APIAdminDatabase.replaceOrInsert(document);

        return new APIResponse(StatusResponse.SUCCESS, document.toDocument().toJson());
    }
}
