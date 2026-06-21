package net.swofty.type.game.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecordablePlayerSkin extends AbstractRecordable {
    private int entityId;
    private UUID playerUuid;
    private String textureValue; // Base64 encoded skin texture
    private String textureSignature; // Mojang signature (can be empty)

    public RecordablePlayerSkin(int entityId, UUID playerUuid, String textureValue, String textureSignature) {
        this.entityId = entityId;
        this.playerUuid = playerUuid;
        this.textureValue = textureValue;
        this.textureSignature = textureSignature != null ? textureSignature : "";
    }

    @Override
    public RecordableType getType() {
        return RecordableType.PLAYER_SKIN;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeUUID(playerUuid);
        writer.writeString(textureValue);
        writer.writeString(textureSignature);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        playerUuid = reader.readUUID();
        textureValue = reader.readString();
        textureSignature = reader.readString();
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
