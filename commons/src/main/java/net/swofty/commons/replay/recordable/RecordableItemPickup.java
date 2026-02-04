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
public class RecordableItemPickup extends AbstractRecordable {
    private int itemEntityId;
    private int collectorEntityId;

    public RecordableItemPickup(int itemEntityId, int collectorEntityId) {
        this.itemEntityId = itemEntityId;
        this.collectorEntityId = collectorEntityId;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ITEM_PICKUP;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(itemEntityId);
        writer.writeVarInt(collectorEntityId);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        itemEntityId = reader.readVarInt();
        collectorEntityId = reader.readVarInt();
    }

    @Override
    public int getEntityId() {
        return itemEntityId;
    }

    @Override
    public int estimatedSize() {
        return 4;
    }
}
