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
public class RecordableParticle extends AbstractRecordable {
	private int particleId;
	private boolean longDistance;
	private double x, y, z;
	private float offsetX, offsetY, offsetZ;
	private float speed;
	private int count;
	private byte[] data; // Optional particle data

	public RecordableParticle(int particleId, boolean longDistance, double x, double y, double z,
							  float offsetX, float offsetY, float offsetZ, float speed, int count) {
		this.particleId = particleId;
		this.longDistance = longDistance;
		this.x = x;
		this.y = y;
		this.z = z;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.speed = speed;
		this.count = count;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PARTICLE;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(particleId);
		writer.writeBoolean(longDistance);
		writer.writeLocation(x, y, z, 0, 0);
		writer.writeHalfFloat(offsetX);
		writer.writeHalfFloat(offsetY);
		writer.writeHalfFloat(offsetZ);
		writer.writeHalfFloat(speed);
		writer.writeVarInt(count);
		boolean hasData = data != null && data.length > 0;
		writer.writeBoolean(hasData);
		if (hasData) writer.writeBytes(data);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		particleId = reader.readVarInt();
		longDistance = reader.readBoolean();
		double[] loc = reader.readLocation();
		x = loc[0];
		y = loc[1];
		z = loc[2];
		offsetX = reader.readHalfFloat();
		offsetY = reader.readHalfFloat();
		offsetZ = reader.readHalfFloat();
		speed = reader.readHalfFloat();
		count = reader.readVarInt();
		if (reader.readBoolean()) {
			data = reader.readBytes();
		}
	}
}
