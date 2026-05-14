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
public final class FriendListResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendListResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendListResponseEvent.class);

    private final UUID player;
    private final List<FriendListEntry> friends;
    private final int page;
    private final int totalPages;
    private final boolean bestOnly;

    @JsonCreator
    public FriendListResponseEvent(@JsonProperty("player") UUID player, @JsonProperty("friends") List<FriendListEntry> friends, @JsonProperty("page") int page, @JsonProperty("totalPages") int totalPages, @JsonProperty("bestOnly") boolean bestOnly) {
        super();
        this.player = player;
        this.friends = friends;
        this.page = page;
        this.totalPages = totalPages;
        this.bestOnly = bestOnly;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendListResponseEvent> getSerializer() {
        return SERIALIZER;
    }

    @Getter
    public static class FriendListEntry {
        private final UUID uuid;
        private final String name;
        private final String nickname;
        private final boolean isBest;
        private final boolean isOnline;
        private final long lastSeen;
        private final long friendSince;
        private final String server;

        @JsonCreator
        public FriendListEntry(@JsonProperty("uuid") UUID uuid, @JsonProperty("name") String name, @JsonProperty("nickname") String nickname, @JsonProperty("isBest") boolean isBest, @JsonProperty("isOnline") boolean isOnline, @JsonProperty("lastSeen") long lastSeen, @JsonProperty("friendSince") long friendSince, @JsonProperty("server") String server) {
            this.uuid = uuid;
            this.name = name;
            this.nickname = nickname;
            this.isBest = isBest;
            this.isOnline = isOnline;
            this.lastSeen = lastSeen;
            this.friendSince = friendSince;
            this.server = server;
        }
    }
}
