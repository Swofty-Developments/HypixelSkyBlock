package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendAcceptRequestEvent extends FriendEvent {
    private final UUID accepter;
    private final UUID requester;

    public FriendAcceptRequestEvent(UUID accepter, UUID requester) {
        super();
        this.accepter = accepter;
        this.requester = requester;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(accepter, requester);
    }

    @Override
    public Serializer<FriendAcceptRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendAcceptRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("accepter", value.accepter.toString());
                json.put("requester", value.requester.toString());
                return json.toString();
            }

            @Override
            public FriendAcceptRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendAcceptRequestEvent(
                        UUID.fromString(jsonObject.getString("accepter")),
                        UUID.fromString(jsonObject.getString("requester"))
                );
            }

            @Override
            public FriendAcceptRequestEvent clone(FriendAcceptRequestEvent value) {
                return new FriendAcceptRequestEvent(value.accepter, value.requester);
            }
        };
    }
}
