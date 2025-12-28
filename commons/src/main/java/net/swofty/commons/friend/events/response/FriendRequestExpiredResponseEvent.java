package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRequestExpiredResponseEvent extends FriendResponseEvent {
    private final UUID sender;
    private final UUID target;
    private final String senderName;
    private final String targetName;

    public FriendRequestExpiredResponseEvent(UUID sender, UUID target, String senderName, String targetName) {
        super();
        this.sender = sender;
        this.target = target;
        this.senderName = senderName;
        this.targetName = targetName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender, target);
    }

    @Override
    public Serializer<FriendRequestExpiredResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRequestExpiredResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("sender", value.sender.toString());
                json.put("target", value.target.toString());
                json.put("senderName", value.senderName);
                json.put("targetName", value.targetName);
                return json.toString();
            }

            @Override
            public FriendRequestExpiredResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRequestExpiredResponseEvent(
                        UUID.fromString(jsonObject.getString("sender")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("senderName"),
                        jsonObject.getString("targetName")
                );
            }

            @Override
            public FriendRequestExpiredResponseEvent clone(FriendRequestExpiredResponseEvent value) {
                return new FriendRequestExpiredResponseEvent(value.sender, value.target, value.senderName, value.targetName);
            }
        };
    }
}
