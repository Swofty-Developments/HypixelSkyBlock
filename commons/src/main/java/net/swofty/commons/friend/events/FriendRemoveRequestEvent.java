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
public final class FriendRemoveRequestEvent extends FriendEvent {
    private static final Serializer<FriendRemoveRequestEvent> SERIALIZER =
            new JacksonSerializer<>(FriendRemoveRequestEvent.class);

    private final UUID remover;
    private final UUID target;

    @JsonCreator
    public FriendRemoveRequestEvent(@JsonProperty("remover") UUID remover, @JsonProperty("target") UUID target) {
        super();
        this.remover = remover;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(remover, target);
    }

    @Override
    public Serializer<FriendRemoveRequestEvent> getSerializer() {
        return SERIALIZER;
    }
}
