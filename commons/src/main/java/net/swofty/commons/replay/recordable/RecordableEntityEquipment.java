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
public class RecordableEntityEquipment extends AbstractRecordable {
    private int entityId;
    private EquipmentSlot slot;
    private byte[] itemBytes; // Serialized item

    public RecordableEntityEquipment(int entityId, EquipmentSlot slot, byte[] itemBytes) {
        this.entityId = entityId;
        this.slot = slot;
        this.itemBytes = itemBytes;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_EQUIPMENT;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeByte(slot.ordinal());
        writer.writeBytes(itemBytes != null ? itemBytes : new byte[0]);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        slot = EquipmentSlot.values()[reader.readUnsignedByte()];
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

    @Override
    public int estimatedSize() {
        return 6 + (itemBytes != null ? itemBytes.length : 0);
    }

    public enum EquipmentSlot {
        MAIN_HAND,
        OFF_HAND,
        BOOTS,
        LEGGINGS,
        CHESTPLATE,
        HELMET
    }
}
