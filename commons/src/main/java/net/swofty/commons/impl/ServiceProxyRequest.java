package net.swofty.commons.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class ServiceProxyRequest {
    private UUID requestId;
    private String requestServer;
    private List<String> requiredKeys;
    private String endpoint;
    private String message;

    public JSONObject toJSON() {
        return new JSONObject()
                .put("requestId", requestId.toString())
                .put("requestServer", requestServer)
                .put("endpoint", endpoint)
                .put("requiredKeys", String.join(",", requiredKeys))
                .put("message", message);
    }

    public static ServiceProxyRequest fromJSON(JSONObject json) {
        return new ServiceProxyRequest(
                UUID.fromString(json.getString("requestId")),
                json.getString("requestServer"),
                List.of(json.getString("requiredKeys").split(",")),
                json.getString("endpoint"),
                json.getString("message")
        );
    }
}
