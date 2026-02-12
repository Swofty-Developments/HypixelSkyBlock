package net.swofty.type.game.replay.recordable.bedwars;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;
import net.swofty.type.game.replay.recordable.AbstractRecordable;
import net.swofty.type.game.replay.recordable.RecordableType;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class RecordableTeamElimination extends AbstractRecordable {
    private byte teamId;

    public RecordableTeamElimination(byte teamId) {
        this.teamId = teamId;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BEDWARS_TEAM_ELIMINATION;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeByte(teamId);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        teamId = (byte) reader.readByte();
    }

}
