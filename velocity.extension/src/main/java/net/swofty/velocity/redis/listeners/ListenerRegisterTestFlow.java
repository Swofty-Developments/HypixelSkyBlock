package net.swofty.velocity.redis.listeners;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.to.RegisterTestFlowProtocol;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.velocity.testflow.TestFlowManager;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ListenerRegisterTestFlow implements RedisMessageHandler<
        RegisterTestFlowProtocol.Request,
        RegisterTestFlowProtocol.Response> {

    @Override
    public RedisProtocol<RegisterTestFlowProtocol.Request, RegisterTestFlowProtocol.Response> protocol() {
        return new RegisterTestFlowProtocol();
    }

    @Override
    public RegisterTestFlowProtocol.Response handle(RegisterTestFlowProtocol.Request message, RedisMessageContext context) {
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

            Logger.info("Registered test flow '{}' from server {}", testFlowName, UUID.fromString(context.origin().id()));

            return new RegisterTestFlowProtocol.Response(true, "Test flow registered successfully", null);

        } catch (Exception e) {
            Logger.error(e, "Failed to register test flow from server {}", UUID.fromString(context.origin().id()));
            return new RegisterTestFlowProtocol.Response(false, null, e.getMessage());
        }
    }
}
