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
public class RecordableBlockBreakAnimation extends AbstractRecordable {
    private int entityId;
    private int x;
    private int y;
    private int z;
    private byte stage; // 0-9, or -1 to stop

    public RecordableBlockBreakAnimation(int entityId, int x, int y, int z, byte stage) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.stage = stage;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BLOCK_BREAK_ANIMATION;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeBlockCoords(x, y, z);
        writer.writeByte(stage);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        int[] coords = reader.readBlockCoords();
        x = coords[0];
        y = coords[1];
        z = coords[2];
        stage = (byte) reader.readByte();
    }

    @Override
    public int getEntityId() {
        return entityId;
    }
}
