package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendToggleBestRequestEvent extends FriendEvent {
    private final UUID player;
    private final UUID target;

    public FriendToggleBestRequestEvent(UUID player, UUID target) {
        super();
        this.player = player;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player, target);
    }

    @Override
    public Serializer<FriendToggleBestRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendToggleBestRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public FriendToggleBestRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendToggleBestRequestEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        UUID.fromString(jsonObject.getString("target"))
                );
            }

            @Override
            public FriendToggleBestRequestEvent clone(FriendToggleBestRequestEvent value) {
                return new FriendToggleBestRequestEvent(value.player, value.target);
            }
        };
    }
}
