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
public class RecordablePlayerChat extends AbstractRecordable {
	private int entityId;
	private String message;

	public RecordablePlayerChat(int entityId, String message) {
		this.entityId = entityId;
		this.message = message;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_CHAT;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeString(message);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		message = reader.readString();
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	@Override
	public int estimatedSize() {
		return 8 + message.length();
	}
}
