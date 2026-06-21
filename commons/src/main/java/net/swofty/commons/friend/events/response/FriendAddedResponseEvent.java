package net.swofty.commons.friend.events.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

@Getter
public final class FriendAddedResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendAddedResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendAddedResponseEvent.class);

    private final UUID player1;
    private final UUID player2;
    private final String player1Name;
    private final String player2Name;

    @JsonCreator
    public FriendAddedResponseEvent(@JsonProperty("player1") UUID player1, @JsonProperty("player2") UUID player2, @JsonProperty("player1Name") String player1Name, @JsonProperty("player2Name") String player2Name) {
        super();
        this.player1 = player1;
        this.player2 = player2;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player1, player2);
    }

    @Override
    public Serializer<FriendAddedResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
