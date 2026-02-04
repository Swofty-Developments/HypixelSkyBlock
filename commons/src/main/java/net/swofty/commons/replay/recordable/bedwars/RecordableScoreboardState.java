package net.swofty.commons.replay.recordable.bedwars;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;
import net.swofty.commons.replay.recordable.AbstractRecordable;
import net.swofty.commons.replay.recordable.RecordableType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Records periodic snapshots of the BedWars scoreboard state.
 * Used for accurate scoreboard reconstruction during replay seeking.
 */
@Getter
@Setter
@NoArgsConstructor
public class RecordableScoreboardState extends AbstractRecordable {
    private String nextEventName;
    private int nextEventSeconds;
    private List<TeamScoreboardState> teamStates;

    public RecordableScoreboardState(String nextEventName, int nextEventSeconds,
                                      List<TeamScoreboardState> teamStates) {
        this.nextEventName = nextEventName;
        this.nextEventSeconds = nextEventSeconds;
        this.teamStates = teamStates != null ? new ArrayList<>(teamStates) : new ArrayList<>();
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BEDWARS_SCOREBOARD_STATE;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeString(nextEventName);
        writer.writeVarInt(nextEventSeconds);
        writer.writeVarInt(teamStates.size());
        for (TeamScoreboardState state : teamStates) {
            writer.writeString(state.teamId);
            writer.writeBoolean(state.bedAlive);
            writer.writeVarInt(state.alivePlayers);
        }
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        nextEventName = reader.readString();
        nextEventSeconds = reader.readVarInt();
        int count = reader.readVarInt();
        teamStates = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String teamId = reader.readString();
            boolean bedAlive = reader.readBoolean();
            int alivePlayers = reader.readVarInt();
            teamStates.add(new TeamScoreboardState(teamId, bedAlive, alivePlayers));
        }
    }

    @Override
    public int estimatedSize() {
        int size = 4 + nextEventName.length() + 4 + 2;
        for (TeamScoreboardState state : teamStates) {
            size += 2 + state.teamId.length() + 1 + 2;
        }
        return size;
    }

    /**
     * Represents the scoreboard state for a single team.
     */
    public record TeamScoreboardState(
        String teamId,
        boolean bedAlive,
        int alivePlayers
    ) {}
}
