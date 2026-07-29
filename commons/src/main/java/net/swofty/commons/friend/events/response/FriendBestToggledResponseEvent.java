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
public final class FriendBestToggledResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendBestToggledResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendBestToggledResponseEvent.class);

    private final UUID player;
    private final UUID target;
    private final String targetName;
    private final boolean isBest;

    @JsonCreator
    public FriendBestToggledResponseEvent(@JsonProperty("player") UUID player, @JsonProperty("target") UUID target, @JsonProperty("targetName") String targetName, @JsonProperty("isBest") boolean isBest) {
        super();
        this.player = player;
        this.target = target;
        this.targetName = targetName;
        this.isBest = isBest;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendBestToggledResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
