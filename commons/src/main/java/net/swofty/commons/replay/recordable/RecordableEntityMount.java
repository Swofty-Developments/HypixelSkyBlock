package net.swofty.commons.replay.recordable;

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
public class RecordableEntityMount extends AbstractRecordable {
    private int vehicleId;
    private List<Integer> passengerIds = new ArrayList<>();

    public RecordableEntityMount(int vehicleId, List<Integer> passengerIds) {
        this.vehicleId = vehicleId;
        this.passengerIds = passengerIds;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.ENTITY_MOUNT;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(vehicleId);
        writer.writeVarInt(passengerIds.size());
        for (int passengerId : passengerIds) {
            writer.writeVarInt(passengerId);
        }
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        vehicleId = reader.readVarInt();
        int count = reader.readVarInt();
        passengerIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            passengerIds.add(reader.readVarInt());
        }
    }

    @Override
    public int getEntityId() {
        return vehicleId;
    }

    @Override
    public boolean isEntityState() {
        return true;
    }
}
