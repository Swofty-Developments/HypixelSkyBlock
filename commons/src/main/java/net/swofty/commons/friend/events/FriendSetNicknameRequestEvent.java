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
public final class FriendSetNicknameRequestEvent extends FriendEvent {
    private static final Serializer<FriendSetNicknameRequestEvent> SERIALIZER =
            new JacksonSerializer<>(FriendSetNicknameRequestEvent.class);

    private final UUID player;
    private final UUID target;
    private final String nickname;

    @JsonCreator
    public FriendSetNicknameRequestEvent(@JsonProperty("player") UUID player, @JsonProperty("target") UUID target, @JsonProperty("nickname") String nickname) {
        super();
        this.player = player;
        this.target = target;
        this.nickname = nickname;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendSetNicknameRequestEvent> getSerializer() {
        return SERIALIZER;
    }
}
