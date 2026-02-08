package net.swofty.type.game.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class RecordableEntityEffect extends AbstractRecordable {
    private int entityId;
    private int effectId;
    private byte amplifier;
    private int durationTicks;
    private byte flags; // ambient, particles, icon visibility - bitmask

    public RecordableEntityEffect(int entityId, int effectId, byte amplifier, int durationTicks, byte flags) {
        this.entityId = entityId;
        this.effectId = effectId;
        this.amplifier = amplifier;
        this.durationTicks = durationTicks;
        this.flags = flags;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_EFFECT;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeVarInt(effectId);
        writer.writeByte(amplifier);
        writer.writeVarInt(durationTicks);
        writer.writeByte(flags);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        effectId = reader.readVarInt();
        amplifier = (byte) reader.readByte();
        durationTicks = reader.readVarInt();
        flags = (byte) reader.readByte();
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public int estimatedSize() {
        return 12;
    }
}
