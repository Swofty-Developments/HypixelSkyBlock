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
// tbh we only need the entityId, nothing else. As this will only be used to track the animation
public class RecordablePlayerBlockChange extends AbstractRecordable {
    private int entityId;
    private int x;
    private int y;
    private int z;
    private int blockStateId;
    private int previousBlockStateId;

    public RecordablePlayerBlockChange(int entityId, int x, int y, int z, int blockStateId, int previousBlockStateId) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockStateId = blockStateId;
        this.previousBlockStateId = previousBlockStateId;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.PLAYER_BLOCK_CHANGE;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeBlockCoords(x, y, z);
        writer.writeVarInt(blockStateId);
        writer.writeVarInt(previousBlockStateId);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        int[] coords = reader.readBlockCoords();
        x = coords[0];
        y = coords[1];
        z = coords[2];
        blockStateId = reader.readVarInt();
        previousBlockStateId = reader.readVarInt();
    }

    @Override
    public int estimatedSize() {
        return 12; // 6 bytes coords + 2x VarInt
    }
}
