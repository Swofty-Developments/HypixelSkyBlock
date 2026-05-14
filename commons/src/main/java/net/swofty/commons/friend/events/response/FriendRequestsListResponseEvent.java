package net.swofty.commons.friend.events.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public final class FriendRequestsListResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendRequestsListResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendRequestsListResponseEvent.class);

    private final UUID player;
    private final List<FriendRequestEntry> requests;
    private final int page;
    private final int totalPages;

    @JsonCreator
    public FriendRequestsListResponseEvent(@JsonProperty("player") UUID player, @JsonProperty("requests") List<FriendRequestEntry> requests, @JsonProperty("page") int page, @JsonProperty("totalPages") int totalPages) {
        super();
        this.player = player;
        this.requests = requests;
        this.page = page;
        this.totalPages = totalPages;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendRequestsListResponseEvent> getSerializer() {
        return SERIALIZER;
    }

    @Getter
    public static class FriendRequestEntry {
        private final UUID senderUuid;
        private final String senderName;
        private final long timestamp;

        @JsonCreator
        public FriendRequestEntry(@JsonProperty("senderUuid") UUID senderUuid, @JsonProperty("senderName") String senderName, @JsonProperty("timestamp") long timestamp) {
            this.senderUuid = senderUuid;
            this.senderName = senderName;
            this.timestamp = timestamp;
        }
    }
}
