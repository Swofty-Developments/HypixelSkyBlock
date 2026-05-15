package net.swofty.commons.protocol.objects.game;

import net.swofty.commons.protocol.ServicePushProtocol;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ViewReplayPushProtocol
        extends ServicePushProtocol<ViewReplayPushProtocol.Request, ViewReplayPushProtocol.Response> {

    public ViewReplayPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(UUID uuid, String replayId, @Nullable String shareCode) {}

    public record Response(boolean success) {}
}
