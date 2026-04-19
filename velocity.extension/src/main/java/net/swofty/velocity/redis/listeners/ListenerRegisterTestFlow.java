package net.swofty.velocity.redis.listeners;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.to.RegisterTestFlowProtocol;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.testflow.TestFlowManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ChannelListener
public class ListenerRegisterTestFlow extends RedisListener<
        RegisterTestFlowProtocol.Request,
        RegisterTestFlowProtocol.Response> {

    @Override
    public ProtocolObject<RegisterTestFlowProtocol.Request, RegisterTestFlowProtocol.Response> getProtocol() {
        return new RegisterTestFlowProtocol();
    }

    @Override
    public RegisterTestFlowProtocol.Response receivedMessage(RegisterTestFlowProtocol.Request message, UUID serverUUID) {
        try {
            String testFlowName = message.testFlowName();
            String handler = message.handler();
            List<String> players = message.players();

            List<TestFlowManager.ServerConfig> serverConfigs = new ArrayList<>();
            for (Map<String, Object> configMap : message.serverConfigs()) {
                JSONObject configJson = new JSONObject(configMap);
                serverConfigs.add(TestFlowManager.ServerConfig.fromJson(configJson));
            }

            TestFlowManager.registerTestFlow(testFlowName, handler, players, serverConfigs);

            System.out.println("Registered test flow '" + testFlowName + "' from server " + serverUUID);

            return new RegisterTestFlowProtocol.Response(true, "Test flow registered successfully", null);

        } catch (Exception e) {
            System.out.println("Failed to register test flow from server " + serverUUID);
            return new RegisterTestFlowProtocol.Response(false, null, e.getMessage());
        }
    }
}
