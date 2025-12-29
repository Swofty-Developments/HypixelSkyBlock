package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendDeniedResponseEvent extends FriendResponseEvent {
    private final UUID denier;
    private final UUID requester;
    private final String denierName;

    public FriendDeniedResponseEvent(UUID denier, UUID requester, String denierName) {
        super();
        this.denier = denier;
        this.requester = requester;
        this.denierName = denierName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(requester);
    }

    @Override
    public Serializer<FriendDeniedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendDeniedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("denier", value.denier.toString());
                json.put("requester", value.requester.toString());
                json.put("denierName", value.denierName);
                return json.toString();
            }

            @Override
            public FriendDeniedResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendDeniedResponseEvent(
                        UUID.fromString(jsonObject.getString("denier")),
                        UUID.fromString(jsonObject.getString("requester")),
                        jsonObject.getString("denierName")
                );
            }

            @Override
            public FriendDeniedResponseEvent clone(FriendDeniedResponseEvent value) {
                return new FriendDeniedResponseEvent(value.denier, value.requester, value.denierName);
            }
        };
    }
}
