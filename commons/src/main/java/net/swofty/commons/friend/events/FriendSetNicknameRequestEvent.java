package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendSetNicknameRequestEvent extends FriendEvent {
    private final UUID player;
    private final UUID target;
    private final String nickname;

    public FriendSetNicknameRequestEvent(UUID player, UUID target, String nickname) {
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
        return new Serializer<>() {
            @Override
            public String serialize(FriendSetNicknameRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("target", value.target.toString());
                json.put("nickname", value.nickname != null ? value.nickname : JSONObject.NULL);
                return json.toString();
            }

            @Override
            public FriendSetNicknameRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendSetNicknameRequestEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.isNull("nickname") ? null : jsonObject.getString("nickname")
                );
            }

            @Override
            public FriendSetNicknameRequestEvent clone(FriendSetNicknameRequestEvent value) {
                return new FriendSetNicknameRequestEvent(value.player, value.target, value.nickname);
            }
        };
    }
}
