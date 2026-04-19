package net.swofty.commons.protocol.objects.data;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.UUID;

public class UpdatePlayerDataPushProtocol
        extends ServicePushProtocol<UpdatePlayerDataPushProtocol.Request, UpdatePlayerDataPushProtocol.Response> {

    public UpdatePlayerDataPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(UUID playerUUID, String dataKey, String newData) {}

    public record Response(boolean success, String error, long timestamp) {
        public static Response failure(String error) {
            return new Response(false, error, 0);
        }

        public static Response success(long timestamp) {
            return new Response(true, null, timestamp);
        }
    }
}
