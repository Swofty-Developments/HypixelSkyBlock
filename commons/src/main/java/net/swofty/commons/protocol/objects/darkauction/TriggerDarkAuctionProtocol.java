package net.swofty.commons.protocol.objects.darkauction;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class TriggerDarkAuctionProtocol extends ProtocolObject<
        TriggerDarkAuctionProtocol.TriggerMessage,
        TriggerDarkAuctionProtocol.TriggerResponse> {

    @Override
    public Serializer<TriggerMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(TriggerMessage value) {
                JSONObject json = new JSONObject();
                json.put("calendarTime", value.calendarTime);
                json.put("forced", value.forced);
                return json.toString();
            }

            @Override
            public TriggerMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                long calendarTime = jsonObject.optLong("calendarTime", 0);
                boolean forced = jsonObject.optBoolean("forced", false);
                return new TriggerMessage(calendarTime, forced);
            }

            @Override
            public TriggerMessage clone(TriggerMessage value) {
                return new TriggerMessage(value.calendarTime, value.forced);
            }
        };
    }

    @Override
    public Serializer<TriggerResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(TriggerResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("message", value.message);
                return json.toString();
            }

            @Override
            public TriggerResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                boolean success = jsonObject.getBoolean("success");
                String message = jsonObject.optString("message", "");
                return new TriggerResponse(success, message);
            }

            @Override
            public TriggerResponse clone(TriggerResponse value) {
                return new TriggerResponse(value.success, value.message);
            }
        };
    }

    public record TriggerMessage(long calendarTime, boolean forced) {
        public TriggerMessage(long calendarTime) {
            this(calendarTime, false);
        }
    }

    public record TriggerResponse(boolean success, String message) {}
}
