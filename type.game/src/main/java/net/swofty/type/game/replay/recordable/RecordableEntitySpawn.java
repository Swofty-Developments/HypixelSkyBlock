package net.swofty.type.game.replay.recordable;

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
public class RecordableEntitySpawn extends AbstractRecordable {
    private int entityId;
    private UUID entityUuid;
    private int entityTypeId;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private byte[] initialMetadata; // Optional metadata

    public RecordableEntitySpawn(int entityId, UUID entityUuid, int entityTypeId,
                                  double x, double y, double z, float yaw, float pitch) {
        this.entityId = entityId;
        this.entityUuid = entityUuid;
        this.entityTypeId = entityTypeId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_SPAWN;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeUUID(entityUuid);
        writer.writeVarInt(entityTypeId);
        writer.writeLocation(x, y, z, yaw, pitch);

        boolean hasMetadata = initialMetadata != null && initialMetadata.length > 0;
        writer.writeBoolean(hasMetadata);
        if (hasMetadata) {
            writer.writeBytes(initialMetadata);
        }
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        entityUuid = reader.readUUID();
        entityTypeId = reader.readVarInt();

        double[] loc = reader.readLocation();
        x = loc[0];
        y = loc[1];
        z = loc[2];
        yaw = (float) loc[3];
        pitch = (float) loc[4];

        if (reader.readBoolean()) {
            initialMetadata = reader.readBytes();
        }
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

}
