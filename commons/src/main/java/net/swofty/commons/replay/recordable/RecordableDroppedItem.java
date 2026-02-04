package net.swofty.commons.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecordableDroppedItem extends AbstractRecordable {
    private int entityId;
    private UUID entityUuid;
    private double x;
    private double y;
    private double z;
    private float velocityX;
    private float velocityY;
    private float velocityZ;
    private byte[] itemNbt; // Serialized item data
    private int pickupDelay; // Ticks until item can be picked up
    private int despawnTick; // Tick when item should despawn (0 = no auto-despawn)

    public RecordableDroppedItem(int entityId, UUID entityUuid,
                                  double x, double y, double z,
                                  float velocityX, float velocityY, float velocityZ,
                                  byte[] itemNbt, int pickupDelay, int despawnTick) {
        this.entityId = entityId;
        this.entityUuid = entityUuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.itemNbt = itemNbt;
        this.pickupDelay = pickupDelay;
        this.despawnTick = despawnTick;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.DROPPED_ITEM;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeUUID(entityUuid);
        writer.writeLocation(x, y, z, 0, 0);
        writer.writeFloat(velocityX);
        writer.writeFloat(velocityY);
        writer.writeFloat(velocityZ);
        writer.writeBytes(itemNbt);
        writer.writeVarInt(pickupDelay);
        writer.writeVarInt(despawnTick);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        entityUuid = reader.readUUID();
        double[] loc = reader.readLocation();
        x = loc[0];
        y = loc[1];
        z = loc[2];
        velocityX = reader.readFloat();
        velocityY = reader.readFloat();
        velocityZ = reader.readFloat();
        itemNbt = reader.readBytes();
        pickupDelay = reader.readVarInt();
        despawnTick = reader.readVarInt();
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
        return 2 + 16 + 24 + 12 + (itemNbt != null ? itemNbt.length + 2 : 2) + 4 + 4;
    }
}
