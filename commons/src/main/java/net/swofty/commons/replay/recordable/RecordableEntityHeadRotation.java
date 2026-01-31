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
public class RecordableEntityHeadRotation extends AbstractRecordable {
    private int entityId;
    private float headYaw;

    public RecordableEntityHeadRotation(int entityId, float headYaw) {
        this.entityId = entityId;
        this.headYaw = headYaw;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_HEAD_ROTATION;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        // Store as angle byte (0-255 = 0-360 degrees)
        writer.writeByte((int) (headYaw / 360.0f * 256.0f));
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        headYaw = reader.readUnsignedByte() / 256.0f * 360.0f;
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
