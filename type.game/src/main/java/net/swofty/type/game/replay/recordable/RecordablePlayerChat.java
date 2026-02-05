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
public class RecordablePlayerChat extends AbstractRecordable {
	private int entityId;
	private String message;
	private boolean shout;

	public RecordablePlayerChat(int entityId, String message, boolean shout) {
		this.entityId = entityId;
		this.message = message;
		this.shout = shout;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_CHAT;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeString(message);
		writer.writeBoolean(shout);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		message = reader.readString();
		shout = reader.readBoolean();
	}

	@Override
	public int getEntityId() {
		return entityId;
	}

	@Override
	public int estimatedSize() {
		return 5 + message.length();
	}
}
