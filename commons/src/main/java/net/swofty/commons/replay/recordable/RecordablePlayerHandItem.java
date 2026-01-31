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
public class RecordablePlayerHandItem extends AbstractRecordable {
	private int entityId;
	private byte slot; // 0-8 hotbar slot

	public RecordablePlayerHandItem(int entityId, byte slot) {
		this.entityId = entityId;
		this.slot = slot;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_HAND_ITEM;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeByte(slot);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		slot = (byte) reader.readByte();
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	@Override
	public boolean isEntityState() {
		return true;
	}
}
