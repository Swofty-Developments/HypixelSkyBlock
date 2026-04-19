package net.swofty.commons.protocol.objects.data;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.UUID;

public class LockPlayerDataPushProtocol
        extends ServicePushProtocol<LockPlayerDataPushProtocol.Request, LockPlayerDataPushProtocol.Response> {

    public LockPlayerDataPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(UUID playerUUID, String dataKey) {}

    public record Response(boolean success, String error, long lockTime) {
        public static Response failure(String error) {
            return new Response(false, error, 0);
        }

        public static Response success(long lockTime) {
            return new Response(true, null, lockTime);
        }
    }
}
