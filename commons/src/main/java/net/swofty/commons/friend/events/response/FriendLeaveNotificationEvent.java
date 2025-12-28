package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendLeaveNotificationEvent extends FriendResponseEvent {
    private final UUID player;
    private final UUID friend;
    private final String friendName;

    public FriendLeaveNotificationEvent(UUID player, UUID friend, String friendName) {
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
    public Serializer<FriendLeaveNotificationEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendLeaveNotificationEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("friend", value.friend.toString());
                json.put("friendName", value.friendName);
                return json.toString();
            }

            @Override
            public FriendLeaveNotificationEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendLeaveNotificationEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        UUID.fromString(jsonObject.getString("friend")),
                        jsonObject.getString("friendName")
                );
            }

            @Override
            public FriendLeaveNotificationEvent clone(FriendLeaveNotificationEvent value) {
                return new FriendLeaveNotificationEvent(value.player, value.friend, value.friendName);
            }
        };
    }
}
