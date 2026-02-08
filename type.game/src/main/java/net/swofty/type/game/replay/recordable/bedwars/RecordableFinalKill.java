package net.swofty.type.game.replay.recordable.bedwars;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;
import net.swofty.type.game.replay.recordable.AbstractRecordable;
import net.swofty.type.game.replay.recordable.RecordableType;

import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecordableFinalKill extends AbstractRecordable {
    private int victimEntityId;
    private UUID victimUuid;
    private int killerEntityId; // nullable as -1
    private UUID killerUuid; // nullable
    private byte victimTeamId;
    private byte deathCause; // 0=player, 1=void, 2=fall, 3=fire, 4=explosion, 5=other - Maybe could be better?

    public RecordableFinalKill(int victimEntityId, UUID victimUuid, int killerEntityId, UUID killerUuid, byte victimTeamId, byte deathCause) {
        this.victimEntityId = victimEntityId;
        this.victimUuid = victimUuid;
        this.killerEntityId = killerEntityId;
        this.killerUuid = killerUuid;
        this.victimTeamId = victimTeamId;
        this.deathCause = deathCause;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BEDWARS_FINAL_KILL;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(victimEntityId);
        writer.writeUUID(victimUuid);
        writer.writeVarInt(killerEntityId);
        boolean hasKiller = killerUuid != null;
        writer.writeBoolean(hasKiller);
        if (hasKiller) {
            writer.writeUUID(killerUuid);
        }
        writer.writeByte(victimTeamId);
        writer.writeByte(deathCause);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        victimEntityId = reader.readVarInt();
        victimUuid = reader.readUUID();
        killerEntityId = reader.readVarInt();
        if (reader.readBoolean()) {
            killerUuid = reader.readUUID();
        }
        victimTeamId = (byte) reader.readByte();
        deathCause = (byte) reader.readByte();
    }

    @Override
    public int estimatedSize() {
        return 2 + 16 + 2 + 1 + (killerUuid != null ? 16 : 0) + 2; // ~23-39 bytes
    }
}
