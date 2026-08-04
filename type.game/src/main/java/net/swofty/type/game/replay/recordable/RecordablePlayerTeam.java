package net.swofty.type.game.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class RecordablePlayerTeam extends AbstractRecordable {
    private int entityId;
    private UUID playerUuid;
    private String teamId;

    public RecordablePlayerTeam(int entityId, UUID playerUuid, String teamId) {
        this.entityId = entityId;
        this.playerUuid = playerUuid;
        this.teamId = teamId;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.PLAYER_TEAM;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeUUID(playerUuid);
        writer.writeString(teamId);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        playerUuid = reader.readUUID();
        teamId = reader.readString();
    }

    @Override
    public boolean isEntityState() {
        return true;
    }
}
