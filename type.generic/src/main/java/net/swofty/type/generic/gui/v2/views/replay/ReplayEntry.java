package net.swofty.type.generic.gui.v2.views.replay;

import lombok.Builder;
import lombok.With;
import net.swofty.commons.ServerType;

import java.util.Map;
import java.util.UUID;

@Builder
@With
public record ReplayEntry(
        UUID replayId,
        String gameId,
        ServerType serverType,
        String gameTypeName,
        String mapName,
        long startTime,
        long endTime,
        int durationTicks,
        Map<UUID, String> players,
        String winnerId,
        String winnerType,
        long dataSize
) {
    public int durationSeconds() {
        return durationTicks / 20;
    }

    public String formattedDuration() {
        int seconds = durationSeconds();
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }

    public String displayName() {
        String serverName = switch (serverType) {
            case BEDWARS_GAME -> "BedWars";
            case MURDER_MYSTERY_GAME -> "Murder Mystery";
            case SKYWARS_GAME -> "SkyWars";
            default -> serverType.formatName();
        };
        return serverName + " - " + gameTypeName;
    }
}
