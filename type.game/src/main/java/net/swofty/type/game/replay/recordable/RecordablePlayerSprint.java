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
public class RecordablePlayerSprint extends AbstractRecordable {
	private int entityId;
	private boolean sprinting;

	public RecordablePlayerSprint(int entityId, boolean sprinting) {
		this.entityId = entityId;
		this.sprinting = sprinting;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_SPRINT;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeBoolean(sprinting);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		sprinting = reader.readBoolean();
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
