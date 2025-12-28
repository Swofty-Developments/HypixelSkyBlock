package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendNicknameSetResponseEvent extends FriendResponseEvent {
    private final UUID player;
    private final UUID target;
    private final String targetName;
    private final String nickname;

    public FriendNicknameSetResponseEvent(UUID player, UUID target, String targetName, String nickname) {
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
        return new Serializer<>() {
            @Override
            public String serialize(FriendNicknameSetResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("target", value.target.toString());
                json.put("targetName", value.targetName);
                json.put("nickname", value.nickname != null ? value.nickname : JSONObject.NULL);
                return json.toString();
            }

            @Override
            public FriendNicknameSetResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendNicknameSetResponseEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("targetName"),
                        jsonObject.isNull("nickname") ? null : jsonObject.getString("nickname")
                );
            }

            @Override
            public FriendNicknameSetResponseEvent clone(FriendNicknameSetResponseEvent value) {
                return new FriendNicknameSetResponseEvent(value.player, value.target, value.targetName, value.nickname);
            }
        };
    }
}
