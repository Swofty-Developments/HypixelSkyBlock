package net.swofty.proxyapi;

import lombok.extern.java.Log;
import net.swofty.commons.protocol.ProtocolSpecification;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.Map;
import java.util.UUID;

public class ProxyAPI {
    public ProxyAPI(String URI, UUID serverUUID) {
        RedisAPI.generateInstance(URI);
        RedisAPI.getInstance().setFilterID(serverUUID.toString());
    }

    public void registerFromProxyHandler(ProxyToClient handler) {
        RedisAPI.getInstance().registerChannel(handler.getChannel().getChannelName(), (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            UUID request = UUID.fromString(split[0].substring(split[0].indexOf(";") + 1));
            String rawMessage = split[1];
            JSONObject json = new JSONObject(rawMessage);

            JSONObject response = handler.onMessage(json);

            if (!handler.getChannel().matchesRequirementsServerSide(response)) {
                Logger.error("Handler " + handler.getClass().getName());
                throw new RuntimeException("Message does not match requirements for server side");
            }

            RedisAPI.getInstance().publishMessage(
                    "proxy",
                    ChannelRegistry.getFromName(handler.getChannel().getChannelName()),
                    request + "}=-=-={" + response.toString());
        });
    }

    public void start() {
        RedisAPI.getInstance().startListeners();
    }
}
