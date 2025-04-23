package net.swofty.service.api.http.endpoints;

import net.swofty.service.api.APIAdminDatabase;
import net.swofty.service.api.APIAdminDatabaseObject;
import net.swofty.service.api.APIKeyDatabase;
import net.swofty.service.api.APIKeyDatabaseObject;
import net.swofty.service.api.http.APIResponse;
import net.swofty.service.api.http.EndpointType;
import net.swofty.service.api.http.SkyBlockEndpoint;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.Random;

public class InsertKeyEndpoint extends SkyBlockEndpoint {
    private static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int API_KEY_LENGTH = 32;

    @Override
    public EndpointType getType() {
        return EndpointType.ADMINISTRATIVE;
    }

    @Override
    public String getPath() {
        return "/insert_key";
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

        // Extract parameters from query string
        String description = req.queryParams("description");
        String usagePerDayStr = req.queryParams("usagePerDay");

        if (description == null || description.isEmpty()) {
            return APIResponse.error("Description is required");
        }

        long usagePerDay;
        try {
            usagePerDay = Long.parseLong(usagePerDayStr);
        } catch (NumberFormatException e) {
            return APIResponse.error("Usage per day must be a valid number");
        }

        if (usagePerDay <= 0) {
            return APIResponse.error("Usage per day must be greater than 0");
        }

        // Generate a new API key
        String apiKey = generateAPIKey();

        // Create and store the new API key
        APIKeyDatabaseObject keyObject = new APIKeyDatabaseObject();
        keyObject.setKey(apiKey);
        keyObject.setDescription(description);
        keyObject.setRequestsPerDay(usagePerDay);

        APIKeyDatabase.insert(keyObject);

        // Create response with the new key
        JSONObject responseData = new JSONObject();
        responseData.put("api_key", apiKey);
        responseData.put("description", description);
        responseData.put("allowed_per_day_usage", usagePerDay);

        return APIResponse.success(responseData.toString());
    }

    private String generateAPIKey() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(API_KEY_LENGTH);

        for (int i = 0; i < API_KEY_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARS.length());
            sb.append(ALLOWED_CHARS.charAt(randomIndex));
        }

        return sb.toString();
    }
}