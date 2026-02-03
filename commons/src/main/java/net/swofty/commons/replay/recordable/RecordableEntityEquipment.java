package net.swofty.commons.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.ByteArrayOutputStream;
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
        slotId = reader.readByte();
        itemBytes = reader.readBytes();
    }

    public static byte[] itemToNBTBytes(ItemStack item) throws IOException {
        CompoundBinaryTag nbt = item.toItemNBT();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryTagIO.writer().writeNameless(nbt, out);

        return out.toByteArray();
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

}
