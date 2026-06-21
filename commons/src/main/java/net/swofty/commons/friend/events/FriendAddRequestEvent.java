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
public final class FriendAddRequestEvent extends FriendEvent {
    private static final Serializer<FriendAddRequestEvent> SERIALIZER =
            new JacksonSerializer<>(FriendAddRequestEvent.class);

    private final UUID sender;
    private final UUID target;

    @JsonCreator
    public FriendAddRequestEvent(@JsonProperty("sender") UUID sender, @JsonProperty("target") UUID target) {
        super();
        this.sender = sender;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender, target);
    }

    @Override
    public Serializer<FriendAddRequestEvent> getSerializer() {
        return SERIALIZER;
    }
}
