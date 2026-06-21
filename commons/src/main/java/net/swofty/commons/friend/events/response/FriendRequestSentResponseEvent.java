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
public final class FriendRequestSentResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendRequestSentResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendRequestSentResponseEvent.class);

    private final UUID sender;
    private final UUID target;
    private final String targetName;

    @JsonCreator
    public FriendRequestSentResponseEvent(@JsonProperty("sender") UUID sender, @JsonProperty("target") UUID target, @JsonProperty("targetName") String targetName) {
        super();
        this.sender = sender;
        this.target = target;
        this.targetName = targetName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender);
    }

    @Override
    public Serializer<FriendRequestSentResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
