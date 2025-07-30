package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.testflow.TestFlowManager;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.TEST_FLOW_SERVER_READY)
public class ListenerTestFlowServerReady extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        try {
            String testFlowName = message.getString("test_flow_name");
            ServerType serverType = ServerType.valueOf(message.getString("server_type"));
            int serverIndex = message.getInt("server_index");

            // Mark the server as ready
            TestFlowManager.markServerReady(testFlowName, serverType, serverIndex, serverUUID);

            System.out.println("Server " + serverType.name() + " #" + serverIndex +
                    " is ready for test flow '" + testFlowName + "' (UUID: " + serverUUID + ")");

            return new JSONObject()
                    .put("success", true)
                    .put("message", "Server ready status recorded");

        } catch (Exception e) {
            System.out.println("Failed to process test flow server ready from server " + serverUUID);
            return new JSONObject()
                    .put("success", false)
                    .put("error", e.getMessage());
        }
    }
}