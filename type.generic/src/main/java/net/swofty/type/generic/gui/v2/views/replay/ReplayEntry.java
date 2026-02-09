package net.swofty.type.generic.gui.v2.views.replay;

import lombok.Builder;
import lombok.With;
import net.swofty.commons.ServerType;

import java.util.Map;
import java.util.UUID;

// this has to be the 5th Replay data record in this project. I'm a bad developer
@Builder
@With
public record ReplayEntry(
        UUID replayId,
        String gameId,
        ServerType serverType,
        String serverId,
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
        return switch (serverType) {
            case BEDWARS_GAME -> "Bed Wars";
            case MURDER_MYSTERY_GAME -> "Murder Mystery";
            case SKYWARS_GAME -> "SkyWars";
            default -> serverType.formatName();
        };
    }
}
