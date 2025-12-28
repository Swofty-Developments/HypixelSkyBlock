package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRemoveAllRequestEvent extends FriendEvent {
    private final UUID player;

    public FriendRemoveAllRequestEvent(UUID player) {
        super();
        this.player = player;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendRemoveAllRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRemoveAllRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                return json.toString();
            }

            @Override
            public FriendRemoveAllRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRemoveAllRequestEvent(
                        UUID.fromString(jsonObject.getString("player"))
                );
            }

            @Override
            public FriendRemoveAllRequestEvent clone(FriendRemoveAllRequestEvent value) {
                return new FriendRemoveAllRequestEvent(value.player);
            }
        };
    }
}
