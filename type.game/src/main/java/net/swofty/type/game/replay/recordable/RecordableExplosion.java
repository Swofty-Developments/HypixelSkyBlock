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
public class RecordableExplosion extends AbstractRecordable {
	private double x, y, z;
	private float strength;
	private List<int[]> affectedBlocks = new ArrayList<>();
	private float playerMotionX, playerMotionY, playerMotionZ;

	public RecordableExplosion(double x, double y, double z, float strength) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.strength = strength;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.EXPLOSION;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeLocation(x, y, z, 0, 0);
		writer.writeHalfFloat(strength);
		writer.writeVarInt(affectedBlocks.size());
		for (int[] block : affectedBlocks) {
			writer.writeByte(block[0]); // Relative offsets fit in bytes
			writer.writeByte(block[1]);
			writer.writeByte(block[2]);
		}
		writer.writeHalfFloat(playerMotionX);
		writer.writeHalfFloat(playerMotionY);
		writer.writeHalfFloat(playerMotionZ);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		double[] loc = reader.readLocation();
		x = loc[0];
		y = loc[1];
		z = loc[2];
		strength = reader.readHalfFloat();
		int count = reader.readVarInt();
		affectedBlocks = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			affectedBlocks.add(new int[]{reader.readByte(), reader.readByte(), reader.readByte()});
		}
		playerMotionX = reader.readHalfFloat();
		playerMotionY = reader.readHalfFloat();
		playerMotionZ = reader.readHalfFloat();
	}
}
