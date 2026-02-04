package net.swofty.commons.replay.recordable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecordableNpcTextLine extends AbstractRecordable {
    private int entityId;
    private List<String> textLines;
    private double yOffset; // vertical offset from NPC position
    private int displayDurationTicks; // how long to show (0 = indefinite)

    public RecordableNpcTextLine(int entityId, List<String> textLines,
                                  double yOffset, int displayDurationTicks) {
        this.entityId = entityId;
        this.textLines = textLines != null ? new ArrayList<>(textLines) : new ArrayList<>();
        this.yOffset = yOffset;
        this.displayDurationTicks = displayDurationTicks;
    }

    /**
     * Convenience constructor for simple single-line text.
     */
    public RecordableNpcTextLine(int entityId, String text, double yOffset) {
        this(entityId, List.of(text), yOffset, 0);
    }

    @Override
    public RecordableType getType() {
        return RecordableType.NPC_TEXT_LINE;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeVarInt(textLines.size());
        for (String line : textLines) {
            writer.writeString(line);
        }
        writer.writeDouble(yOffset);
        writer.writeVarInt(displayDurationTicks);
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        int lineCount = reader.readVarInt();
        textLines = new ArrayList<>(lineCount);
        for (int i = 0; i < lineCount; i++) {
            textLines.add(reader.readString());
        }
        yOffset = reader.readDouble();
        displayDurationTicks = reader.readVarInt();
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
        int size = 2 + 2 + 8 + 2;
        for (String line : textLines) {
            size += 2 + line.length();
        }
        return size;
    }
}
