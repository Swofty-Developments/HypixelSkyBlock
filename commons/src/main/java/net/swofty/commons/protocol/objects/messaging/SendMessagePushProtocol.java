package net.swofty.commons.protocol.objects.messaging;

import net.swofty.commons.protocol.RedisProtocol;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class SendMessagePushProtocol
        extends RedisProtocol<SendMessagePushProtocol.Request, SendMessagePushProtocol.Response> {

    public SendMessagePushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(UUID playerUUID, String message) {}

    public record Response(boolean success, @Nullable String error) {}
}
