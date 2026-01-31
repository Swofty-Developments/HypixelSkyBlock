package net.swofty.commons.replay.recordable;

import lombok.Getter;
import net.swofty.commons.replay.protocol.ReplayDataReader;
import net.swofty.commons.replay.protocol.ReplayDataWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// this is kinda bad, maybe useful for having always the updated style of messages.
// but in general I would kinda rather just have "RecordableMessage" with a string message.
@Getter
public class RecordableCustomEvent implements Recordable {
    private String eventType;
    private Map<String, String> data;
    private int tick;

    public RecordableCustomEvent() {
        this.eventType = "";
        this.data = new HashMap<>();
    }

    public RecordableCustomEvent(String eventType, Map<String, String> data) {
        this.eventType = eventType;
        this.data = data;
    }

    public String get(String key) {
        return data.get(key);
    }

    @Override
    public RecordableType getType() {
        return RecordableType.CUSTOM_EVENT;
    }

    @Override
    public void write(ReplayDataWriter writer) throws IOException {
        writer.writeString(eventType);
        writer.writeVarInt(data.size());
        for (Map.Entry<String, String> entry : data.entrySet()) {
            writer.writeString(entry.getKey());
            writer.writeString(entry.getValue());
        }
    }

    @Override
    public void read(ReplayDataReader reader) throws IOException {
        eventType = reader.readString();
        int size = reader.readVarInt();
        data = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = reader.readString();
            String value = reader.readString();
            data.put(key, value);
        }
    }

    @Override
    public int getTick() {
        return tick;
    }

    @Override
    public void setTick(int tick) {
        this.tick = tick;
    }

    @Override
    public int estimatedSize() {
        return 64 + (data.size() * 32);
    }

    @Override
    public String toString() {
        return "CustomEvent{type=" + eventType + ", data=" + data + ", tick=" + tick + "}";
    }
}
