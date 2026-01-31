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
public class RecordableBlockChange extends AbstractRecordable {
    private int x;
    private int y;
    private int z;
    private int blockStateId;
    private int previousBlockStateId;

    public RecordableBlockChange(int x, int y, int z, int blockStateId, int previousBlockStateId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockStateId = blockStateId;
        this.previousBlockStateId = previousBlockStateId;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BLOCK_CHANGE;
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
