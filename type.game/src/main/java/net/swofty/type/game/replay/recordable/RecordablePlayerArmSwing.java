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
public class RecordablePlayerArmSwing extends AbstractRecordable {
	private int entityId;
	private boolean mainHand;

	public RecordablePlayerArmSwing(int entityId, boolean mainHand) {
		this.entityId = entityId;
		this.mainHand = mainHand;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_ARM_SWING;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeBoolean(mainHand);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		mainHand = reader.readBoolean();
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	@Override
	public int estimatedSize() {
		return 5;
	}
}
