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
public final class FriendRemoveAllResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendRemoveAllResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendRemoveAllResponseEvent.class);

    private final UUID player;
    private final int removedCount;

    @JsonCreator
    public FriendRemoveAllResponseEvent(@JsonProperty("player") UUID player, @JsonProperty("removedCount") int removedCount) {
        super();
        this.player = player;
        this.removedCount = removedCount;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendRemoveAllResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
