package net.swofty.commons.friend.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

@Getter
public final class FriendAcceptRequestEvent extends FriendEvent {
    private static final Serializer<FriendAcceptRequestEvent> SERIALIZER =
            new JacksonSerializer<>(FriendAcceptRequestEvent.class);

    private final UUID accepter;
    private final UUID requester;

    @JsonCreator
    public FriendAcceptRequestEvent(@JsonProperty("accepter") UUID accepter, @JsonProperty("requester") UUID requester) {
        super();
        this.accepter = accepter;
        this.requester = requester;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(accepter, requester);
    }

    @Override
    public Serializer<FriendAcceptRequestEvent> getSerializer() {
        return SERIALIZER;
    }
}
