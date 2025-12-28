package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendJoinNotificationEvent extends FriendResponseEvent {
    private final UUID player;
    private final UUID friend;
    private final String friendName;

    public FriendJoinNotificationEvent(UUID player, UUID friend, String friendName) {
        super();
        this.player = player;
        this.friend = friend;
        this.friendName = friendName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendJoinNotificationEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendJoinNotificationEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("friend", value.friend.toString());
                json.put("friendName", value.friendName);
                return json.toString();
            }

            @Override
            public FriendJoinNotificationEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendJoinNotificationEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        UUID.fromString(jsonObject.getString("friend")),
                        jsonObject.getString("friendName")
                );
            }

            @Override
            public FriendJoinNotificationEvent clone(FriendJoinNotificationEvent value) {
                return new FriendJoinNotificationEvent(value.player, value.friend, value.friendName);
            }
        };
    }
}
