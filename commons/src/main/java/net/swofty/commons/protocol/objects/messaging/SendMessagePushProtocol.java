package net.swofty.commons.protocol.objects.messaging;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class SendMessagePushProtocol
        extends ServicePushProtocol<SendMessagePushProtocol.Request, SendMessagePushProtocol.Response> {

    public SendMessagePushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(UUID playerUUID, String message) {}

    public record Response(boolean success, @Nullable String error) {}
}
