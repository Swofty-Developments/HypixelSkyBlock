package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.to.TestFlowServerReadyProtocol;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.testflow.TestFlowManager;
import org.tinylog.Logger;

import java.util.UUID;

@ChannelListener
public class ListenerTestFlowServerReady extends RedisListener<
        TestFlowServerReadyProtocol.Request,
        TestFlowServerReadyProtocol.Response> {

    @Override
    public RedisProtocol<TestFlowServerReadyProtocol.Request, TestFlowServerReadyProtocol.Response> protocol() {
        return new TestFlowServerReadyProtocol();
    }

    @Override
    public TestFlowServerReadyProtocol.Response receivedMessage(TestFlowServerReadyProtocol.Request message, UUID serverUUID) {
        try {
            String testFlowName = message.testFlowName();
            ServerType serverType = ServerType.valueOf(message.serverType());
            int serverIndex = message.serverIndex();

            TestFlowManager.markServerReady(testFlowName, serverType, serverIndex, serverUUID);

            Logger.info("Server {} #{} is ready for test flow '{}' (UUID: {})",
                    serverType.name(), serverIndex, testFlowName, serverUUID);

            return new TestFlowServerReadyProtocol.Response(true, "Server ready status recorded", null);

        } catch (Exception e) {
            Logger.error(e, "Failed to process test flow server ready from server {}", serverUUID);
            return new TestFlowServerReadyProtocol.Response(false, null, e.getMessage());
        }
    }
}
