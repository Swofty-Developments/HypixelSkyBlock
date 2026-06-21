package net.swofty.commons.protocol.objects.guild;

import net.swofty.commons.protocol.RedisProtocol;

import java.util.List;
import java.util.UUID;

public class GuildEventPushProtocol
    extends RedisProtocol<GuildEventPushProtocol.Request, GuildEventPushProtocol.Response> {

    public GuildEventPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(String eventType, String eventData, List<UUID> participants) {
    }

    public record Response(boolean success, List<UUID> playersHandled, String error) {
        public static Response success(List<UUID> playersHandled) {
            return new Response(true, playersHandled, null);
        }

        public static Response failure(String error) {
            return new Response(false, List.of(), error);
        }
    }
}
