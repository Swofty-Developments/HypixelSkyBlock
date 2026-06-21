package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.party.PartyBroadcast;
import net.swofty.commons.protocol.RedisProtocol;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PartyBroadcastPushProtocol
        extends RedisProtocol<PartyBroadcastPushProtocol.Request, PartyBroadcastPushProtocol.Response> {

    public PartyBroadcastPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(PartyBroadcast broadcast) {}

    public record Response(boolean success,
                           List<UUID> playersHandled,
                           String error,
                           Map<UUID, String> rejectedPlayers) {
        public static Response success(List<UUID> playersHandled) {
            return new Response(true, playersHandled, null, Map.of());
        }

        public static Response success(List<UUID> playersHandled, Map<UUID, String> rejectedPlayers) {
            return new Response(true, playersHandled, null, rejectedPlayers);
        }

        public static Response failure(String error) {
            return new Response(false, List.of(), error, Map.of());
        }
    }
}
