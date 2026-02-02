package net.swofty.commons.replay.recordable.bedwars;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;
import net.swofty.commons.replay.recordable.AbstractRecordable;
import net.swofty.commons.replay.recordable.RecordableType;

import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecordableBedDestruction extends AbstractRecordable {
    private byte teamId;
    private int destroyerEntityId; // this is nullable?
    private UUID destroyerUuid;
    private int bedX;
    private int bedY;
    private int bedZ;

    public RecordableBedDestruction(byte teamId, int destroyerEntityId, UUID destroyerUuid, int bedX, int bedY, int bedZ) {
        this.teamId = teamId;
        this.destroyerEntityId = destroyerEntityId;
        this.destroyerUuid = destroyerUuid;
        this.bedX = bedX;
        this.bedY = bedY;
        this.bedZ = bedZ;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BEDWARS_BED_DESTRUCTION;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeByte(teamId);
        writer.writeVarInt(destroyerEntityId);
        boolean hasDestroyer = destroyerUuid != null;
        writer.writeBoolean(hasDestroyer);
        if (hasDestroyer) {
            writer.writeUUID(destroyerUuid);
        }
        writer.writeBlockCoords(bedX, bedY, bedZ);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        teamId = (byte) reader.readByte();
        destroyerEntityId = reader.readVarInt();
        if (reader.readBoolean()) {
            destroyerUuid = reader.readUUID();
        }
        int[] coords = reader.readBlockCoords();
        bedX = coords[0];
        bedY = coords[1];
        bedZ = coords[2];
    }

    @Override
    public int estimatedSize() {
        return 1 + 2 + 1 + (destroyerUuid != null ? 16 : 0) + 6; // ~26-42 bytes
    }
}
