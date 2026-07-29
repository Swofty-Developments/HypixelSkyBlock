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
public class RecordableEntityEquipment extends AbstractRecordable {
    private int entityId;
    private int slotId;
    private byte[] itemBytes; // Serialized item

    public RecordableEntityEquipment(int entityId, int slotId, byte[] itemBytes) {
        this.entityId = entityId;
        this.slotId = slotId;
        this.itemBytes = itemBytes;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_EQUIPMENT;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeByte(slotId);
        writer.writeBytes(itemBytes != null ? itemBytes : new byte[0]);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        slotId = reader.readUnsignedByte();
        itemBytes = reader.readBytes();
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public boolean isEntityState() {
        return true;
    }

}
