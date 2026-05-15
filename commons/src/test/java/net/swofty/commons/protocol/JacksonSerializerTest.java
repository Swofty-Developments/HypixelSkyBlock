package net.swofty.commons.protocol;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class JacksonSerializerTest {

    public record SimpleMessage(String name, int count) {}
    public record UUIDMessage(UUID playerId, String action) {}
    public record NestedMessage(String type, SimpleMessage inner) {}

    @Test
    void serializesAndDeserializesSimpleRecord() {
        var serializer = new JacksonSerializer<>(SimpleMessage.class);
        var original = new SimpleMessage("diamond_sword", 5);
        String json = serializer.serialize(original);
        SimpleMessage result = serializer.deserialize(json);
        assertEquals("diamond_sword", result.name());
        assertEquals(5, result.count());
    }

    @Test
    void serializesAndDeserializesUUIDs() {
        var serializer = new JacksonSerializer<>(UUIDMessage.class);
        UUID id = UUID.randomUUID();
        var original = new UUIDMessage(id, "join");
        String json = serializer.serialize(original);
        UUIDMessage result = serializer.deserialize(json);
        assertEquals(id, result.playerId());
        assertEquals("join", result.action());
    }

    @Test
    void serializesNestedRecords() {
        var serializer = new JacksonSerializer<>(NestedMessage.class);
        var original = new NestedMessage("test", new SimpleMessage("item", 3));
        String json = serializer.serialize(original);
        NestedMessage result = serializer.deserialize(json);
        assertEquals("test", result.type());
        assertEquals("item", result.inner().name());
        assertEquals(3, result.inner().count());
    }

    @Test
    void cloneProducesEqualButDistinctObject() {
        var serializer = new JacksonSerializer<>(SimpleMessage.class);
        var original = new SimpleMessage("test", 42);
        SimpleMessage cloned = serializer.clone(original);
        assertEquals(original, cloned);
    }
}
