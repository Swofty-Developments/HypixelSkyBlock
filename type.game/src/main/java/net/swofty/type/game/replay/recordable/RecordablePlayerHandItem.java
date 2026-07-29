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
public class RecordablePlayerHandItem extends AbstractRecordable {
	private int entityId;
	private byte[] itemBytes;

	public RecordablePlayerHandItem(int entityId, byte[] itemBytes) {
		this.entityId = entityId;
		this.itemBytes = itemBytes;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_HAND_ITEM;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeBytes(itemBytes != null ? itemBytes : new byte[0]);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		itemBytes = reader.readBytes();
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
