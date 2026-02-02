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
public class RecordablePlayerDisplayName extends AbstractRecordable {
    private int entityId;
    private String displayName;
    private String prefix;
    private String suffix;
    private int nameColor;

    public RecordablePlayerDisplayName(int entityId, String displayName, String prefix, String suffix, int nameColor) {
        this.entityId = entityId;
        this.displayName = displayName;
        this.prefix = prefix != null ? prefix : "";
        this.suffix = suffix != null ? suffix : "";
        this.nameColor = nameColor;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.PLAYER_DISPLAY_NAME;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeString(displayName);
        writer.writeString(prefix);
        writer.writeString(suffix);
        writer.writeInt(nameColor);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        displayName = reader.readString();
        prefix = reader.readString();
        suffix = reader.readString();
        nameColor = reader.readInt();
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
        return 2 + 4 + displayName.length() + 4 + prefix.length() + 4 + suffix.length() + 4;
    }
}
