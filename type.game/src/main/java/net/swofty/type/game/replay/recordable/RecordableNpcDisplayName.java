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
public class RecordableNpcDisplayName extends AbstractRecordable {
    private int entityId;
    private String displayName;
    private String prefix;
    private String suffix;
    private int nameColor;
    private boolean visible; // whether the name is visible above the NPC

    public RecordableNpcDisplayName(int entityId, String displayName,
                                    String prefix, String suffix,
                                    int nameColor, boolean visible) {
        this.entityId = entityId;
        this.displayName = displayName != null ? displayName : "";
        this.prefix = prefix != null ? prefix : "";
        this.suffix = suffix != null ? suffix : "";
        this.nameColor = nameColor;
        this.visible = visible;
    }

    @Override
    public RecordableType getType() {
        return RecordableType.NPC_DISPLAY_NAME;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeString(displayName);
        writer.writeString(prefix);
        writer.writeString(suffix);
        writer.writeInt(nameColor);
        writer.writeBoolean(visible);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        displayName = reader.readString();
        prefix = reader.readString();
        suffix = reader.readString();
        nameColor = reader.readInt();
        visible = reader.readBoolean();
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
        return 2 + 4 + displayName.length() + 4 + prefix.length() + 4 + suffix.length() + 4 + 1;
    }
}
