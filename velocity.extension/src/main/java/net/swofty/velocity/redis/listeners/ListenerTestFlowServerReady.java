package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.to.TestFlowServerReadyProtocol;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.testflow.TestFlowManager;

import java.util.UUID;

@ChannelListener
public class ListenerTestFlowServerReady extends RedisListener<
        TestFlowServerReadyProtocol.Request,
        TestFlowServerReadyProtocol.Response> {

    @Override
    public ProtocolObject<TestFlowServerReadyProtocol.Request, TestFlowServerReadyProtocol.Response> getProtocol() {
        return new TestFlowServerReadyProtocol();
    }

    @Override
    public TestFlowServerReadyProtocol.Response receivedMessage(TestFlowServerReadyProtocol.Request message, UUID serverUUID) {
        try {
            String testFlowName = message.testFlowName();
            ServerType serverType = ServerType.valueOf(message.serverType());
            int serverIndex = message.serverIndex();

            TestFlowManager.markServerReady(testFlowName, serverType, serverIndex, serverUUID);

            System.out.println("Server " + serverType.name() + " #" + serverIndex +
                    " is ready for test flow '" + testFlowName + "' (UUID: " + serverUUID + ")");

            return new TestFlowServerReadyProtocol.Response(true, "Server ready status recorded", null);

        } catch (Exception e) {
            System.out.println("Failed to process test flow server ready from server " + serverUUID);
            return new TestFlowServerReadyProtocol.Response(false, null, e.getMessage());
        }
    }
}
