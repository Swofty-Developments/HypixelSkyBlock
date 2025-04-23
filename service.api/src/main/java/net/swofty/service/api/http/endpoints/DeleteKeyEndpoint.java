package net.swofty.service.api.http.endpoints;

import net.swofty.service.api.APIAdminDatabase;
import net.swofty.service.api.APIAdminDatabaseObject;
import net.swofty.service.api.APIKeyDatabase;
import net.swofty.service.api.http.APIResponse;
import net.swofty.service.api.http.EndpointType;
import net.swofty.service.api.http.SkyBlockEndpoint;
import spark.Request;
import spark.Response;

import java.util.Map;

public class DeleteKeyEndpoint extends SkyBlockEndpoint {
    @Override
    public EndpointType getType() {
        return EndpointType.ADMINISTRATIVE;
    }

    @Override
    public String getPath() {
        return "/delete_key";
    }

    @Override
    public APIResponse handle(Map<String, String> headers, Request req, Response res) {
        // Get session ID from cookie
        String sessionId = req.cookie("sessionId");
        if (sessionId == null) {
            // Try from headers as fallback
            sessionId = headers.get("X-Session-ID");
        }

        if (sessionId == null || sessionId.isEmpty()) {
            return APIResponse.error("Unauthorized - No session ID provided");
        }

        // Verify authentication
        APIAdminDatabase adminDatabase = new APIAdminDatabase();
        APIAdminDatabaseObject document = adminDatabase.getFromSessionId(sessionId);

        if (document == null || !document.isAuthenticated()) {
            return APIResponse.error("Unauthorized - Invalid session or not authenticated");
        }

        // Get the API key to delete from query parameter
        String apiKey = req.queryParams("key");
        if (apiKey == null || apiKey.isEmpty()) {
            return APIResponse.error("No API key provided");
        }

        // Check if the key exists
        APIKeyDatabase keyDatabase = new APIKeyDatabase(apiKey);
        if (keyDatabase.fetch() == null) {
            return APIResponse.error("API key not found");
        }

        // Delete the key
        keyDatabase.delete();

        return APIResponse.success("API key deleted successfully");
    }
}