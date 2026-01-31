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
public class RecordablePlayerSneak extends AbstractRecordable {
	private int entityId;
	private boolean sneaking;

	public RecordablePlayerSneak(int entityId, boolean sneaking) {
		this.entityId = entityId;
		this.sneaking = sneaking;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_SNEAK;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeBoolean(sneaking);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		sneaking = reader.readBoolean();
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	@Override
	public boolean isEntityState() {
		return true;
	}

	@Override
	public int estimatedSize() {
		return 5;
	}
}
