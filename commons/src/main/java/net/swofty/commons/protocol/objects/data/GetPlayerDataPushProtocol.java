package net.swofty.commons.protocol.objects.data;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.UUID;

public class GetPlayerDataPushProtocol
        extends ServicePushProtocol<GetPlayerDataPushProtocol.Request, GetPlayerDataPushProtocol.Response> {

    public GetPlayerDataPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(UUID playerUUID, String dataKey) {}

    public record Response(boolean success, String error, String data, long timestamp) {
        public static Response failure(String error) {
            return new Response(false, error, null, 0);
        }

        public static Response success(String data, long timestamp) {
            return new Response(true, null, data, timestamp);
        }
    }
}
