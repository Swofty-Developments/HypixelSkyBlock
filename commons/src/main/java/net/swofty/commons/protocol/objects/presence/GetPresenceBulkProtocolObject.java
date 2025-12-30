package net.swofty.commons.protocol.objects.presence;

import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetPresenceBulkProtocolObject extends ProtocolObject<
        GetPresenceBulkProtocolObject.GetPresenceBulkMessage,
        GetPresenceBulkProtocolObject.GetPresenceBulkResponse> {

    @Override
    public Serializer<GetPresenceBulkMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetPresenceBulkMessage value) {
                JSONArray array = new JSONArray();
                for (UUID uuid : value.uuids) {
                    array.put(uuid.toString());
                }
                JSONObject json = new JSONObject();
                json.put("uuids", array);
                return json.toString();
            }

            @Override
            public GetPresenceBulkMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                JSONArray array = obj.getJSONArray("uuids");
                List<UUID> ids = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    ids.add(UUID.fromString(array.getString(i)));
                }
                return new GetPresenceBulkMessage(ids);
            }

            @Override
            public GetPresenceBulkMessage clone(GetPresenceBulkMessage value) {
                return new GetPresenceBulkMessage(new ArrayList<>(value.uuids));
            }
        };
    }

    @Override
    public Serializer<GetPresenceBulkResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetPresenceBulkResponse value) {
                JSONArray array = new JSONArray();
                for (PresenceInfo info : value.presence()) {
                    array.put(new JSONObject(PresenceInfo.getSerializer().serialize(info)));
                }
                JSONObject json = new JSONObject();
                json.put("presence", array);
                return json.toString();
            }

            @Override
            public GetPresenceBulkResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                JSONArray array = obj.getJSONArray("presence");
                List<PresenceInfo> presence = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    presence.add(PresenceInfo.getSerializer().deserialize(array.getJSONObject(i).toString()));
                }
                return new GetPresenceBulkResponse(presence);
            }

            @Override
            public GetPresenceBulkResponse clone(GetPresenceBulkResponse value) {
                return new GetPresenceBulkResponse(new ArrayList<>(value.presence()));
            }
        };
    }

    public record GetPresenceBulkMessage(List<UUID> uuids) {}

    public record GetPresenceBulkResponse(List<PresenceInfo> presence) {}
}

