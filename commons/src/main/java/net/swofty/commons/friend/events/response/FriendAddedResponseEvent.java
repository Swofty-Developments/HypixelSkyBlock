package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendAddedResponseEvent extends FriendResponseEvent {
    private final UUID player1;
    private final UUID player2;
    private final String player1Name;
    private final String player2Name;

    public FriendAddedResponseEvent(UUID player1, UUID player2, String player1Name, String player2Name) {
        super();
        this.player1 = player1;
        this.player2 = player2;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player1, player2);
    }

    @Override
    public Serializer<FriendAddedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendAddedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("player1", value.player1.toString());
                json.put("player2", value.player2.toString());
                json.put("player1Name", value.player1Name);
                json.put("player2Name", value.player2Name);
                return json.toString();
            }

            @Override
            public FriendAddedResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendAddedResponseEvent(
                        UUID.fromString(jsonObject.getString("player1")),
                        UUID.fromString(jsonObject.getString("player2")),
                        jsonObject.getString("player1Name"),
                        jsonObject.getString("player2Name")
                );
            }

            @Override
            public FriendAddedResponseEvent clone(FriendAddedResponseEvent value) {
                return new FriendAddedResponseEvent(value.player1, value.player2, value.player1Name, value.player2Name);
            }
        };
    }
}
