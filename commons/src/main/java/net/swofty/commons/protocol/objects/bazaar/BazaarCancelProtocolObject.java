package net.swofty.commons.protocol.objects.bazaar;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class BazaarCancelProtocolObject extends ProtocolObject<
        BazaarCancelProtocolObject.CancelMessage,
        BazaarCancelProtocolObject.CancelResponse> {

    @Override
    public Serializer<CancelMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(CancelMessage v) {
                JSONObject o = new JSONObject();
                o.put("order-id",   v.orderId.toString());
                o.put("player-uuid", v.playerUuid.toString());
                return o.toString();
            }
            @Override
            public CancelMessage deserialize(String json) {
                JSONObject o = new JSONObject(json);
                return new CancelMessage(
                        UUID.fromString(o.getString("order-id")),
                        UUID.fromString(o.getString("player-uuid"))
                );
            }
            @Override
            public CancelMessage clone(CancelMessage v) {
                return new CancelMessage(v.orderId, v.playerUuid);
            }
        };
    }

    @Override
    public Serializer<CancelResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(CancelResponse v) {
                JSONObject o = new JSONObject();
                o.put("successful", v.successful);
                return o.toString();
            }
            @Override
            public CancelResponse deserialize(String json) {
                boolean ok = new JSONObject(json).getBoolean("successful");
                return new CancelResponse(ok);
            }
            @Override
            public CancelResponse clone(CancelResponse v) {
                return new CancelResponse(v.successful);
            }
        };
    }

    @AllArgsConstructor @NoArgsConstructor
    public static class CancelMessage {
        public UUID orderId;
        public UUID playerUuid;
    }

    @AllArgsConstructor @NoArgsConstructor
    public static class CancelResponse {
        public boolean successful;
    }
}
