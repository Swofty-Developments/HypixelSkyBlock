package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReplayListProtocolObject extends RedisProtocol<
        ReplayListProtocolObject.ListRequest,
        ReplayListProtocolObject.ListResponse> {

    @Override
    public Serializer<ListRequest> getSerializer() {
        return new JacksonSerializer<>(ListRequest.class);
    }

    @Override
    public Serializer<ListResponse> getReturnSerializer() {
        return new JacksonSerializer<>(ListResponse.class);
    }

    public record ListRequest(UUID playerId, int limit, ServerType filterType) {}

    public record ListResponse(boolean success, List<ReplaySummary> replays) {}

    public record ReplaySummary(
            UUID replayId,
            String gameId,
            ServerType serverType,
            String serverId,
            String gameTypeName,
            String mapName,
            long startTime,
            long endTime,
            int durationTicks,
            int playerCount,
            Map<UUID, String> players,
            String winnerId,
            String winnerType,
            long dataSize
    ) {}
}
