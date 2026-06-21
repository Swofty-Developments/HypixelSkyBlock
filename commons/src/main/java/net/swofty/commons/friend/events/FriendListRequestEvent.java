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
public final class FriendListRequestEvent extends FriendEvent {
    private static final Serializer<FriendListRequestEvent> SERIALIZER =
            new JacksonSerializer<>(FriendListRequestEvent.class);

    private final UUID player;
    private final int page;
    private final boolean bestOnly;

    @JsonCreator
    public FriendListRequestEvent(@JsonProperty("player") UUID player, @JsonProperty("page") int page, @JsonProperty("bestOnly") boolean bestOnly) {
        super();
        this.player = player;
        this.page = page;
        this.bestOnly = bestOnly;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendListRequestEvent> getSerializer() {
        return SERIALIZER;
    }
}
