package net.swofty.commons.friend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

@Getter
public class Friend {
    private static final Serializer<Friend> SERIALIZER = new JacksonSerializer<>(Friend.class);

    private final UUID uuid;
    @Setter
    private String nickname;
    @Setter
    private boolean bestFriend;
    private final long addedTimestamp;

    @JsonCreator
    public Friend(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("nickname") String nickname,
            @JsonProperty("bestFriend") boolean bestFriend,
            @JsonProperty("addedTimestamp") long addedTimestamp) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.bestFriend = bestFriend;
        this.addedTimestamp = addedTimestamp;
    }

    public static Friend create(UUID uuid) {
        return new Friend(uuid, null, false, System.currentTimeMillis());
    }

    public static Serializer<Friend> getStaticSerializer() {
        return SERIALIZER;
    }

    public Serializer<Friend> getSerializer() {
        return SERIALIZER;
    }
}
