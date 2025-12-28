package net.swofty.commons.friend;

import lombok.Getter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PendingFriendRequest {
    private final UUID from;
    private final UUID to;
    private final String fromName;
    private final String toName;
    private final long timestamp;

    public PendingFriendRequest(UUID from, UUID to, String fromName, String toName, long timestamp) {
        this.from = from;
        this.to = to;
        this.fromName = fromName;
        this.toName = toName;
        this.timestamp = timestamp;
    }

    public static PendingFriendRequest create(UUID from, UUID to, String fromName, String toName) {
        return new PendingFriendRequest(from, to, fromName, toName, System.currentTimeMillis());
    }

    public List<UUID> getParticipants() {
        return List.of(from, to);
    }

    public static Serializer<PendingFriendRequest> getStaticSerializer() {
        return create(UUID.randomUUID(), UUID.randomUUID(), "", "").getSerializer();
    }

    public Serializer<PendingFriendRequest> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PendingFriendRequest value) {
                JSONObject json = new JSONObject();
                json.put("from", value.from.toString());
                json.put("to", value.to.toString());
                json.put("fromName", value.fromName);
                json.put("toName", value.toName);
                json.put("timestamp", value.timestamp);
                return json.toString();
            }

            @Override
            public PendingFriendRequest deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new PendingFriendRequest(
                        UUID.fromString(jsonObject.getString("from")),
                        UUID.fromString(jsonObject.getString("to")),
                        jsonObject.optString("fromName", "Unknown"),
                        jsonObject.optString("toName", "Unknown"),
                        jsonObject.getLong("timestamp")
                );
            }

            @Override
            public PendingFriendRequest clone(PendingFriendRequest value) {
                return new PendingFriendRequest(value.from, value.to, value.fromName, value.toName, value.timestamp);
            }
        };
    }
}
