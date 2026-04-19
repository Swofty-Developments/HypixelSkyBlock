package net.swofty.commons.protocol;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ServicePushProtocolTest {

    public record TestPushRequest(UUID playerId, String item, int amount) {}
    public record TestPushResponse(boolean handled) {}

    static class TestPush extends ServicePushProtocol<TestPushRequest, TestPushResponse> {
        public TestPush() {
            super(TestPushRequest.class, TestPushResponse.class);
        }
    }

    @Test
    void channelNameDerivedFromClassName() {
        var protocol = new TestPush();
        assertEquals("TestPush", protocol.channel());
    }

    @Test
    void serializesRequestRoundTrip() {
        var protocol = new TestPush();
        UUID id = UUID.randomUUID();
        var original = new TestPushRequest(id, "diamond", 5);
        String json = protocol.translateToString(original);
        TestPushRequest result = protocol.translateFromString(json);
        assertEquals(id, result.playerId());
        assertEquals("diamond", result.item());
        assertEquals(5, result.amount());
    }

    @Test
    void serializesResponseRoundTrip() {
        var protocol = new TestPush();
        var original = new TestPushResponse(true);
        String json = protocol.translateReturnToString(original);
        TestPushResponse result = protocol.translateReturnFromString(json);
        assertTrue(result.handled());
    }
}
