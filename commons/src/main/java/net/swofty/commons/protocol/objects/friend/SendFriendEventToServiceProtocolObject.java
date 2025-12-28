package net.swofty.commons.protocol.objects.friend;

import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class SendFriendEventToServiceProtocolObject extends ProtocolObject
        <SendFriendEventToServiceProtocolObject.SendFriendEventToServiceMessage,
                SendFriendEventToServiceProtocolObject.SendFriendEventToServiceResponse> {

    @Override
    public Serializer<SendFriendEventToServiceMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(SendFriendEventToServiceMessage value) {
                JSONObject json = new JSONObject();
                json.put("event", value.event.getSerializer().serialize(value.event));
                json.put("eventType", value.event.getClass().getSimpleName());
                return json.toString();
            }

            @Override
            public SendFriendEventToServiceMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                String eventType = jsonObject.getString("eventType");
                FriendEvent event = FriendEvent.findFromType(eventType);
                FriendEvent deserializedEvent = (FriendEvent) event.getSerializer().deserialize(jsonObject.getString("event"));
                return new SendFriendEventToServiceMessage(deserializedEvent);
            }

            @Override
            public SendFriendEventToServiceMessage clone(SendFriendEventToServiceMessage value) {
                return new SendFriendEventToServiceMessage(value.event);
            }
        };
    }

    @Override
    public Serializer<SendFriendEventToServiceResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(SendFriendEventToServiceResponse value) {
                return value.success ? "true" : "false";
            }

            @Override
            public SendFriendEventToServiceResponse deserialize(String json) {
                return new SendFriendEventToServiceResponse(json.equals("true"));
            }

            @Override
            public SendFriendEventToServiceResponse clone(SendFriendEventToServiceResponse value) {
                return new SendFriendEventToServiceResponse(value.success);
            }
        };
    }

    public record SendFriendEventToServiceMessage(FriendEvent event) {
    }

    public record SendFriendEventToServiceResponse(boolean success) {
    }
}
