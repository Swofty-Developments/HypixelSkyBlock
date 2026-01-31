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
public class RecordableEntityAnimation extends AbstractRecordable {
    private int entityId;
    private AnimationType animation;

    public RecordableEntityAnimation(int entityId, AnimationType animation) {
        this.entityId = entityId;
        this.animation = animation;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_ANIMATION;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeByte(animation.ordinal());
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        animation = AnimationType.values()[reader.readUnsignedByte()];
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public int estimatedSize() {
        return 5;
    }

    public enum AnimationType {
        SWING_MAIN_HAND,
        TAKE_DAMAGE,
        LEAVE_BED,
        SWING_OFFHAND,
        CRITICAL_EFFECT,
        MAGIC_CRITICAL_EFFECT
    }
}
