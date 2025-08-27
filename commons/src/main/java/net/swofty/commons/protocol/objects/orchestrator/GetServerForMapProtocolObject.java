package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class GetServerForMapProtocolObject extends ProtocolObject
		<GetServerForMapProtocolObject.GetServerForMapMessage,
				GetServerForMapProtocolObject.GetServerForMapResponse> {

	@Override
	public Serializer<GetServerForMapMessage> getSerializer() {
		return new Serializer<GetServerForMapMessage>() {
			@Override
			public String serialize(GetServerForMapMessage value) {
				JSONObject json = new JSONObject();
				json.put("type", value.type.name());
				json.put("map", value.map);
				json.put("neededSlots", value.neededSlots);
				return json.toString();
			}

			@Override
			public GetServerForMapMessage deserialize(String json) {
				JSONObject obj = new JSONObject(json);
				return new GetServerForMapMessage(
						ServerType.valueOf(obj.getString("type")),
						obj.getString("map"),
						obj.getInt("neededSlots")
				);
			}

			@Override
			public GetServerForMapMessage clone(GetServerForMapMessage value) {
				return new GetServerForMapMessage(value.type, value.map, value.neededSlots);
			}
		};
	}

	@Override
	public Serializer<GetServerForMapResponse> getReturnSerializer() {
		return new Serializer<GetServerForMapResponse>() {
			@Override
			public String serialize(GetServerForMapResponse value) {
				return value.server() == null ? new JSONObject().put("server", (Object) null).toString()
						: new JSONObject().put("server", value.server().toJSON()).toString();
			}

			@Override
			public GetServerForMapResponse deserialize(String json) {
				JSONObject obj = new JSONObject(json);
				if (obj.isNull("server")) return new GetServerForMapResponse(null);
				return new GetServerForMapResponse(UnderstandableProxyServer.singleFromJSON(obj.getJSONObject("server")));
			}

			@Override
			public GetServerForMapResponse clone(GetServerForMapResponse value) {
				return new GetServerForMapResponse(value.server);
			}
		};
	}

	public record GetServerForMapMessage(ServerType type, String map, int neededSlots) {
	}

	public record GetServerForMapResponse(UnderstandableProxyServer server) {
	}
}
