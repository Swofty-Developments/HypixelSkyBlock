package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PartyEventPushProtocol
        extends ServicePushProtocol<PartyEventPushProtocol.Request, PartyEventPushProtocol.Response> {

    public PartyEventPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(String eventType, String eventData, List<UUID> participants) {}

    public record Response(boolean success, int playersHandled, List<UUID> playersHandledUUIDs, String error,
                           boolean blocked, String blockReason, Map<UUID, String> rejectedPlayers) {
        public static Response failure(String reason) {
            return new Response(false, 0, List.of(), reason, false, null, Map.of());
        }

        public static Response success(int playersHandled, List<UUID> playersHandledUUIDs) {
            return new Response(true, playersHandled, playersHandledUUIDs, null, false, null, Map.of());
        }

        public static Response blocked(String blockReason) {
            return new Response(false, 0, List.of(), null, true, blockReason, Map.of());
        }

        public static Response gameWarp(List<UUID> accepted, Map<UUID, String> rejected) {
            return new Response(true, accepted.size(), accepted, null, false, null, rejected);
        }
    }
}
