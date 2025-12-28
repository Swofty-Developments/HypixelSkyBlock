package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRequestReceivedResponseEvent extends FriendResponseEvent {
    private final UUID sender;
    private final UUID target;
    private final String senderName;

    public FriendRequestReceivedResponseEvent(UUID sender, UUID target, String senderName) {
        super();
        this.sender = sender;
        this.target = target;
        this.senderName = senderName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(target);
    }

    @Override
    public Serializer<FriendRequestReceivedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRequestReceivedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("sender", value.sender.toString());
                json.put("target", value.target.toString());
                json.put("senderName", value.senderName);
                return json.toString();
            }

            @Override
            public FriendRequestReceivedResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRequestReceivedResponseEvent(
                        UUID.fromString(jsonObject.getString("sender")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("senderName")
                );
            }

            @Override
            public FriendRequestReceivedResponseEvent clone(FriendRequestReceivedResponseEvent value) {
                return new FriendRequestReceivedResponseEvent(value.sender, value.target, value.senderName);
            }
        };
    }
}
