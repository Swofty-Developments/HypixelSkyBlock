package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import org.bson.Document;
import org.json.JSONObject;

import java.util.UUID;

public class SendPartyEventToServiceProtocolObject extends ProtocolObject
        <SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage,
        SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse> {


    @Override
    public Serializer<SendPartyEventToServiceMessage> getSerializer() {
        return new Serializer<SendPartyEventToServiceMessage>() {
            @Override
            public String serialize(SendPartyEventToServiceMessage value) {
                JSONObject json = new JSONObject();
                json.put("event", value.event.getSerializer().serialize(value.event));
                json.put("eventType", value.event.getClass().getSimpleName());
                return json.toString();
            }

            @Override
            public SendPartyEventToServiceMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                String eventType = jsonObject.getString("eventType");
                PartyEvent event = PartyEvent.findFromType(eventType);
                PartyEvent deserializedEvent = (PartyEvent) event.getSerializer().deserialize(jsonObject.getString("event"));
                return new SendPartyEventToServiceMessage(deserializedEvent);
            }

            @Override
            public SendPartyEventToServiceMessage clone(SendPartyEventToServiceMessage value) {
                return new SendPartyEventToServiceMessage(value.event);
            }
        };
    }

    @Override
    public Serializer<SendPartyEventToServiceResponse> getReturnSerializer() {
        return new Serializer<SendPartyEventToServiceResponse>() {
            @Override
            public String serialize(SendPartyEventToServiceResponse value) {
                return value.success ? "true" : "false";
            }

            @Override
            public SendPartyEventToServiceResponse deserialize(String json) {
                return new SendPartyEventToServiceResponse(json.equals("true"));
            }

            @Override
            public SendPartyEventToServiceResponse clone(SendPartyEventToServiceResponse value) {
                return new SendPartyEventToServiceResponse(value.success);
            }
        };
    }

    public record SendPartyEventToServiceMessage(PartyEvent event) {
    }

    public record SendPartyEventToServiceResponse(boolean success) {
    }
}
