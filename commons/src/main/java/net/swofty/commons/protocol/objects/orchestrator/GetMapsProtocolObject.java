package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class GetMapsProtocolObject extends ProtocolObject
        <GetMapsProtocolObject.GetMapsMessage,
                GetMapsProtocolObject.GetMapsResponse> {

    @Override
    public Serializer<GetMapsMessage> getSerializer() {
        return new Serializer<GetMapsMessage>() {
            @Override
            public String serialize(GetMapsMessage value) {
                JSONObject json = new JSONObject();
                json.put("type", value.type.name());
                if (value.mode != null) json.put("mode", value.mode);
                return json.toString();
            }

            @Override
            public GetMapsMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GetMapsMessage(ServerType.valueOf(obj.getString("type")), obj.has("mode") ? obj.getString("mode") : null);
            }

            @Override
            public GetMapsMessage clone(GetMapsMessage value) {
                return new GetMapsMessage(value.type, value.mode);
            }
        };
    }

    @Override
    public Serializer<GetMapsResponse> getReturnSerializer() {
        return new Serializer<GetMapsResponse>() {
            @Override
            public String serialize(GetMapsResponse value) {
                JSONObject json = new JSONObject();
                json.put("maps", new JSONArray(value.maps));
                return json.toString();
            }

            @Override
            public GetMapsResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                List<String> maps = new ArrayList<>();
                JSONArray arr = obj.getJSONArray("maps");
                for (int i = 0; i < arr.length(); i++) maps.add(arr.getString(i));
                return new GetMapsResponse(maps);
            }

            @Override
            public GetMapsResponse clone(GetMapsResponse value) {
                return new GetMapsResponse(new ArrayList<>(value.maps));
            }
        };
    }

    public record GetMapsMessage(ServerType type, String mode) { }

    public record GetMapsResponse(List<String> maps) { }
}
