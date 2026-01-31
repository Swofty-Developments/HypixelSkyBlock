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
public class RecordableSound extends AbstractRecordable {
	private String soundId; // e.g. "minecraft:entity.player.hurt"
	private byte category; // Sound category
	private double x, y, z;
	private float volume;
	private float pitch;

	public RecordableSound(String soundId, byte category, double x, double y, double z, float volume, float pitch) {
		this.soundId = soundId;
		this.category = category;
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.pitch = pitch;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.SOUND;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeString(soundId);
		writer.writeByte(category);
		writer.writeLocation(x, y, z, 0, 0);
		writer.writeHalfFloat(volume);
		writer.writeHalfFloat(pitch);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		soundId = reader.readString();
		category = (byte) reader.readByte();
		double[] loc = reader.readLocation();
		x = loc[0];
		y = loc[1];
		z = loc[2];
		volume = reader.readHalfFloat();
		pitch = reader.readHalfFloat();
	}
}
