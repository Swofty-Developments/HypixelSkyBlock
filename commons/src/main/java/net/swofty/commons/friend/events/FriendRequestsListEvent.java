package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRequestsListEvent extends FriendEvent {
    private final UUID player;
    private final int page;

    public FriendRequestsListEvent(UUID player, int page) {
        super();
        this.player = player;
        this.page = page;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendRequestsListEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRequestsListEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("page", value.page);
                return json.toString();
            }

            @Override
            public FriendRequestsListEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRequestsListEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        jsonObject.getInt("page")
                );
            }

            @Override
            public FriendRequestsListEvent clone(FriendRequestsListEvent value) {
                return new FriendRequestsListEvent(value.player, value.page);
            }
        };
    }
}
