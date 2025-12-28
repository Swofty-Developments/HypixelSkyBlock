package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.friend.PendingFriendRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetPendingFriendRequestsProtocolObject extends ProtocolObject
        <GetPendingFriendRequestsProtocolObject.GetPendingRequestsMessage,
                GetPendingFriendRequestsProtocolObject.GetPendingRequestsResponse> {

    @Override
    public Serializer<GetPendingRequestsMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetPendingRequestsMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerUuid", value.playerUuid.toString());
                return json.toString();
            }

            @Override
            public GetPendingRequestsMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new GetPendingRequestsMessage(
                        UUID.fromString(jsonObject.getString("playerUuid"))
                );
            }

            @Override
            public GetPendingRequestsMessage clone(GetPendingRequestsMessage value) {
                return new GetPendingRequestsMessage(value.playerUuid);
            }
        };
    }

    @Override
    public Serializer<GetPendingRequestsResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetPendingRequestsResponse value) {
                JSONObject json = new JSONObject();
                JSONArray requestsArray = new JSONArray();
                Serializer<PendingFriendRequest> requestSerializer = PendingFriendRequest.getStaticSerializer();
                for (PendingFriendRequest request : value.requests) {
                    requestsArray.put(requestSerializer.serialize(request));
                }
                json.put("requests", requestsArray);
                return json.toString();
            }

            @Override
            public GetPendingRequestsResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray requestsArray = jsonObject.getJSONArray("requests");
                List<PendingFriendRequest> requests = new ArrayList<>();
                Serializer<PendingFriendRequest> requestSerializer = PendingFriendRequest.getStaticSerializer();
                for (int i = 0; i < requestsArray.length(); i++) {
                    requests.add(requestSerializer.deserialize(requestsArray.getString(i)));
                }
                return new GetPendingRequestsResponse(requests);
            }

            @Override
            public GetPendingRequestsResponse clone(GetPendingRequestsResponse value) {
                return new GetPendingRequestsResponse(new ArrayList<>(value.requests));
            }
        };
    }

    public record GetPendingRequestsMessage(UUID playerUuid) {
    }

    public record GetPendingRequestsResponse(List<PendingFriendRequest> requests) {
    }
}
