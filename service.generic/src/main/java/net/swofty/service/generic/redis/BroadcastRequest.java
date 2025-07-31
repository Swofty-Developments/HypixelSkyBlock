package net.swofty.service.generic.redis;

import lombok.Getter;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastRequest {
    @Getter
    private final CompletableFuture<Map<UUID, JSONObject>> future;
    private final Map<UUID, JSONObject> responses;

    public BroadcastRequest(CompletableFuture<Map<UUID, JSONObject>> future) {
        this.future = future;
        this.responses = new ConcurrentHashMap<>();
    }

    public synchronized void addResponse(UUID serverUUID, JSONObject response) {
        if (!future.isDone()) {
            responses.put(serverUUID, response);
        }
    }

    public Map<UUID, JSONObject> getResponses() {
        return new ConcurrentHashMap<>(responses);
    }
}