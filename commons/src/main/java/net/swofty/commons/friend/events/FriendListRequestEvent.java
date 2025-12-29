package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendListRequestEvent extends FriendEvent {
    private final UUID player;
    private final int page;
    private final boolean bestOnly;

    public FriendListRequestEvent(UUID player, int page, boolean bestOnly) {
        super();
        this.player = player;
        this.page = page;
        this.bestOnly = bestOnly;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendListRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendListRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("page", value.page);
                json.put("bestOnly", value.bestOnly);
                return json.toString();
            }

            @Override
            public FriendListRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendListRequestEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        jsonObject.getInt("page"),
                        jsonObject.getBoolean("bestOnly")
                );
            }

            @Override
            public FriendListRequestEvent clone(FriendListRequestEvent value) {
                return new FriendListRequestEvent(value.player, value.page, value.bestOnly);
            }
        };
    }
}
