package net.swofty.service.api.http.endpoints;

import net.swofty.service.api.APIKeyDatabase;
import net.swofty.service.api.http.APIResponse;
import net.swofty.service.api.http.ApiRateLimiterHandler;
import net.swofty.service.api.http.EndpointType;
import net.swofty.service.api.http.SkyBlockEndpoint;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import org.jetbrains.annotations.Nullable;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.UUID;

public class ProfileEndpoint extends SkyBlockEndpoint {
    @Override
    public EndpointType getType() {
        return EndpointType.CONSUMER;
    }

    @Override
    public String getPath() {
        return "/profile";
    }

    @Override
    public @Nullable APIResponse handle(Map<String, String> headers, Request req, Response res) {
        String apiKey = headers.get("X-API-Key");

        if (apiKey == null) {
            return APIResponse.error("Missing required header X-API-Key");
        }

        APIKeyDatabase keyDatabase = new APIKeyDatabase(apiKey);
        if (keyDatabase.fetch() == null) {
            return APIResponse.error("Invalid API key");
        }

        // Check rate limit before processing the request
        if (!ApiRateLimiterHandler.getInstance().isRequestAllowed(apiKey)) {
            long remaining = ApiRateLimiterHandler.getInstance().getRemainingRequests(apiKey);
            return APIResponse.error("Rate limit exceeded. Daily request limit reached. Remaining requests: " + remaining);
        }

        String profileUuid = headers.get("X-Profile-UUID");
        if (profileUuid == null) {
            return APIResponse.error("Missing required header X-Profile-UUID");
        }

        try {
            UUID.fromString(profileUuid);
        } catch (IllegalArgumentException e) {
            return APIResponse.error("Invalid X-Profile-UUID header");
        }

        ProfilesDatabase profilesDatabase = new ProfilesDatabase(profileUuid);
        if (!profilesDatabase.exists()) {
            return APIResponse.error("Profile not found");
        }

        return APIResponse.success(profilesDatabase.getDocument().toJson());
    }
}

