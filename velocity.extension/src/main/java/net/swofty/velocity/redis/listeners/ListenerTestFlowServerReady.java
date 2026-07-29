package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.to.TestFlowServerReadyProtocol;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.velocity.testflow.TestFlowManager;
import org.tinylog.Logger;

import java.util.UUID;

public class ListenerTestFlowServerReady implements RedisMessageHandler<
        TestFlowServerReadyProtocol.Request,
        TestFlowServerReadyProtocol.Response> {

    @Override
    public RedisProtocol<TestFlowServerReadyProtocol.Request, TestFlowServerReadyProtocol.Response> protocol() {
        return new TestFlowServerReadyProtocol();
    }

    @Override
    public TestFlowServerReadyProtocol.Response handle(TestFlowServerReadyProtocol.Request message, RedisMessageContext context) {
        try {
            String testFlowName = message.testFlowName();
            ServerType serverType = ServerType.valueOf(message.serverType());
            int serverIndex = message.serverIndex();

            TestFlowManager.markServerReady(testFlowName, serverType, serverIndex, UUID.fromString(context.origin().id()));

            Logger.info("Server {} #{} is ready for test flow '{}' (UUID: {})",
                    serverType.name(), serverIndex, testFlowName, UUID.fromString(context.origin().id()));

            return new TestFlowServerReadyProtocol.Response(true, "Server ready status recorded", null);

        } catch (Exception e) {
            Logger.error(e, "Failed to process test flow server ready from server {}", UUID.fromString(context.origin().id()));
            return new TestFlowServerReadyProtocol.Response(false, null, e.getMessage());
        }
    }
}
