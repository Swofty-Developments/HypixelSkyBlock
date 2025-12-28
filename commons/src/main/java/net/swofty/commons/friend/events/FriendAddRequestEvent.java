package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendAddRequestEvent extends FriendEvent {
    private final UUID sender;
    private final UUID target;

    public FriendAddRequestEvent(UUID sender, UUID target) {
        super();
        this.sender = sender;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(sender, target);
    }

    @Override
    public Serializer<FriendAddRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendAddRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("sender", value.sender.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public FriendAddRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendAddRequestEvent(
                        UUID.fromString(jsonObject.getString("sender")),
                        UUID.fromString(jsonObject.getString("target"))
                );
            }

            @Override
            public FriendAddRequestEvent clone(FriendAddRequestEvent value) {
                return new FriendAddRequestEvent(value.sender, value.target);
            }
        };
    }
}
