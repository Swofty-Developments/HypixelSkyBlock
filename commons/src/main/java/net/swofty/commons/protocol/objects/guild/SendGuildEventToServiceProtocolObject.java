package net.swofty.commons.protocol.objects.guild;

import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class SendGuildEventToServiceProtocolObject extends ProtocolObject
        <SendGuildEventToServiceProtocolObject.SendGuildEventToServiceMessage,
        SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse> {

    @Override
    public Serializer<SendGuildEventToServiceMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(SendGuildEventToServiceMessage value) {
                JSONObject json = new JSONObject();
                json.put("event", value.event.getSerializer().serialize(value.event));
                json.put("eventType", value.event.getClass().getSimpleName());
                return json.toString();
            }

            @Override
            public SendGuildEventToServiceMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                String eventType = jsonObject.getString("eventType");
                GuildEvent event = GuildEvent.findFromType(eventType);
                GuildEvent deserializedEvent = (GuildEvent) event.getSerializer().deserialize(jsonObject.getString("event"));
                return new SendGuildEventToServiceMessage(deserializedEvent);
            }

            @Override
            public SendGuildEventToServiceMessage clone(SendGuildEventToServiceMessage value) {
                return new SendGuildEventToServiceMessage(value.event);
            }
        };
    }

    @Override
    public Serializer<SendGuildEventToServiceResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(SendGuildEventToServiceResponse value) {
                return value.success ? "true" : "false";
            }

            @Override
            public SendGuildEventToServiceResponse deserialize(String json) {
                return new SendGuildEventToServiceResponse(json.equals("true"));
            }

            @Override
            public SendGuildEventToServiceResponse clone(SendGuildEventToServiceResponse value) {
                return new SendGuildEventToServiceResponse(value.success);
            }
        };
    }

    public record SendGuildEventToServiceMessage(GuildEvent event) { }
    public record SendGuildEventToServiceResponse(boolean success) { }
}
