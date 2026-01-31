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
public class RecordableEntityEffectRemove extends AbstractRecordable {
    private int entityId;
    private int effectId;

    public RecordableEntityEffectRemove(int entityId, int effectId) {
        this.entityId = entityId;
        this.effectId = effectId;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_EFFECT_REMOVE;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeVarInt(effectId);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        effectId = reader.readVarInt();
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public int estimatedSize() {
        return 8;
    }
}
