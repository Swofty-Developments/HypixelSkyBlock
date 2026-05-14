package net.swofty.commons.friend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

@Getter
public class PendingFriendRequest {
    private static final Serializer<PendingFriendRequest> SERIALIZER =
            new JacksonSerializer<>(PendingFriendRequest.class);

    private final UUID from;
    private final UUID to;
    private final String fromName;
    private final String toName;
    private final long timestamp;

    @JsonCreator
    public PendingFriendRequest(
            @JsonProperty("from") UUID from,
            @JsonProperty("to") UUID to,
            @JsonProperty("fromName") String fromName,
            @JsonProperty("toName") String toName,
            @JsonProperty("timestamp") long timestamp) {
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
        return SERIALIZER;
    }

    public Serializer<PendingFriendRequest> getSerializer() {
        return SERIALIZER;
    }
}
