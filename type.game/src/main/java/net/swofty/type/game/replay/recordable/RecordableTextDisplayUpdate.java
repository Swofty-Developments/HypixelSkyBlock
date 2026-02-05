package net.swofty.type.game.replay.recordable;

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
public class RecordableTextDisplayUpdate extends AbstractRecordable {
    private int entityId;
    private List<String> newTextLines;
    private boolean replaceAll; // true = replace all lines, false = update specific lines
    private int startLineIndex; // if replaceAll is false, starting index for updates

    public RecordableTextDisplayUpdate(int entityId, List<String> newTextLines,
                                        boolean replaceAll, int startLineIndex) {
        this.entityId = entityId;
        this.newTextLines = newTextLines != null ? new ArrayList<>(newTextLines) : new ArrayList<>();
        this.replaceAll = replaceAll;
        this.startLineIndex = startLineIndex;
    }

    /**
     * Convenience constructor for replacing all lines.
     */
    public RecordableTextDisplayUpdate(int entityId, List<String> newTextLines) {
        this(entityId, newTextLines, true, 0);
    }

    @Override
    public RecordableType getType() {
        return RecordableType.TEXT_DISPLAY_UPDATE;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeVarInt(entityId);
        writer.writeVarInt(newTextLines.size());
        for (String line : newTextLines) {
            writer.writeString(line);
        }
        writer.writeBoolean(replaceAll);
        if (!replaceAll) {
            writer.writeVarInt(startLineIndex);
        }
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        entityId = reader.readVarInt();
        int lineCount = reader.readVarInt();
        newTextLines = new ArrayList<>(lineCount);
        for (int i = 0; i < lineCount; i++) {
            newTextLines.add(reader.readString());
        }
        replaceAll = reader.readBoolean();
        if (!replaceAll) {
            startLineIndex = reader.readVarInt();
        }
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
        int size = 2 + 2 + 1;
        for (String line : newTextLines) {
            size += 2 + line.length();
        }
        if (!replaceAll) {
            size += 2;
        }
        return size;
    }
}
