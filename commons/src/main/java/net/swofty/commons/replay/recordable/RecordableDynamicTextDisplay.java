package net.swofty.commons.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecordableDynamicTextDisplay extends AbstractRecordable {
    private int entityId;
    private UUID entityUuid;
    private double x;
    private double y;
    private double z;
    private List<String> textLines;
    private String displayType; // "generator", "hologram", "scoreboard_line"
    private String displayIdentifier;

    public RecordableDynamicTextDisplay(int entityId, UUID entityUuid,
                                         double x, double y, double z,
                                         List<String> textLines,
                                         String displayType, String displayIdentifier) {
        this.entityId = entityId;
        this.entityUuid = entityUuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.textLines = textLines != null ? new ArrayList<>(textLines) : new ArrayList<>();
        this.displayType = displayType != null ? displayType : "";
        this.displayIdentifier = displayIdentifier != null ? displayIdentifier : "";
    }

    @Override
    public RecordableType getType() {
        return RecordableType.DYNAMIC_TEXT_DISPLAY;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeUUID(entityUuid);
        writer.writeLocation(x, y, z, 0, 0);
        writer.writeVarInt(textLines.size());
        for (String line : textLines) {
            writer.writeString(line);
        }
        writer.writeString(displayType);
        writer.writeString(displayIdentifier);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        entityUuid = reader.readUUID();
        double[] loc = reader.readLocation();
        x = loc[0];
        y = loc[1];
        z = loc[2];
        int lineCount = reader.readVarInt();
        textLines = new ArrayList<>(lineCount);
        for (int i = 0; i < lineCount; i++) {
            textLines.add(reader.readString());
        }
        displayType = reader.readString();
        displayIdentifier = reader.readString();
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
        int size = 2 + 16 + 24 + 2;
        for (String line : textLines) {
            size += 2 + line.length();
        }
        size += 2 + displayType.length() + 2 + displayIdentifier.length();
        return size;
    }
}
