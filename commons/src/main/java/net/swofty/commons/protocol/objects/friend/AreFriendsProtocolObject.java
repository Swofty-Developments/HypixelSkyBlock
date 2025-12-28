package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class AreFriendsProtocolObject extends ProtocolObject
        <AreFriendsProtocolObject.AreFriendsMessage,
                AreFriendsProtocolObject.AreFriendsResponse> {

    @Override
    public Serializer<AreFriendsMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(AreFriendsMessage value) {
                JSONObject json = new JSONObject();
                json.put("player1", value.player1.toString());
                json.put("player2", value.player2.toString());
                return json.toString();
            }

            @Override
            public AreFriendsMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new AreFriendsMessage(
                        UUID.fromString(jsonObject.getString("player1")),
                        UUID.fromString(jsonObject.getString("player2"))
                );
            }

            @Override
            public AreFriendsMessage clone(AreFriendsMessage value) {
                return new AreFriendsMessage(value.player1, value.player2);
            }
        };
    }

    @Override
    public Serializer<AreFriendsResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(AreFriendsResponse value) {
                return value.areFriends ? "true" : "false";
            }

            @Override
            public AreFriendsResponse deserialize(String json) {
                return new AreFriendsResponse(json.equals("true"));
            }

            @Override
            public AreFriendsResponse clone(AreFriendsResponse value) {
                return new AreFriendsResponse(value.areFriends);
            }
        };
    }

    public record AreFriendsMessage(UUID player1, UUID player2) {
    }

    public record AreFriendsResponse(boolean areFriends) {
    }
}
