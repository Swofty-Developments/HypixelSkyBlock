package net.swofty.type.game.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class RecordablePlayerRespawn extends AbstractRecordable {
	private int entityId;
	private double x, y, z;
	private float yaw, pitch;

	public RecordablePlayerRespawn(int entityId, double x, double y, double z, float yaw, float pitch) {
		this.entityId = entityId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_RESPAWN;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeLocation(x, y, z, yaw, pitch);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		double[] loc = reader.readLocation();
		x = loc[0];
		y = loc[1];
		z = loc[2];
		yaw = (float) loc[3];
		pitch = (float) loc[4];
	}

	@Override
	public int getEntityId() {
		return entityId;
	}
}
