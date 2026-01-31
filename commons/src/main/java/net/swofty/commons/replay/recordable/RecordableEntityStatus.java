package net.swofty.commons.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class RecordableEntityStatus extends AbstractRecordable {
    private int entityId;
    private byte status;

    public RecordableEntityStatus(int entityId, byte status) {
        this.entityId = entityId;
        this.status = status;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_STATUS;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeByte(status);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        status = (byte) reader.readByte();
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public int estimatedSize() {
        return 5;
    }
}
