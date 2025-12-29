package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRemoveAllResponseEvent extends FriendResponseEvent {
    private final UUID player;
    private final int removedCount;

    public FriendRemoveAllResponseEvent(UUID player, int removedCount) {
        super();
        this.player = player;
        this.removedCount = removedCount;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendRemoveAllResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRemoveAllResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("removedCount", value.removedCount);
                return json.toString();
            }

            @Override
            public FriendRemoveAllResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRemoveAllResponseEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        jsonObject.getInt("removedCount")
                );
            }

            @Override
            public FriendRemoveAllResponseEvent clone(FriendRemoveAllResponseEvent value) {
                return new FriendRemoveAllResponseEvent(value.player, value.removedCount);
            }
        };
    }
}
