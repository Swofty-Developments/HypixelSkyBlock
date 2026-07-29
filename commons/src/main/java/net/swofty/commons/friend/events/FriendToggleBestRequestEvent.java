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
public final class FriendToggleBestRequestEvent extends FriendEvent {
    private static final Serializer<FriendToggleBestRequestEvent> SERIALIZER =
            new JacksonSerializer<>(FriendToggleBestRequestEvent.class);

    private final UUID player;
    private final UUID target;

    @JsonCreator
    public FriendToggleBestRequestEvent(@JsonProperty("player") UUID player, @JsonProperty("target") UUID target) {
        super();
        this.player = player;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player, target);
    }

    @Override
    public Serializer<FriendToggleBestRequestEvent> getSerializer() {
        return SERIALIZER;
    }
}
