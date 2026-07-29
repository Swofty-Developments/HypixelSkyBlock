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
public class RecordablePlayerGamemode extends AbstractRecordable {
	private int entityId;
	private byte gamemode; // 0=survival, 1=creative, 2=adventure, 3=spectator

	public RecordablePlayerGamemode(int entityId, byte gamemode) {
		this.entityId = entityId;
		this.gamemode = gamemode;
	}

	@Override
	public RecordableType getType() {
		return RecordableType.PLAYER_GAMEMODE;
	}

	@Override
	public void write(ReplayDataWriter writer) throws IOException {
		writer.writeVarInt(entityId);
		writer.writeByte(gamemode);
	}

	@Override
	public void read(ReplayDataReader reader) throws IOException {
		entityId = reader.readVarInt();
		gamemode = (byte) reader.readByte();
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
