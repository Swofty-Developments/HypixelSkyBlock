package net.swofty.commons.protocol.objects.game;

import net.swofty.commons.protocol.ServicePushProtocol;

public class InstantiateGamePushProtocol
        extends ServicePushProtocol<InstantiateGamePushProtocol.Request, InstantiateGamePushProtocol.Response> {

    public InstantiateGamePushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(String gameType, String map) {}

    public record Response(boolean success, String gameId, String map, String gameType, String error) {
        public static Response success(String gameId, String map, String gameType) {
            return new Response(true, gameId, map, gameType, null);
        }

        public static Response failure(String reason) {
            return new Response(false, null, null, null, reason);
        }
    }
}
