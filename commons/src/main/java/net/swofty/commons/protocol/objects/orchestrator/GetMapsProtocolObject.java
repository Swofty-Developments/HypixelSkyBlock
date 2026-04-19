package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class GetMapsProtocolObject extends ProtocolObject
        <GetMapsProtocolObject.GetMapsMessage,
                GetMapsProtocolObject.GetMapsResponse> {

    @Override
    public Serializer<GetMapsMessage> getSerializer() {
        return new JacksonSerializer<>(GetMapsMessage.class);
    }

    @Override
    public Serializer<GetMapsResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetMapsResponse.class);
    }

    public record GetMapsMessage(ServerType type, String mode) { }

    public record GetMapsResponse(List<String> maps, boolean success, @Nullable String error) { }
}
