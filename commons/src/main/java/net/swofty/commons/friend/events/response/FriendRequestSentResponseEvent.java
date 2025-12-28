package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRequestSentResponseEvent extends FriendResponseEvent {
    private final UUID sender;
    private final UUID target;
    private final String targetName;

    public FriendRequestSentResponseEvent(UUID sender, UUID target, String targetName) {
        super();
        this.sender = sender;
        this.target = target;
        this.targetName = targetName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender);
    }

    @Override
    public Serializer<FriendRequestSentResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRequestSentResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("sender", value.sender.toString());
                json.put("target", value.target.toString());
                json.put("targetName", value.targetName);
                return json.toString();
            }

            @Override
            public FriendRequestSentResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRequestSentResponseEvent(
                        UUID.fromString(jsonObject.getString("sender")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("targetName")
                );
            }

            @Override
            public FriendRequestSentResponseEvent clone(FriendRequestSentResponseEvent value) {
                return new FriendRequestSentResponseEvent(value.sender, value.target, value.targetName);
            }
        };
    }
}
