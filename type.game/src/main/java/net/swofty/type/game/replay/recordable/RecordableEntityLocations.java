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
public class RecordableEntityLocations extends AbstractRecordable {
    private List<EntityLocationEntry> entries = new ArrayList<>();

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_LOCATIONS;
    }

    public void addEntry(int entityId, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        entries.add(new EntityLocationEntry(entityId, x, y, z, yaw, pitch, onGround));
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entries.size());
        for (EntityLocationEntry entry : entries) {
            writer.writeVarInt(entry.entityId);
            writer.writeLocation(entry.x, entry.y, entry.z, entry.yaw, entry.pitch);
            writer.writeBoolean(entry.onGround);
        }
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        int count = reader.readVarInt();
        entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int entityId = reader.readVarInt();
            double[] loc = reader.readLocation();
            boolean onGround = reader.readBoolean();
            entries.add(new EntityLocationEntry(
                    entityId, loc[0], loc[1], loc[2], (float) loc[3], (float) loc[4], onGround
            ));
        }
    }

    @Override
    public boolean isEntityState() {
        return true; // Location is an entity state - can skip to latest
    }

    public record EntityLocationEntry(
            int entityId,
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            boolean onGround
    ) {}
}
