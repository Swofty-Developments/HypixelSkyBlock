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
public final class FriendDeniedResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendDeniedResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendDeniedResponseEvent.class);

    private final UUID denier;
    private final UUID requester;
    private final String denierName;

    @JsonCreator
    public FriendDeniedResponseEvent(@JsonProperty("denier") UUID denier, @JsonProperty("requester") UUID requester, @JsonProperty("denierName") String denierName) {
        super();
        this.denier = denier;
        this.requester = requester;
        this.denierName = denierName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(requester);
    }

    @Override
    public Serializer<FriendDeniedResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
