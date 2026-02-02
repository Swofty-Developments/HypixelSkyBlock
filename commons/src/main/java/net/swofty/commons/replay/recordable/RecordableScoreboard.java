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
// maybe don't do this? I believe we should rather just reconstruct the scoreboard per game based on game data
public class RecordableScoreboard extends AbstractRecordable {
    private String title;
    private List<ScoreboardLine> lines = new ArrayList<>();

    public RecordableScoreboard(String title, List<ScoreboardLine> lines) {
        this.title = title;
        this.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
    }

    @Override
    public RecordableType getType() {
        return RecordableType.SCOREBOARD;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeString(title);
        writer.writeVarInt(lines.size());
        for (ScoreboardLine line : lines) {
            writer.writeString(line.text);
            writer.writeVarInt(line.score);
        }
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        title = reader.readString();
        int lineCount = reader.readVarInt();
        lines = new ArrayList<>(lineCount);
        for (int i = 0; i < lineCount; i++) {
            String text = reader.readString();
            int score = reader.readVarInt();
            lines.add(new ScoreboardLine(text, score));
        }
    }

    @Override
    public int estimatedSize() {
        int size = 4 + title.length() + 2;
        for (ScoreboardLine line : lines) {
            size += 4 + line.text.length() + 2;
        }
        return size;
    }

    public record ScoreboardLine(String text, int score) {
        public ScoreboardLine(String text) {
            this(text, 0);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title = "";
        private final List<ScoreboardLine> lines = new ArrayList<>();

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder line(String text, int score) {
            lines.add(new ScoreboardLine(text, score));
            return this;
        }

        public Builder line(String text) {
            lines.add(new ScoreboardLine(text, lines.size()));
            return this;
        }

        public Builder lines(List<String> textLines) {
            for (int i = 0; i < textLines.size(); i++) {
                lines.add(new ScoreboardLine(textLines.get(i), textLines.size() - i));
            }
            return this;
        }

        public RecordableScoreboard build() {
            return new RecordableScoreboard(title, lines);
        }
    }
}
