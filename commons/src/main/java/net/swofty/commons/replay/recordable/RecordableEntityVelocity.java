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
public class RecordableEntityVelocity extends AbstractRecordable {
	private int entityId;
	private double velocityX;
	private double velocityY;
	private double velocityZ;

	public RecordableEntityVelocity(int entityId, double velocityX, double velocityY, double velocityZ) {
		this.entityId = entityId;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.ENTITY_VELOCITY;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeShort((int) (velocityX * 8000));
		writer.writeShort((int) (velocityY * 8000));
		writer.writeShort((int) (velocityZ * 8000));
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		velocityX = reader.readShort() / 8000.0;
		velocityY = reader.readShort() / 8000.0;
		velocityZ = reader.readShort() / 8000.0;
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	@Override
	public int estimatedSize() {
		return 10;
	}
}
