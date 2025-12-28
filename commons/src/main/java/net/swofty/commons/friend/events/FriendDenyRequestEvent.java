package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendDenyRequestEvent extends FriendEvent {
    private final UUID denier;
    private final UUID requester;

    public FriendDenyRequestEvent(UUID denier, UUID requester) {
        super();
        this.denier = denier;
        this.requester = requester;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(denier, requester);
    }

    @Override
    public Serializer<FriendDenyRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendDenyRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("denier", value.denier.toString());
                json.put("requester", value.requester.toString());
                return json.toString();
            }

            @Override
            public FriendDenyRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendDenyRequestEvent(
                        UUID.fromString(jsonObject.getString("denier")),
                        UUID.fromString(jsonObject.getString("requester"))
                );
            }

            @Override
            public FriendDenyRequestEvent clone(FriendDenyRequestEvent value) {
                return new FriendDenyRequestEvent(value.denier, value.requester);
            }
        };
    }
}
