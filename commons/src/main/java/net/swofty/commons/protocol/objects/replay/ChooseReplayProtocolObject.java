package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChooseReplayProtocolObject extends ProtocolObject
        <ChooseReplayProtocolObject.ChooseReplayMessage,
            ChooseReplayProtocolObject.ChooseReplayResponse> {

    @Override
    public Serializer<ChooseReplayMessage> getSerializer() {
        return new JacksonSerializer<>(ChooseReplayMessage.class);
    }

    @Override
    public Serializer<ChooseReplayResponse> getReturnSerializer() {
        return new JacksonSerializer<>(ChooseReplayResponse.class);
    }

    public record ChooseReplayMessage(UUID player, String replayId, @Nullable String shareCode) {
        public ChooseReplayMessage(UUID player, String replayId) {
            this(player, replayId, null);
        }
    }

    public record ChooseReplayResponse(boolean error) {
    }
}
