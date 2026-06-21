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
public class RecordablePlayerDeath extends AbstractRecordable {
	private int entityId;
	private int killerId; // -1 if no killer
	private String deathMessage;

	public RecordablePlayerDeath(int entityId, int killerId, String deathMessage) {
		this.entityId = entityId;
		this.killerId = killerId;
		this.deathMessage = deathMessage;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_DEATH;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeSignedVarInt(killerId);
		writer.writeString(deathMessage != null ? deathMessage : "");
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		killerId = reader.readSignedVarInt();
		deathMessage = reader.readString();
		if (deathMessage.isEmpty()) deathMessage = null;
	}

	@Override
	public int getEntityId() {
		return entityId;
	}
}
