package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.friend.FriendData;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class GetFriendDataProtocolObject extends ProtocolObject
        <GetFriendDataProtocolObject.GetFriendDataMessage,
                GetFriendDataProtocolObject.GetFriendDataResponse> {

    @Override
    public Serializer<GetFriendDataMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetFriendDataMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerUuid", value.playerUuid.toString());
                return json.toString();
            }

            @Override
            public GetFriendDataMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID playerUuid = UUID.fromString(jsonObject.getString("playerUuid"));
                return new GetFriendDataMessage(playerUuid);
            }

            @Override
            public GetFriendDataMessage clone(GetFriendDataMessage value) {
                return new GetFriendDataMessage(value.playerUuid);
            }
        };
    }

    @Override
    public Serializer<GetFriendDataResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetFriendDataResponse value) {
                JSONObject json = new JSONObject();
                if (value.friendData != null) {
                    json.put("friendData", value.friendData.getSerializer().serialize(value.friendData));
                } else {
                    json.put("friendData", JSONObject.NULL);
                }
                return json.toString();
            }

            @Override
            public GetFriendDataResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.isNull("friendData")) {
                    return new GetFriendDataResponse(null);
                }
                FriendData friendData = FriendData.getStaticSerializer().deserialize(jsonObject.getString("friendData"));
                return new GetFriendDataResponse(friendData);
            }

            @Override
            public GetFriendDataResponse clone(GetFriendDataResponse value) {
                return new GetFriendDataResponse(value.friendData);
            }
        };
    }

    public record GetFriendDataMessage(UUID playerUuid) {
    }

    public record GetFriendDataResponse(FriendData friendData) {
    }
}
