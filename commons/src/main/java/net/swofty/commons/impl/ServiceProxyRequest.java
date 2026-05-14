package net.swofty.commons.impl;

import org.json.JSONObject;

import java.util.UUID;

public record ServiceProxyRequest(UUID requestId, String requestServer, String endpoint, String message) {

    public JSONObject toJSON() {
        return new JSONObject()
                .put("requestId", requestId.toString())
                .put("requestServer", requestServer)
                .put("endpoint", endpoint)
                .put("message", message);
    }

    public static ServiceProxyRequest fromJSON(JSONObject json) {
        return new ServiceProxyRequest(
                UUID.fromString(json.getString("requestId")),
                json.getString("requestServer"),
                json.getString("endpoint"),
                json.getString("message")
        );
    }

    // Compatibility accessors so existing call sites that used Lombok-generated
    // getters keep compiling — records expose component(), not getComponent().
    public UUID getRequestId() { return requestId; }
    public String getRequestServer() { return requestServer; }
    public String getEndpoint() { return endpoint; }
    public String getMessage() { return message; }
}
