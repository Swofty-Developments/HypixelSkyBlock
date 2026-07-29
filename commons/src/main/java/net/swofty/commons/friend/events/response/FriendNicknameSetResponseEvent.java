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
public final class FriendNicknameSetResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendNicknameSetResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendNicknameSetResponseEvent.class);

    private final UUID player;
    private final UUID target;
    private final String targetName;
    private final String nickname;

    @JsonCreator
    public FriendNicknameSetResponseEvent(@JsonProperty("player") UUID player, @JsonProperty("target") UUID target, @JsonProperty("targetName") String targetName, @JsonProperty("nickname") String nickname) {
        super();
        this.player = player;
        this.target = target;
        this.targetName = targetName;
        this.nickname = nickname;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendNicknameSetResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
