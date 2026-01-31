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
public class RecordableEntityMetadata extends AbstractRecordable {
    private int entityId;
    private byte[] metadataBytes;

    public RecordableEntityMetadata(int entityId, byte[] metadataBytes) {
        this.entityId = entityId;
        this.metadataBytes = metadataBytes;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_METADATA;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeBytes(metadataBytes);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        metadataBytes = reader.readBytes();
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public boolean isEntityState() {
        return true;
    }

    @Override
    public int estimatedSize() {
        return 4 + (metadataBytes != null ? metadataBytes.length : 0);
    }
}
