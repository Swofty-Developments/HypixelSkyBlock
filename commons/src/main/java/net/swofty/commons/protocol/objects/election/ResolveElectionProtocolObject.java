package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class ResolveElectionProtocolObject
        extends ProtocolObject<ResolveElectionProtocolObject.ResolveElectionMessage,
        ResolveElectionProtocolObject.ResolveElectionResponse> {

    @Override
    public Serializer<ResolveElectionMessage> getSerializer() {
        return new JacksonSerializer<>(ResolveElectionMessage.class);
    }

    @Override
    public Serializer<ResolveElectionResponse> getReturnSerializer() {
        return new JacksonSerializer<>(ResolveElectionResponse.class);
    }

    public record ResolveElectionMessage(int year) {}

    public record ResolveElectionResponse(boolean resolved, String serializedData, boolean success, @Nullable String error) {}
}
