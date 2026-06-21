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
public final class FriendRemovedResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendRemovedResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendRemovedResponseEvent.class);

    private final UUID remover;
    private final UUID removed;
    private final String removerName;

    @JsonCreator
    public FriendRemovedResponseEvent(@JsonProperty("remover") UUID remover, @JsonProperty("removed") UUID removed, @JsonProperty("removerName") String removerName) {
        super();
        this.remover = remover;
        this.removed = removed;
        this.removerName = removerName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(remover, removed);
    }

    @Override
    public Serializer<FriendRemovedResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
