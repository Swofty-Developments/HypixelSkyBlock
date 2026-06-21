package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.game.game.GameObject;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GameHeartbeatProtocol extends RedisProtocol
        <GameHeartbeatProtocol.HeartbeatMessage,
                GameHeartbeatProtocol.HeartbeatResponse> {
    private static final Serializer<HeartbeatMessage> SERIALIZER =
            new JacksonSerializer<>(HeartbeatMessage.class);
    private static final Serializer<HeartbeatResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(HeartbeatResponse.class);

    @Override

    public Serializer<HeartbeatMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<HeartbeatResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record HeartbeatMessage(
        UUID uuid,
        String shortName,
        ServerType type,
        int maxPlayers,
        int onlinePlayers,
        List<GameObject> games,
        List<MapAdvertisement> mapAdvertisements,
        Integer remainingGameSlots
    ) {
        public HeartbeatMessage {
            Objects.requireNonNull(uuid, "uuid");
            Objects.requireNonNull(shortName, "shortName");
            Objects.requireNonNull(type, "type");
            games = games != null ? List.copyOf(games) : List.of();
            mapAdvertisements = mapAdvertisements != null ? List.copyOf(mapAdvertisements) : List.of();
        }

        public HeartbeatMessage(UUID uuid, String shortName, ServerType type, int maxPlayers, int onlinePlayers, List<GameObject> games) {
            this(uuid, shortName, type, maxPlayers, onlinePlayers, games, List.of(), null);
        }
    }

    public record MapAdvertisement(String mapId, String mapName, List<String> supportedModes) {
        public MapAdvertisement {
            Objects.requireNonNull(mapId, "mapId");
            Objects.requireNonNull(mapName, "mapName");
            supportedModes = supportedModes != null ? List.copyOf(supportedModes) : List.of();
        }
    }

    public record HeartbeatResponse(boolean success, @Nullable String error) { }
}
