package net.swofty.type.game.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecordableBatch extends AbstractRecordable {
    private List<Recordable> recordables = new ArrayList<>();

    public RecordableBatch(List<Recordable> recordables) {
        this.recordables = recordables;
    }

    public void add(Recordable recordable) {
        recordables.add(recordable);
    }

    @Override
    public RecordableType getType() {
        return RecordableType.BATCH;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(recordables.size());
        for (Recordable recordable : recordables) {
            writer.writeVarInt(recordable.getTick());
            writer.writeByte(recordable.getType().getId());
            recordable.write(writer);
        }
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        int count = reader.readVarInt();
        recordables = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int tick = reader.readVarInt();
            int typeId = reader.readUnsignedByte();
            RecordableType type = RecordableType.byId(typeId);
            if (type == null) {
                throw new IOException("Unknown recordable type: " + typeId);
            }
            Recordable recordable = type.createAndRead(reader);
            recordable.setTick(tick);
            recordables.add(recordable);
        }
    }

    @Override
    public int estimatedSize() {
        int size = 4;
        for (Recordable r : recordables) {
            size += 6 + r.estimatedSize();
        }
        return size;
    }
}
