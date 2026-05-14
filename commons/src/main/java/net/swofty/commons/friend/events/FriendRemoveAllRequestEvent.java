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
public final class FriendRemoveAllRequestEvent extends FriendEvent {
    private static final Serializer<FriendRemoveAllRequestEvent> SERIALIZER =
            new JacksonSerializer<>(FriendRemoveAllRequestEvent.class);

    private final UUID player;

    @JsonCreator
    public FriendRemoveAllRequestEvent(@JsonProperty("player") UUID player) {
        super();
        this.player = player;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendRemoveAllRequestEvent> getSerializer() {
        return SERIALIZER;
    }
}
