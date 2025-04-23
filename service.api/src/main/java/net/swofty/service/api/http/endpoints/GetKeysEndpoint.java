package net.swofty.service.api.http.endpoints;

import net.swofty.service.api.APIAdminDatabase;
import net.swofty.service.api.APIAdminDatabaseObject;
import net.swofty.service.api.APIKeyDatabase;
import net.swofty.service.api.APIKeyDatabaseObject;
import net.swofty.service.api.http.APIResponse;
import net.swofty.service.api.http.EndpointType;
import net.swofty.service.api.http.SkyBlockEndpoint;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

public class GetKeysEndpoint extends SkyBlockEndpoint {
    @Override
    public EndpointType getType() {
        return EndpointType.ADMINISTRATIVE;
    }

    @Override
    public String getPath() {
        return "/get_keys";
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

        // Get all API keys
        List<APIKeyDatabaseObject> keys = APIKeyDatabase.getAll();

        // Convert to JSONArray
        JSONArray keysArray = new JSONArray();
        for (APIKeyDatabaseObject key : keys) {
            JSONObject keyObj = new JSONObject();
            keyObj.put("api_key", key.getKey());
            keyObj.put("description", key.getDescription());
            keyObj.put("allowed_per_day_usage", key.getRequestsPerDay());
            keysArray.put(keyObj);
        }

        return APIResponse.success(keysArray.toString());
    }
}