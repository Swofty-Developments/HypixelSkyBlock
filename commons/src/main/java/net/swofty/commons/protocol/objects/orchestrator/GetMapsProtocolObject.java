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
    private static final Serializer<GetMapsMessage> SERIALIZER =
            new JacksonSerializer<>(GetMapsMessage.class);
    private static final Serializer<GetMapsResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetMapsResponse.class);

    @Override

    public Serializer<GetMapsMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetMapsResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetMapsMessage(ServerType type, String mode) { }

    public record GetMapsResponse(List<String> maps, boolean success, @Nullable String error) { }
}
