package net.swofty.commons.protocol.objects.data;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.UUID;

public class UnlockPlayerDataPushProtocol
        extends ServicePushProtocol<UnlockPlayerDataPushProtocol.Request, UnlockPlayerDataPushProtocol.Response> {

    public UnlockPlayerDataPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(UUID playerUUID, String dataKey) {}

    public record Response(boolean success, long unlockTime) {
        public static Response success(long unlockTime) {
            return new Response(true, unlockTime);
        }
    }
}
