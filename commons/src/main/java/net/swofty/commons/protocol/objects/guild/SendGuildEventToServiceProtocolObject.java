package net.swofty.commons.protocol.objects.guild;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

public class SendGuildEventToServiceProtocolObject extends RedisProtocol<SendGuildEventToServiceProtocolObject.SendGuildEventToServiceMessage, SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse> {
    @Override
    public Serializer<SendGuildEventToServiceMessage> getSerializer() {
        return new JacksonSerializer<>(SendGuildEventToServiceMessage.class);
    }

    @Override
    public Serializer<SendGuildEventToServiceResponse> getReturnSerializer() {
        return new JacksonSerializer<>(SendGuildEventToServiceResponse.class);
    }

    public record SendGuildEventToServiceMessage(String eventType, String eventData) {
    }

    public record SendGuildEventToServiceResponse(boolean success) {
    }
}
