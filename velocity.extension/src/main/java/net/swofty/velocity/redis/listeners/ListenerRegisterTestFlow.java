package net.swofty.velocity.redis.listeners;

import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.testflow.TestFlowManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.REGISTER_TEST_FLOW)
public class ListenerRegisterTestFlow extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        try {
            String testFlowName = message.getString("test_flow_name");
            String handler = message.getString("handler");

            // Parse players array
            JSONArray playersArray = message.getJSONArray("players");
            List<String> players = new ArrayList<>();
            for (int i = 0; i < playersArray.length(); i++) {
                players.add(playersArray.getString(i));
            }

            // Parse server configs array
            JSONArray serverConfigsArray = message.getJSONArray("server_configs");
            List<TestFlowManager.ServerConfig> serverConfigs = new ArrayList<>();
            for (int i = 0; i < serverConfigsArray.length(); i++) {
                JSONObject configJson = serverConfigsArray.getJSONObject(i);
                serverConfigs.add(TestFlowManager.ServerConfig.fromJson(configJson));
            }

            // Register the test flow
            TestFlowManager.registerTestFlow(testFlowName, handler, players, serverConfigs);

            System.out.println("Registered test flow '" + testFlowName + "' from server " + serverUUID);

            return new JSONObject()
                    .put("success", true)
                    .put("message", "Test flow registered successfully");

        } catch (Exception e) {
            System.out.println("Failed to register test flow from server " + serverUUID);
            return new JSONObject()
                    .put("success", false)
                    .put("error", e.getMessage());
        }
    }
}