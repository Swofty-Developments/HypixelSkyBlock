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
public class RecordableKill extends AbstractRecordable {
    private int victimEntityId;
    private UUID victimUuid;
    private int killerEntityId; // nullable as -1
    private UUID killerUuid; // nullable
    private byte victimTeamId;
    private byte deathCause; // 0=generic, 1=generic_player, 2=void, 3=void_player, 4=shot, 5=entity
    private byte finalKill;

    public RecordableKill(int victimEntityId, UUID victimUuid, int killerEntityId, UUID killerUuid, byte victimTeamId, byte deathCause, byte finalKill) {
        this.victimEntityId = victimEntityId;
        this.victimUuid = victimUuid;
        this.killerEntityId = killerEntityId;
        this.killerUuid = killerUuid;
        this.victimTeamId = victimTeamId;
        this.deathCause = deathCause;
        this.finalKill = finalKill;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BEDWARS_KILL;
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
        writer.writeByte(finalKill);
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
        finalKill = (byte) reader.readByte();
    }

}
